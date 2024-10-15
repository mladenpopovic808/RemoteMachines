package rs.raf.domaci3;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.repositories.RoleRepository;
import rs.raf.domaci3.services.RoleService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void addRole_Test() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        roleService.addRole(role);

        verify(roleRepository).save(role);
    }

    @Test
    public void getAllRoles_Test() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("User");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<Role> roles = roleService.getAllRoles();

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));

        verify(roleRepository).findAll();
    }
}
