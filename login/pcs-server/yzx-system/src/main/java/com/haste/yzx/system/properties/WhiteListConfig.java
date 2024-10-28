package com.haste.yzx.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "whitelist")
@Data
public class WhiteListConfig {
    private List<String> urls;
}
