package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entitis.User;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByName(String name);

//    User findByLogin(String login);
}
