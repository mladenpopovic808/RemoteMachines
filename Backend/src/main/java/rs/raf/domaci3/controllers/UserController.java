package rs.raf.domaci3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.dto.UserDto;
import rs.raf.domaci3.repositories.RoleRepository;
import rs.raf.domaci3.requests.LoginRequest;
import rs.raf.domaci3.response.LoginResponse;
import rs.raf.domaci3.services.UserService;
import rs.raf.domaci3.utils.JwtUtil;

import javax.print.attribute.standard.Media;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    //prebaci u service samo 
    private final UserService userService;
    private final RoleRepository roleService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, RoleRepository roleRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleService = roleRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    @Operation(description = "za Login korisnika")
    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        //U response odma pakujemo i roles da ne bi posle fetchovao na frontu
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getEmail()), userService.getRolesForUser(loginRequest.getEmail())));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getAllUsers(){

        return ResponseEntity.ok().body(userService.getAllUsers());
    }
    @GetMapping(value="/get/{id}")
    public ResponseEntity<?>getUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){

        User updatedUser=userService.updateUser(userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){

        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
