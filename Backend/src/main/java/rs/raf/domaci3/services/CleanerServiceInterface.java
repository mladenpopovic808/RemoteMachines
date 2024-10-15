package rs.raf.domaci3.services;

import org.springframework.http.ResponseEntity;
import rs.raf.domaci3.model.Cleaner;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CleanerServiceInterface {

    Cleaner addCleaner(String name, String userMail);
    Optional<Cleaner>findById(Long id);

    Collection<Cleaner>getCleanersByUser(String userMail);

    //Za filter pretrage usisivaca.
    Collection<Cleaner>searchCleaners(String name, List<String> statuses, LocalDate dateFrom, LocalDate dateTo, String userMail);

    ResponseEntity<Map<String,String>> removeCleaner(Long id);



    ResponseEntity<Map<String,String>> callStartCleaner(Long id, boolean scheduled) throws InterruptedException;

    ResponseEntity<Map<String,String>>callStopCleaner(Long id,boolean scheduled) throws InterruptedException;

    ResponseEntity<Map<String,String>> callDischargeCleaner(Long id,boolean scheduled) throws InterruptedException;

    ResponseEntity<Map<String,String>> scheduleCleaner(Long id,String date,String time,String action) throws ParseException;

    //Ove metode su prebacene u drugi servis
//    void stopCleaner(Long id, boolean scheduled) throws InterruptedException;
//    void startCleaner(Long id,boolean scheduled) throws InterruptedException;
//    void dischargeCleaner(Long id, boolean scheduled) throws InterruptedException;



}
