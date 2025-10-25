package br.com.lrbarros.assinador.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "crypto")
@Getter
@Setter
public class CryptoProperties {
    private String keystorePath;
    private String keystorePassword;
    private String keyAlias;
    private String keyPassword;

}
