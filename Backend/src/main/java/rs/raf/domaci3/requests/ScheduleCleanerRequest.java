package rs.raf.domaci3.requests;

import lombok.Data;

@Data
public class ScheduleCleanerRequest {
    private Long id;
    private String date;
    private String time;
    private String action;
}
