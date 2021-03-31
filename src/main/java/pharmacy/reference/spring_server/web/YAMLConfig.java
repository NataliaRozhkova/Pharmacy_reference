package pharmacy.reference.spring_server.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private String statisticPath;
    private String emailDownloadPath;
    private String emailAddress;
    private String password;

    public String getEmailDownloadPath() {
        return emailDownloadPath;
    }

    public String getStatisticPath() {
        return statisticPath;
    }

    public void setStatisticPath(String statisticPath) {
        this.statisticPath = statisticPath;
    }

    public void setEmailDownloadPath(String emailDownloadPath) {
        this.emailDownloadPath = emailDownloadPath;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
