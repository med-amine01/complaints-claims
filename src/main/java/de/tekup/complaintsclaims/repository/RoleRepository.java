package de.tekup.complaintsclaims.repository;


import de.tekup.complaintsclaims.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRoleName(String role);

    Role findByRoleName(String role);
}