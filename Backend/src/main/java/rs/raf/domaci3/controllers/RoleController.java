package rs.raf.domaci3.controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.repositories.RoleRepository;
import rs.raf.domaci3.services.RoleService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value ="/all")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    //dodaj addRole ako zatreba



}
