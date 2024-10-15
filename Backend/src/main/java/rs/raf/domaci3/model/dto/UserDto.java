package rs.raf.domaci3.model.dto;

import lombok.Data;
import lombok.ToString;
import rs.raf.domaci3.model.Role;

@Data
@ToString
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Role[] roles;


}
