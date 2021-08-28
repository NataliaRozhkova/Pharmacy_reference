package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pharmacy.reference.spring_server.entitis.Role;
import pharmacy.reference.spring_server.entitis.User;
import pharmacy.reference.spring_server.repositories.RoleRepository;
import pharmacy.reference.spring_server.repositories.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service("userDetailsService")
@Transactional
public class UsersService implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        try {
            User user = usersRepository.findByName(name);
            if (user == null) {
                return new org.springframework.security.core.userdetails.User(
                        " ", " ", true, true, true, true,
                        getAuthorities(roleRepository.findByName("ROLE_USER")));
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getName(), user.getPassword(), user.isEnabled(), true, true, true,
                    getAuthorities(user.getRoles()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String userName) {
        return usersRepository.findByName(userName);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        roles.forEach(role -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(
                    role.getName()));
        });
        return grantedAuthorities;
    }

    public void changeUserPassword(User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        usersRepository.save(user);
    }

    public void saveUser(String userName, String password) throws Exception {
        if (usersRepository.findByName(userName) != null) {
            throw new Exception("Пользователь с таким именем уже существует");
        }
        User user = new User();
        user.setName(userName);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> role = new HashSet<>();
        role.add(roleRepository.findByName("ROLE_USER").get(0));
        user.setRoles(role);
        usersRepository.save(user);
    }


}
