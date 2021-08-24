package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entitis.Role;

import java.util.List;

public interface RoleRepository  extends JpaRepository<Role, Long> {

    List<Role> findByName(String role_user);
}
