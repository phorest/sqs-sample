package com.phorest.sample.sqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(AwsCredentialsProperties.class)
public class AwsCredentialsConfiguration {

    @Autowired
    private AwsCredentialsProperties awsCredentialsProperties;

    @PostConstruct
    public void init() {
        System.setProperty("aws.accessKeyId", awsCredentialsProperties.getAccessKeyId());
        System.setProperty("aws.secretKey", awsCredentialsProperties.getSecretKey());
    }
}

