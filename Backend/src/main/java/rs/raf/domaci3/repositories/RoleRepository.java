package rs.raf.domaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.domaci3.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    public Role findByName(String name);


}
