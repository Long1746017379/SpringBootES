package com.example.demo.config;


/**
 * @author zht
 * @date 2020/11/17 11:59
 */

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "es")
public class EsConfig {

    private volatile static RestHighLevelClient restHighLevelClient;

    private static String esUserName;
    private static String esUserPassword;

    //es主机地址
    private static String encryHostAddress;

    private static int ports;


    @Value("${es.cluster.host}")
    public void setEncryHostAddress(String host) {
        encryHostAddress = host;
    }


    @Value("${es.cluster.port}")
    public void setPort(int port) {
        ports = port;
    }

    @Value("${es.user.name}")
    public void setEsUserName(String esUserName) {
        EsConfig.esUserName = esUserName;
    }

    @Value("${es.user.password}")
    public void setEsUserPassword(String esUserPassword) {
        EsConfig.esUserPassword = esUserPassword;
    }

    @Bean
    public static RestHighLevelClient getclient() {
        if (restHighLevelClient == null) {
            try {
                String[] host = encryHostAddress.split(",");
                // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是ip,参数2是HTTP端口，参数3是通信协议
                RestClientBuilder clientBuilder = RestClient.builder(new HttpHost(host[0], ports, "http"));
		        /*
		         如果ES设置了密码，那这里也提供了一个基本的认证机制，下面设置了ES需要基本身份验证的默认凭据提供程序
		        */
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUserName, esUserPassword));

                clientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

                // 最后配置好的clientBuilder再build一下即可得到真正的Client
                restHighLevelClient = new RestHighLevelClient(clientBuilder);
            } catch (Exception e) {
                throw new IllegalStateException("decrypt elastic host error", e);
            }
        }
        return restHighLevelClient;
    }

}

