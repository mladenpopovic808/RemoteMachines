package rs.raf.domaci3.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.dto.UserDto;
import rs.raf.domaci3.repositories.RoleRepository;
import rs.raf.domaci3.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Redis pokreni sa redis-server u command promptu
    //Redis pokreni sa redis-server u command promptu
    //Redis pokreni sa redis-server u command promptu
    @Cacheable(value = "userById", key = "#id")
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Cacheable(value = "allUsers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "rolesForUser", key = "#mail")
    @Override
    public List<Role> getRolesForUser(String mail) {
        return new ArrayList<>(userRepository.findByEmail(mail).getRoles());
    }
    @Override
    @Transactional
    @Cacheable(value = "userByEmail", key = "#email")
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @CacheEvict(value = "allUsers", allEntries = true)
    @Override
    public User addUser(User user) {
        //Hashiramo pass pre cuvanja
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        List<Role>roles=new ArrayList<>();
        for(Role role:user.getRoles()){
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Caching(put = {
            @CachePut(value = "userById", key = "#user.id"),
            @CachePut(value = "userByEmail", key = "#user.email"),

    },evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
    })
    @Override
    public User updateUser(UserDto user) {
        User updatedUser = userRepository.getById(user.getId());
        updatedUser.setName(user.getName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setRoles(new ArrayList<>(Arrays.asList(user.getRoles())));
        return userRepository.save(updatedUser);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", allEntries = true),
            @CacheEvict(value = "userByEmail", key = "#mail"),
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "rolesForUser", key = "#mail"),
    })
    @Override
    public void addRoleToUser(String mail, String roleName) {
        Role role = roleRepository.findByName(roleName);
        User user=userRepository.findByEmail(mail);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userByEmail", allEntries = true),
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "rolesForUser", allEntries = true),
    })

    @Override
    public void deleteUser(Long id) {
         userRepository.deleteById(id);
    }
}
