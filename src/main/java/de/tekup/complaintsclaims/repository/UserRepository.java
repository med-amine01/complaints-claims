package de.tekup.complaintsclaims.repository;


import de.tekup.complaintsclaims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);

    boolean existsByName(String username);

    boolean existsByEmail(String email);
}