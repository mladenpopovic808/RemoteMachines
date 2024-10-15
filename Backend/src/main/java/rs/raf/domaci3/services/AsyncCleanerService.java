package rs.raf.domaci3.services;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.raf.domaci3.model.Cleaner;
import rs.raf.domaci3.model.ErrorMessage;
import rs.raf.domaci3.model.status.Status;
import rs.raf.domaci3.repositories.CleanerRepository;
import rs.raf.domaci3.repositories.ErrorMessageRepository;
import rs.raf.domaci3.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class AsyncCleanerService {

    //S obzirom da unuttar iste klase ne radi asinhrono pozivanje metoda,morao sam da odvojim metode koje cu pozivati
    //asinhrono. S obzirom da bekend odmah mora da vrati poruku od tome da je startovanje usisivaca pocelo,to je razlog ovoga.


    private CleanerRepository cleanerRepository;
    private UserRepository userRepository;
    private TaskScheduler taskScheduler;
    private ErrorMessageRepository errorMessageRepository;

    public AsyncCleanerService(CleanerRepository cleanerRepository, UserRepository userRepository, TaskScheduler taskScheduler, ErrorMessageRepository errorMessageRepository) {
        this.cleanerRepository = cleanerRepository;
        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
        this.errorMessageRepository = errorMessageRepository;
    }

    @Async
    public void startCleaner(Long id, boolean scheduled) throws InterruptedException {
        Optional<Cleaner> optionalCleaner=cleanerRepository.findById(id);

        if(optionalCleaner.isPresent()){
            Cleaner cleaner=optionalCleaner.get();
            if(cleaner.isBlocked()==true){
                return;
            }
            if(cleaner.isActive()){
                if(cleaner.getStatus()== Status.STOPPED){
                    System.out.println("Starting cleaner...");
                    cleaner.setBlocked(true);
                    cleanerRepository.save(cleaner);
                    Thread.sleep(4000); //TODO povecati posle
                    cleaner.setStatus(Status.RUNNING);
                    cleaner.setBlocked(false);
                    cleanerRepository.save(cleaner);
                    System.out.println("Cleaner is STARTED!");
                }else if(scheduled){

                    errorMessageRepository.save(new ErrorMessage(0L,"Cleaner is not STOPPED","START", LocalDate.now(),cleaner));
                }
            }else if(scheduled){
                errorMessageRepository.save(new ErrorMessage(0L,"Cleaner is NOT Activated","START",LocalDate.now(),cleaner));
            }
        }
    }
    @Async
    public void stopCleaner(Long id, boolean scheduled) throws InterruptedException {
        Optional<Cleaner> optionalCleaner = cleanerRepository.findById(id);

        if(optionalCleaner.isPresent()){
            Cleaner cleaner=optionalCleaner.get();
            if(cleaner.isBlocked()==true){
                return;
            }
            if(cleaner.isActive()){
                if(cleaner.getStatus()==Status.RUNNING){//Samo running mogu da se iskljuce
                    System.out.println("Stopping Cleaner...");
                    cleaner.setBlocked(true);
                    cleanerRepository.save(cleaner);
                    Thread.sleep(4000); //TODO povecati posle
                    cleaner.setStatus(Status.STOPPED);
                    cleaner.setBlocked(false);
                    cleaner.setCycleCounter(cleaner.getCycleCounter()+1);
                    cleanerRepository.save(cleaner);
                    System.out.println("Cleaner is STOPPED!");
                    //Nakon svaka 3 running-stop ciklusa,treba usisivac da se isprazni
                    if(cleaner.getCycleCounter()%3==0){
                        dischargeCleaner(id,scheduled);
                    }
                }else if(scheduled){
                    errorMessageRepository.save(new ErrorMessage(0L,"Cleaner is not RUNNING","STOP",LocalDate.now(),cleaner));
                }
            }else if(scheduled){
                errorMessageRepository.save(new ErrorMessage(0L,"Cleaner is NOT Activated","STOP",LocalDate.now(),cleaner));
            }
        }
    }

    @Async
    public void dischargeCleaner(Long id, boolean scheduled) throws InterruptedException {
        Optional<Cleaner>optionalCleaner=cleanerRepository.findById(id);

        if(optionalCleaner.isPresent()){
            Cleaner cleaner=optionalCleaner.get();
            if(cleaner.isBlocked()==true){
                return;
            }
            if(cleaner.isActive()){
                if(cleaner.getStatus().equals(Status.STOPPED)){
                    System.out.println("Discharging cleaner...");
                    cleaner.setBlocked(true);
                    cleanerRepository.save(cleaner);
                    Thread.sleep(2000);
                    cleaner.setStatus(Status.DISCHARGING);
                    cleanerRepository.save(cleaner);
                    Thread.sleep(2000);
                    cleaner.setBlocked(false);
                    cleaner.setStatus(Status.STOPPED);
                    cleanerRepository.save(cleaner);
                    System.out.println("Cleaner is DISCHARGED!");
                }
            }
        }
    }

}
