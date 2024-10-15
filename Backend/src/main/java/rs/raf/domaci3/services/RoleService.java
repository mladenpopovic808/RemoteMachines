package rs.raf.domaci3.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleService implements RoleServiceInterface {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @CacheEvict(value = "allRoles", allEntries = true)
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    @Cacheable(value = "allRoles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
