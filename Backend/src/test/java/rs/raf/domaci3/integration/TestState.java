package rs.raf.domaci3.integration;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestState {

    String jwtToken;
    int actualResponseStatus;
}
