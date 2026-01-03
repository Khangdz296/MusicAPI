package peterpan.api.repository;

import peterpan.api.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findBySessionKey(String sessionKey);
    
    boolean existsByUsernameOrEmail(String username, String email);
}