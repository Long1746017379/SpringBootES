package com.example.demo.utils.sftp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/01/31 1:09
 */
@Component
public class SftpConfig {

    private static String sftpHost;

    private static int sftpPort;

    private static String sftpUserName;

    private static String sftpPassWord;

    @Value("${sftp.host}")
    public void setSftpHost(String sftpHost) {
        SftpConfig.sftpHost = sftpHost;
    }

    @Value("${sftp.port}")
    public void setSftpPort(int sftpPort) {
        SftpConfig.sftpPort = sftpPort;
    }

    @Value("${sftp.userName}")
    public void setSftpUserName(String sftpUserName) {
        SftpConfig.sftpUserName = sftpUserName;
    }

    @Value("${sftp.passWord}")
    public void setSftpPassWord(String sftpPassWord) {
        SftpConfig.sftpPassWord = sftpPassWord;
    }



}
