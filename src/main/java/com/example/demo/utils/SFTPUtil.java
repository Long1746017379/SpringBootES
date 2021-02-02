package com.example.demo.utils;

import com.example.demo.utils.oss.AliyunOSSUtil;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/01/18 18:32
 */
@Slf4j
public class SFTPUtil {

    private static String username = "sftpuser";

    private static String password = "1qazMJU&";

    private static String host = "10.242.31.210";

    private static int port = 3222;

    private static ChannelSftp sftp = null;

    private static Session session = null;

    private static Channel channel = null;

    /**
     * 获取sftp客户端连接
     */
    public static ChannelSftp getSftpClient() {
        if (sftp == null) {
            try {
                JSch jsch = new JSch();
                session = jsch.getSession(username, host, port);
                if (password != null) {
                    session.setPassword(password);
                }
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");

                session.setConfig(config);
                session.connect();
                log.info("SFTP Session connected...");
                channel = session.openChannel("sftp");
                channel.connect();
                sftp = (ChannelSftp) channel;
                log.info("Connected to " + host);
            } catch (JSchException e) {
                e.printStackTrace();
                log.error("与SFTP主机{}建立连接失败：", host, e.getMessage());
            }
        }
        return sftp;
    }

    /**
     * 关闭连接
     */
    public static void close() {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.getSession().disconnect();
                    sftp.disconnect();
                    sftp.isClosed();
                    log.info("关闭sftp客户端连接。。。");
                }
            }

            if (channel != null) {
                if (channel.isConnected()) {
                    channel.disconnect();
                    log.info("关闭channel连接。。。");
                }
            }

            if (session != null) {
                if (session.isConnected()) {
                    session.disconnect();
                    log.info("关闭session会话。。。");
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否与ftp主机连接
     *
     * @return
     */
    public static boolean isConnect() {
        if (null != session) {
            return session.isConnected();
        }
        return false;
    }


    /**
     * 判断该路径是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isExistFilePath(String filePath) {
        if (sftp != null) {
            try {
                SftpATTRS sftpATTRS = sftp.lstat(filePath);
                return sftpATTRS.isDir();
            } catch (Exception e) {
                log.error(e.getMessage());
                return false;
            }
        }
        log.error("sftp 连接对象为null，请先创建连接");
        return false;
    }


    /**
     * 判断该路径下的文件是否存在
     *
     * @param filePath
     * @param folderName
     * @return
     */
    public static boolean isExist(String filePath, String folderName) {
        if (sftp != null) {
            try {
                if (isExistFilePath(filePath)) {
                    sftp.cd(filePath);
                    log.info("切换到当前路径：" + filePath);
                    SftpATTRS attrs = null;
                    attrs = sftp.stat(folderName);
                    if (attrs != null) {
                        return true;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                return false;
            }
        }
        log.error("sftp 连接对象为null，请先创建连接");
        return false;
    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public static Vector<?> listFiles(String directory) throws SftpException {
        if (sftp != null) {
            if (isExistFilePath(directory)) {
                return sftp.ls(directory);
            }
        }
        log.error("sftp 连接对象为null，请先创建连接");
        return null;
    }


    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件名字
     */
    public static void delete(String directory, String deleteFile) {
        try {
            if (sftp != null) {
                if (isExistFilePath(directory)) {
                    sftp.cd(directory);
                    sftp.rm(deleteFile);
                } else {
                    log.error("要删除文件所在目录：{} 不存在", directory);
                }
            } else {
                log.error("sftp 连接对象为null，请先创建连接");
            }
        } catch (Exception e) {
            log.error("删除文件异常：{}", e.getMessage());
        }
    }


    /**
     * 测试读取SFTP文件是否乱码
     *
     * @param directory
     * @param fileName
     */
    public static List testReadSftp(String directory, String fileName) {
        List<String> list = new ArrayList<>();
        InputStream inputStream = null;
        BufferedReader br = null;
        try {
            if (sftp != null) {
                if (directory != null && !"".equals(directory)) {
//                    sftp.cd(directory);
                    String filePathName = directory + "/" + fileName;
                    inputStream = sftp.get(filePathName);
                    log.info("inputStream.available()大小为：{}", inputStream.available());

                    StringBuffer stringBuffer = new StringBuffer();
                    br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line = null;
                    int nowLine = 1;
                    while ((line = br.readLine()) != null) {
                        log.info("第 {} 行数据：{}", nowLine, line);
                        list.add(line);
                        if (nowLine == 3) {
                            break;
                        }
                        nowLine++;
                    }
                }
            } else {
                log.error("sftp 连接对象为null，请先创建连接");
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    log.info("关闭InputStream流");
                }
                if (br != null) {
                    br.close();
                    log.info("关闭BufferedReader流");
                }
                close();

                String flag = isConnect() ? "关闭" : "未关闭";
                log.info("{}", flag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 获取SFTP文件InputStream流
     *
     * @param directory 下载目录
     * @param fileName  下载文件名
     * @return InputStream
     */
    public static void download(String directory, String fileName) {
        InputStream inputStream = null;
        InputStream rspInputStream = null;
        BufferedReader br = null;
        try {
            if (sftp != null) {
                if (directory != null && !"".equals(directory)) {
                    sftp.cd(directory);
                    inputStream = sftp.get(fileName);

                    StringBuffer stringBuffer = new StringBuffer();
                    br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line = null;
                    int nowLine = 1;
                    int num = 1;
                    while ((line = br.readLine()) != null) {
                        stringBuffer.append(line + "\r\n");
                        // 每100W条写入OSS一个txt文件
                        if ((nowLine % 1000000 == 0)) {
                            String localUrl = AliyunOSSUtil.uploadTxtFileToOSS("text/plain", "114商家信息" + num + ".txt", new ByteArrayInputStream(stringBuffer.toString().getBytes()));
                            log.info("写入 114商家信息{}.txt，位置：{}", num, localUrl);
                            num += 1;
                            stringBuffer.setLength(0);
                        }
                        nowLine++;
                    }
                    if (line == null && (stringBuffer.length() > 0)) {
                        String localUrl = AliyunOSSUtil.uploadTxtFileToOSS("text/plain", "114商家信息" + num + ".txt", new ByteArrayInputStream(stringBuffer.toString().getBytes()));
                        log.info("写入 114商家信息{}.txt，位置：{}", num, localUrl);
                    }
                    log.info("共 {}行数据", nowLine);
                }
            } else {
                log.error("sftp 连接对象为null，请先创建连接");
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (rspInputStream != null) {
                    rspInputStream.close();
                }
                if (br != null) {
                    br.close();
                }
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
