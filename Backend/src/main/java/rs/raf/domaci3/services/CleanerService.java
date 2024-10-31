package rs.raf.domaci3.services;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.raf.domaci3.model.Cleaner;
import rs.raf.domaci3.model.ErrorMessage;
import rs.raf.domaci3.model.status.Status;
import rs.raf.domaci3.repositories.CleanerRepository;
import rs.raf.domaci3.repositories.ErrorMessageRepository;
import rs.raf.domaci3.repositories.UserRepository;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class CleanerService implements CleanerServiceInterface {

    private CleanerRepository cleanerRepository;
    private UserRepository userRepository;
    private TaskScheduler taskScheduler;
    private ErrorMessageRepository errorMessageRepository;
    private AsyncCleanerService asyncCleanerService;


    public CleanerService(AsyncCleanerService asyncCleanerService,CleanerRepository cleanerRepository, UserRepository userRepository, TaskScheduler taskScheduler, ErrorMessageRepository errorMessageRepository) {
        this.cleanerRepository = cleanerRepository;
        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
        this.errorMessageRepository = errorMessageRepository;
        this.asyncCleanerService=asyncCleanerService;
    }

    @Cacheable(value = "cleaners", key = "#id")
    @Override
    public Optional<Cleaner> findById(Long id) {
        return cleanerRepository.findById(id);
    }
    @Override
    public Cleaner addCleaner(String name, String userMail) {
        //Vezujemo usisivac za korisnika tako sto usisivacu prosledimo email korisnika
        return cleanerRepository.save(new Cleaner(0L,name,true,Status.STOPPED,LocalDate.now(),userRepository.findByEmail(userMail),false,0));
    }

    @Transactional //Ili se izvrsi ili rollbackuje ako padne
    public Collection<ErrorMessage> findAllErrorsForUser(String userMail){

        return errorMessageRepository.findAllByCleaner_User(userRepository.findByEmail(userMail));
    }

    @Cacheable(value = "cleanersByUser", key = "#email")
    @Override
    @Transactional
    public Collection<Cleaner> getCleanersByUser(String email) {
        return cleanerRepository.findAllByUser(userRepository.findByEmail(email));
    }
    @Cacheable(value = "searchCleaners", key = "#name + #statuses + #dateFrom + #dateTo + #userMail")
    @Override
    @Transactional
    public Collection<Cleaner> searchCleaners(String name, List<String> statuses, LocalDate dateFrom, LocalDate dateTo, String userMail) {
        List<Cleaner> cleanersByUser=(ArrayList<Cleaner>) getCleanersByUser(userMail);
        List<Cleaner>returnCleaners=new ArrayList<>();


        System.out.println(statuses);
        for (Cleaner cleaner:cleanersByUser){

            if(doesCleanerContainsName(cleaner,name)){
                System.out.println("Sadrzi ime");

                if(doesCleanerContainsStatus(cleaner,statuses)){
                    System.out.println("Sadrzi statuse");

                        if(isCleanerBetweenDates(cleaner,dateFrom,dateTo)){
                            System.out.println("Izmedju datuma je");
                            returnCleaners.add(cleaner);
                    }
                }
            }
        }
        return returnCleaners;
    }

    //Moze se izvrsiti samo nad STOPPED usisivacima.Ne brise ga iz baze samo ga markuje kao neaktivnog


    @Caching(evict = {
            @CacheEvict(value = "cleaners", allEntries = true),
            @CacheEvict(value = "cleanersByUser", allEntries = true),
            @CacheEvict(value = "searchCleaners", allEntries = true)
    })
    @Override
    @Transactional
    public ResponseEntity<Map<String,String>> removeCleaner(Long id) {
        Optional<Cleaner> nullableCleaner=this.findById(id);
            Map<String, String> jsonResponse = new HashMap<>();
        if(nullableCleaner.isPresent()){
            Cleaner cleaner=nullableCleaner.get();
            if(cleaner.getStatus()!=Status.STOPPED){
                jsonResponse.put("message","Usisivac mora biti STOPPED pre brisanja iz sistema...");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(jsonResponse);
            }
            cleaner.setActive(false);
            cleanerRepository.save(cleaner);

            jsonResponse.put("message","Usisivac je uklonjen iz sistema rada...");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonResponse);
        }else{
            jsonResponse.put("message","Ne postoji usisivac sa tim id-jem...");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }
    }


    @Override
    public ResponseEntity<Map<String,String>> callStartCleaner(Long id, boolean scheduled) throws InterruptedException {
        Optional<Cleaner> optionalCleaner = cleanerRepository.findById(id);
        if (optionalCleaner.isPresent()) {
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message","Poruka sa bekenda:Usisivac se startuje...");

            //Asinhronost ne funkcionise unutar iste klase,zato sam metode startovanja/stop/discharge prebacio
            //u drugu klasu
            asyncCleanerService.startCleaner(id, scheduled);


            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonResponse);
        } else {
            throw new InterruptedException("Usisivac Ne postoji");
        }
    }


    //todo Nakon svaka tri perioda rada usisivača, tj. nakon svaka tri ciklusa RUNNING-STOPPED,
    // potrebno je da se usisivač isprazni, tj. da se, i bez da korisnik zada komandu,
    // izvrši operacija DISCHARGE (ovo je potrebno obezbediti i za usisivače čiji korisnici nemaju DISCHARGE permisiju).
    //Ovo stavi u stopped funkciji.Stavi neki counter u cleaner klasi,i svaki put kada se stoppira ususivac povecaj taj counter



    @Override

    public ResponseEntity<Map<String,String>> callStopCleaner(Long id, boolean scheduled) throws InterruptedException {
        Optional<Cleaner> optionalCleaner = cleanerRepository.findById(id);
        if (optionalCleaner.isPresent()) {
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message","Poruka sa bekenda:Usisivac se stopira...");

            //Asinhronost ne funkcionise unutar iste klase,zato sam metode startovanja/stop/discharge prebacio
            //u drugu klasu
            asyncCleanerService.stopCleaner(id, scheduled);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonResponse);
        } else {
            throw new InterruptedException("Usisivac ne postoji");
        }
    }

    @Override
    public ResponseEntity<Map<String,String>> callDischargeCleaner(Long id, boolean scheduled) throws InterruptedException {
        Optional<Cleaner> optionalCleaner = cleanerRepository.findById(id);
        if (optionalCleaner.isPresent()) {
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message","Poruka sa bekenda:Usisivac se prazni...");

            //Asinhronost ne funkcionise unutar iste klase,zato sam metode startovanja/stop/discharge prebacio
            //u drugu klasu
            asyncCleanerService.dischargeCleaner(id, scheduled);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonResponse);
        } else {
            throw new InterruptedException("Usisivac ne postoji");
        }
    }

    @Override
    public ResponseEntity<Map<String,String>> scheduleCleaner(Long id, String date, String time, String action) throws ParseException {
        Date date1= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date+" "+time);

        this.taskScheduler.schedule(() -> {

            try {
                switch (action.toLowerCase()) {
                    case "start":
                        asyncCleanerService.startCleaner(id, true);
                        break;
                    case "stop":
                        asyncCleanerService.stopCleaner(id, true);
                        break;
                    case "discharge":
                        asyncCleanerService.dischargeCleaner(id, true);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, date1);

        return null;

    }



    private boolean isCleanerBetweenDates(Cleaner cleaner, LocalDate dateFrom,LocalDate dateTo){

        if(dateTo==null && dateFrom==null){
            return true;
        }

        boolean flag=false;
        if(dateTo!=null){
            if(cleaner.getCreationDate().isBefore(dateTo) || cleaner.getCreationDate().isEqual(dateTo)){
                flag=true;
            }
        }
        if(dateFrom!=null){
            if(cleaner.getCreationDate().isAfter(dateFrom) || cleaner.getCreationDate().isEqual(dateFrom)){
                flag=true;
            }else{
                flag=false;
            }

        }
        return flag;

    }

    private boolean doesCleanerContainsStatus(Cleaner cleaner,List<String>statuses){

        if(statuses==null || statuses.isEmpty())
            return true;

        if(statuses.contains(cleaner.getStatus().toString()))
            return true;

        return false;
    }
    private boolean doesCleanerContainsName(Cleaner cleaner,String name){
        if(name!=null && cleaner.getName().toLowerCase().contains(name.toLowerCase()))
            return true;

        if(name==null)
            return true;

        return false;

    }


}
