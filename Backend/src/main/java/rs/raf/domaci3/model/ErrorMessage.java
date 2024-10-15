package rs.raf.domaci3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;


    private String action;

    @Column(nullable = false)
    private LocalDate date;

    //Jedan errorMessage moze pripadati vise usisivacima
    @ManyToOne
    private Cleaner cleaner;
}
