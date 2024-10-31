package rs.raf.domaci3.unitTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.dto.UserDto;
import rs.raf.domaci3.repositories.RoleRepository;
import rs.raf.domaci3.repositories.UserRepository;
import rs.raf.domaci3.services.UserService;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class) // JUnit 4 Runner
@ExtendWith(MockitoExtension.class) // JUnit 4 Runner
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private  RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserById_Test(){
        User user=createDummyUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User>optionalUser=userRepository.findById(1L);
        User user2=optionalUser.get();


//        assertEquals(user,user2);
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getLastName(), user2.getLastName());
        assertEquals(user.getPassword(), user2.getPassword());
        assertEquals(user.getLastName(), user2.getLastName());
        assertEquals(user.getEmail(), user2.getEmail());
    }

    @Test
    public void getAllUsers_Test(){

        List<User>users=createDummyUsers();
        when(userRepository.findAll()).thenReturn(users);

        List<User>users2=userRepository.findAll();

        assertEquals(users,users2);

    }

    @Test
    public void addUser_test(){

        User user=createDummyUser();

        when(passwordEncoder.encode(user.getPassword())).thenReturn("HashedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User user2=userService.addUser(user);

        assertEquals(user,user2);

    }

    @Test
    public void getUserByEmail_Test() {
        User user=createDummyUser();
        when(userRepository.findByEmail("mladen.priv@gmail.com")).thenReturn(user);

        User user2=userService.getUserByEmail("mladen.priv@gmail.com");

        assertEquals(user,user2);

    }

    @Test
    public void addRoleToUser_Test() {
        Role role=createRole();
        User user=createDummyUser();

        when(roleRepository.findByName("Admin")).thenReturn(role);
        when(userRepository.findByEmail("mladen.priv@gmail.com")).thenReturn(user);

        userService.addRoleToUser(user.getEmail(), "Admin");

        assertTrue(user.getRoles().contains(role));
        verify(userRepository).save(user);
    }

    @Test
    public void getRolesForUser_Test(){

        List<Role>roleList=createDummyRoles();
        User user=createDummyUser();
        user.setRoles(roleList);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        List<Role> list2 = userService.getRolesForUser(user.getEmail());
        assertEquals(roleList, list2);

    }

    @Test
    public void updateUser_Test() {

        User existingUser = createDummyUser();
        UserDto userDto = createDummyUserDto();

        when(userRepository.getById(userDto.getId())).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userDto);

        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getLastName(), updatedUser.getLastName());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(Arrays.asList(userDto.getRoles()), updatedUser.getRoles());


        verify(userRepository).getById(userDto.getId());
        verify(userRepository).save(updatedUser);
    }

    @Test
    public void deleteUser_Test() {
        Long userId = 1L;
        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }



    private UserDto createDummyUserDto() {
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("Admin");

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("User");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Mladen");
        userDto.setLastName("Popovic");
        userDto.setEmail("mladen.priv@gmail.com");
        userDto.setRoles(new Role[]{adminRole, userRole}); // Postavljanje rola

        return userDto;
    }


    private Role createRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");
        return role;
    }


    private List<Role> createDummyRoles() {
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("Admin");
        roles.add(adminRole);

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("User");
        roles.add(userRole);

        Role guestRole = new Role();
        guestRole.setId(3L);
        guestRole.setName("Guest");
        roles.add(guestRole);

        return roles;
    }





    private User createDummyUser() {
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("Admin");

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("User");

        User user = new User();
        user.setId(1L);
        user.setName("Mladen");
        user.setLastName("Popovic");
        user.setEmail("mladen.priv@gmail.com");
        user.setPassword("makelele");
        user.setRoles(new ArrayList<>(Arrays.asList(adminRole, userRole)));  // Dodavanje rola

        return user;
    }

    private List<User> createDummyUsers() {
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("Admin");

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("User");

        Role guestRole = new Role();
        guestRole.setId(3L);
        guestRole.setName("Guest");

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Mladen");
        user1.setLastName("Popovic");
        user1.setEmail("mladen.priv@gmail.com");
        user1.setPassword("makelele");
        user1.setRoles(new ArrayList<>(Arrays.asList(adminRole, userRole))); // Mutable lista

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Ivan");
        user2.setLastName("Jovic");
        user2.setEmail("ivan.jovic@gmail.com");
        user2.setPassword("password123");
        user2.setRoles(new ArrayList<>(Arrays.asList(userRole))); // Mutable lista

        User user3 = new User();
        user3.setId(3L);
        user3.setName("Ana");
        user3.setLastName("Markovic");
        user3.setEmail("ana.markovic@gmail.com");
        user3.setPassword("anaPass");
        user3.setRoles(new ArrayList<>(Arrays.asList(guestRole))); // Mutable lista

        return Arrays.asList(user1, user2, user3);
    }

}






