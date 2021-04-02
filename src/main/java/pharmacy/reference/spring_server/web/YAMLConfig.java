package pharmacy.reference.spring_server.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private String statisticPath;
    private String emailDownloadPath;
    private String emailAddress;
    private String emailPassword;
    private Map<String, Credential> users =  new HashMap<>();;

    public String getEmailDownloadPath() {
        return emailDownloadPath;
    }

    public String getStatisticPath() {
        return statisticPath;
    }

    public void setUsers(Map<String, Credential> users) {
        this.users = users;
    }

    public Map<String, Credential> getUsers() {
        return users;
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

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public static class Credential {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
