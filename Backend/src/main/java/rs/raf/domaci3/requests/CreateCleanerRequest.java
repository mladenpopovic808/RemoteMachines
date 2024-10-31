package rs.raf.domaci3.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCleanerRequest implements Serializable {
    private String email;
    private String name;
}
