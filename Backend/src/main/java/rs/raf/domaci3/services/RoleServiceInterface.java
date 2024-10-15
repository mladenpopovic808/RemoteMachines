package rs.raf.domaci3.services;

import rs.raf.domaci3.model.Role;

import java.util.List;

public interface RoleServiceInterface {
    public void addRole(Role role);
    public List<Role> getAllRoles();
}
