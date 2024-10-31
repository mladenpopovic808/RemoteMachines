package rs.raf.domaci3.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import rs.raf.domaci3.model.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private List<Role> roles;



}
