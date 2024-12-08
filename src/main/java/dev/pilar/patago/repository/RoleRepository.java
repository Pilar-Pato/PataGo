package dev.pilar.patago.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.pilar.patago.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
