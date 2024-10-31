package rs.raf.domaci3.services;

import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.dto.UserDto;

import java.util.List;

public interface UserServiceInterface {

    User getUserById(Long id);
    List<User>getAllUsers();

    User addUser(User user);

    User getUserByEmail(String mail); //ova logika postoji vec u UserDetailService ali neka ga


    void addRoleToUser(String mail,String roleName);

    List<Role> getRolesForUser(String mail);

    User updateUser(UserDto user);
    void deleteUser(Long id);







}
