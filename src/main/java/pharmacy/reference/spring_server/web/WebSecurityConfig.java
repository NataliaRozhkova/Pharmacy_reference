package pharmacy.reference.spring_server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private YAMLConfig config;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/file").hasRole("ADMIN")
                .antMatchers("/file/**").hasRole("ADMIN")
                .antMatchers("/pharmacy/parse").hasRole("ADMIN")
                .antMatchers("/auxiliary").hasRole("ADMIN")
                .antMatchers("/email").hasRole("ADMIN")

                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .csrf().disable()
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("user1")
                .password(passwordEncoder.encode(config.getUsers().get("user1").getPassword()))
                .roles("USER")
                .and()
                .withUser("user2")
                .password(passwordEncoder.encode(config.getUsers().get("user2").getPassword()))
                .roles("USER")
                .and()
                .withUser("user3")
                .password(passwordEncoder.encode(config.getUsers().get("user3").getPassword()))
                .roles("USER")
                .and()
                .withUser("user4")
                .password(passwordEncoder.encode(config.getUsers().get("user4").getPassword()))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder.encode(config.getUsers().get("admin").getPassword()))
                .roles("USER", "ADMIN");
    }

    @Autowired
    public void setConfig(YAMLConfig config) {
        this.config = config;
    }
}