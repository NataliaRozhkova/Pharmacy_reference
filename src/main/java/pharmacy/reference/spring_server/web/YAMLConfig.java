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
    private String logfilePath;
    private Map<String, Credential> users = new HashMap<>();
    ;

    public String getEmailDownloadPath() {
        return emailDownloadPath;
    }

    public void setEmailDownloadPath(String emailDownloadPath) {
        this.emailDownloadPath = emailDownloadPath;
    }

    public String getStatisticPath() {
        return statisticPath;
    }

    public void setStatisticPath(String statisticPath) {
        this.statisticPath = statisticPath;
    }

    public Map<String, Credential> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Credential> users) {
        this.users = users;
    }

    public String getLogfilePath() {
        return logfilePath;
    }

    public void setLogfilePath(String logfilePath) {
        this.logfilePath = logfilePath;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public static class Credential {

        private String username;
        private String password;

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
    }

}
