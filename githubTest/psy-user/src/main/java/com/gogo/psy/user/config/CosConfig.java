package com.gogo.psy.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.cloud")
public class CosConfig {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;
}
