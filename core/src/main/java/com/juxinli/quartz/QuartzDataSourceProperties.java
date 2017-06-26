package com.juxinli.quartz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by maybo on 17/5/11.
 */
@Component
@ConfigurationProperties(prefix="quartz")
public class QuartzDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    @Value("${datasourceEnabled:false}")
    private boolean datasourceEnabled;
    @Value("${enabled:false}")
    private boolean enabled;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public boolean isDatasourceEnabled() {
        return datasourceEnabled;
    }

    public void setDatasourceEnabled(boolean datasourceEnabled) {
        this.datasourceEnabled = datasourceEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "QuartzDataSourceProperties{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", datasourceEnabled=" + datasourceEnabled +
                ", enabled=" + enabled +
                '}';
    }
}
