package rs.raf.domaci3.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import rs.raf.domaci3.model.Role;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private List<Role> roles;



}
