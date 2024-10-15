package rs.raf.domaci3.controllers;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.domaci3.model.Cleaner;
import rs.raf.domaci3.repositories.CleanerRepository;
import rs.raf.domaci3.requests.CreateCleanerRequest;
import rs.raf.domaci3.requests.ScheduleCleanerRequest;
import rs.raf.domaci3.services.CleanerService;
import rs.raf.domaci3.services.UserService;

import javax.print.attribute.standard.Media;
import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/cleaners")
public class CleanerController {

    private  CleanerService cleanerService;
    private  UserService userService;

    @Autowired
    public CleanerController(CleanerService cleanerService,UserService userService){
        this.cleanerService=cleanerService;
        this.userService=userService;

    }
    //TODO Pogledaj exceptione zasto se bacaju
    //TODO pogledaj da li treba @PathVariable umesto @PathParam tamo gde nisu navedeni parametri u URI-ju


    @GetMapping(value ="/getCleaner/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Cleaner> findCleanerById(@PathVariable("id") Long id){
        return cleanerService.findById(id);
    }


    @PostMapping(value="/add",produces = MediaType.APPLICATION_JSON_VALUE)
    public Cleaner createCleaner(@RequestBody CreateCleanerRequest request){
        return cleanerService.addCleaner(request.getName(), request.getEmail());

    }
    @GetMapping(value="/remove/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>removeCleaner(@PathVariable("id")Long id){

        return cleanerService.removeCleaner(id);

    }

    @GetMapping(value="/start/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>startCleaner(@PathVariable("id") Long id) throws InterruptedException{

        Optional<Cleaner>cleaner=cleanerService.findById(id);
        if(!cleaner.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return cleanerService.callStartCleaner(id,false);

    }
    @GetMapping(value = "/stop/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>stopCleaner(@PathVariable("id")Long id) throws InterruptedException{
        Optional<Cleaner> optionalCleaner=cleanerService.findById(id);
        if(!optionalCleaner.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return cleanerService.callStopCleaner(id,false);

    }
    @GetMapping(value = "/discharge/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>dischargeCleaner(@PathVariable("id")Long id) throws InterruptedException{
        Optional<Cleaner> optionalCleaner=cleanerService.findById(id);
        if(!optionalCleaner.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return cleanerService.callDischargeCleaner(id,false);

    }

    @PostMapping(value ="/schedule")
    public ResponseEntity<Map<String,String>>schedule(@RequestBody ScheduleCleanerRequest request) throws ParseException {

        return cleanerService.scheduleCleaner(request.getId(),request.getDate(),request.getTime(),request.getAction());

    }
    @GetMapping(value ="/allByUser",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Cleaner>> getCleanersByUser(@PathParam("email") String email){
        return ResponseEntity.ok(cleanerService.getCleanersByUser(email));
    }
    @GetMapping(value = "/errorsByUser",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>getErrorHistory(@PathParam("email") String email){

        return ResponseEntity.ok(cleanerService.findAllErrorsForUser(email));
    }

    @GetMapping(value = "/filter",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Cleaner>> filterCleaners(
            @PathParam("email")String email,
            @PathParam("name")String name,
            @PathParam("statusList")String  statusList,
            @PathParam("dateFrom") String dateFrom,
            @PathParam("dateTo")String dateTo){

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFrom2=null;
        LocalDate dateTo2=null;
        List<String> statusList2=null;
        if(!dateFrom.equals("null")){
            dateFrom2=LocalDate.parse(dateFrom,formatter);
        }
        if(!dateTo.equals("null")) {
            dateTo2=LocalDate.parse(dateTo,formatter);
        }
        if(statusList!=null && statusList.length()!=0) {
            statusList2=new ArrayList<>(Arrays.asList(statusList.split(",")));
        }

        return ResponseEntity.ok().body(cleanerService.searchCleaners(name,statusList2,dateFrom2,dateTo2,email));
    }

}











