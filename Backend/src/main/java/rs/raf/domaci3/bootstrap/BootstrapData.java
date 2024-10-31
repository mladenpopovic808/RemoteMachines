package rs.raf.domaci3.bootstrap;


import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.services.CleanerService;
import rs.raf.domaci3.services.RoleService;
import rs.raf.domaci3.services.UserService;

@Data
@Component
public class BootstrapData implements CommandLineRunner {


    private final UserService userService;
    private final RoleService roleService;
    private final CleanerService cleanerService;

    public BootstrapData(UserService userService, RoleService roleService, CleanerService cleanerService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cleanerService = cleanerService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Insertujem podataka u bazu..");

        roleService.addRole(new Role(null,"can_read_users"));
        roleService.addRole(new Role(null,"can_update_users"));
        roleService.addRole(new Role(null,"can_delete_users"));
        roleService.addRole(new Role(null,"can_create_users"));

        roleService.addRole(new Role(null,"can_search_cleaners"));
        roleService.addRole(new Role(null,"can_add_cleaners"));
        roleService.addRole(new Role(null,"can_stop_cleaners"));
        roleService.addRole(new Role(null,"can_start_cleaners"));
        roleService.addRole(new Role(null,"can_discharge_cleaners"));
        roleService.addRole(new Role(null,"can_remove_cleaners"));
        roleService.addRole(new Role(null,"can_schedule_cleaners"));

        userService.addUser(new User(null,"Admin","Popovic","admin@raf.rs","123"));
        userService.addUser(new User(null,"User1","Popovic","user1@raf.rs","123"));
        userService.addUser(new User(null,"User2","Popovic","user2@raf.rs","123"));

        userService.addRoleToUser("admin@raf.rs","can_read_users");
        userService.addRoleToUser("admin@raf.rs","can_create_users");
        userService.addRoleToUser("admin@raf.rs","can_update_users");
        userService.addRoleToUser("admin@raf.rs","can_delete_users");

        userService.addRoleToUser("admin@raf.rs","can_search_cleaners");
        userService.addRoleToUser("admin@raf.rs","can_add_cleaners");
        userService.addRoleToUser("admin@raf.rs","can_stop_cleaners");
        userService.addRoleToUser("admin@raf.rs","can_start_cleaners");
        userService.addRoleToUser("admin@raf.rs","can_discharge_cleaners");
        userService.addRoleToUser("admin@raf.rs","can_remove_cleaners");
        userService.addRoleToUser("admin@raf.rs","can_schedule_cleaners");
        userService.addRoleToUser("user2@raf.rs","can_read_users");



        cleanerService.addCleaner("Cleaner1","admin@raf.rs");
        cleanerService.addCleaner("Cleaner2","admin@raf.rs");
        cleanerService.addCleaner("Cleaner3","admin@raf.rs");
        cleanerService.addCleaner("Cleaner4","admin@raf.rs");




    }
}
