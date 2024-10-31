package rs.raf.domaci3.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class ScheduleCleanerRequest implements Serializable {
    private Long id;
    private String date;
    private String time;
    private String action;
}
