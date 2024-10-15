package rs.raf.domaci3;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rs.raf.domaci3.model.Cleaner;
import rs.raf.domaci3.model.ErrorMessage;
import rs.raf.domaci3.model.status.Status;
import rs.raf.domaci3.repositories.CleanerRepository;
import rs.raf.domaci3.repositories.ErrorMessageRepository;
import rs.raf.domaci3.repositories.UserRepository;
import org.springframework.scheduling.TaskScheduler;
import rs.raf.domaci3.services.AsyncCleanerService;
import rs.raf.domaci3.services.CleanerService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CleanerServiceTests {

    @Mock
    private CleanerRepository cleanerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private ErrorMessageRepository errorMessageRepository;

    @Mock
    private AsyncCleanerService asyncCleanerService;

    @InjectMocks
    private CleanerService cleanerService;

    @Test
    public void addCleaner_Test() {
        String name = "Test Cleaner";
        String userMail = "test@mail.com";
        Cleaner savedCleaner = new Cleaner(1L, name, true, Status.STOPPED, LocalDate.now(), null, false, 0);
        when(userRepository.findByEmail(userMail)).thenReturn(null); // Mock user lookup

        when(cleanerRepository.save(any(Cleaner.class))).thenReturn(savedCleaner);

        Cleaner result = cleanerService.addCleaner(name, userMail);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(cleanerRepository).save(any(Cleaner.class));
    }

    @Test
    public void findAllErrorsForUser_Test() {
        String userMail = "test@mail.com";
        ErrorMessage errorMessage = new ErrorMessage();
        List<ErrorMessage> errorMessages = Arrays.asList(errorMessage);
        when(userRepository.findByEmail(userMail)).thenReturn(null); // Mock user lookup
        when(errorMessageRepository.findAllByCleaner_User(any())).thenReturn(errorMessages);

        Collection<ErrorMessage> result = cleanerService.findAllErrorsForUser(userMail);

        assertEquals(1, result.size());
        verify(errorMessageRepository).findAllByCleaner_User(any());
    }

    @Test
    public void getCleanersByUser_Test() {
        String email = "test@mail.com";
        Cleaner cleaner = new Cleaner();
        List<Cleaner> cleaners = Arrays.asList(cleaner);
        when(userRepository.findByEmail(email)).thenReturn(null); // Mock user lookup
        when(cleanerRepository.findAllByUser(any())).thenReturn(cleaners);

        Collection<Cleaner> result = cleanerService.getCleanersByUser(email);

        assertEquals(1, result.size());
        verify(cleanerRepository).findAllByUser(any());
    }

//    @Test
//    public void searchCleaners_Test() {
//        String name = "Cleaner";
//        List<String> statuses = Arrays.asList("STOPPED");
//        LocalDate dateFrom = LocalDate.now().minusDays(1);
//        LocalDate dateTo = LocalDate.now().plusDays(1);
//        String userMail = "test@mail.com";
//
//        Cleaner cleaner = new Cleaner(1L, name, true, Status.STOPPED, LocalDate.now(), null, false, 0);
//        when(userRepository.findByEmail(userMail)).thenReturn(null); // Mock user lookup
//        when(cleanerRepository.findAllByUser(any())).thenReturn(Collections.singletonList(cleaner));
//
//        Collection<Cleaner> result = cleanerService.searchCleaners(name, statuses, dateFrom, dateTo, userMail);
//
//        assertEquals(1, result.size());
//    }

    @Test
    public void removeCleaner_Test_CleanerExists() {
        Long cleanerId = 1L;
        Cleaner cleaner = new Cleaner(cleanerId, "Cleaner", true, Status.STOPPED, LocalDate.now(), null, false, 0);
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(cleaner));

        ResponseEntity<Map<String, String>> response = cleanerService.removeCleaner(cleanerId);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Usisivac je uklonjen iz sistema rada...", response.getBody().get("message"));
        assertFalse(cleaner.isActive());
    }

    @Test
    public void removeCleaner_Test_CleanerDoesNotExist() {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = cleanerService.removeCleaner(cleanerId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ne postoji usisivac sa tim id-jem...", response.getBody().get("message"));
    }

    @Test
    public void removeCleaner_Test_CleanerNotStopped() {
        Long cleanerId = 1L;
        Cleaner cleaner = new Cleaner(cleanerId, "Cleaner", true, Status.RUNNING, LocalDate.now(), null, false, 0);
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(cleaner));

        ResponseEntity<Map<String, String>> response = cleanerService.removeCleaner(cleanerId);

        assertEquals(406, response.getStatusCodeValue());
        assertEquals("Usisivac mora biti STOPPED pre brisanja iz sistema...", response.getBody().get("message"));
    }

    @Test
    public void callStartCleaner_Test() throws InterruptedException {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(new Cleaner()));

        ResponseEntity<Map<String, String>> response = cleanerService.callStartCleaner(cleanerId, false);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Poruka sa bekenda:Usisivac se startuje...", response.getBody().get("message"));
        verify(asyncCleanerService).startCleaner(cleanerId, false);
    }

    @Test
    public void callStartCleaner_Test_CleanerDoesNotExist() {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.empty());

        //ocekujemo da se desi ovaj exception, kada se pozove metoda navedena u argumentu
        assertThrows(InterruptedException.class, () -> cleanerService.callStartCleaner(cleanerId, false));
    }

    @Test
    public void callStopCleaner_Test() throws InterruptedException {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(new Cleaner()));

        ResponseEntity<Map<String, String>> response = cleanerService.callStopCleaner(cleanerId, false);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Poruka sa bekenda:Usisivac se stopira...", response.getBody().get("message"));
        verify(asyncCleanerService).stopCleaner(cleanerId, false);
    }

    @Test
    public void callStopCleaner_Test_CleanerDoesNotExist() {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.empty());

        assertThrows(InterruptedException.class, () -> cleanerService.callStopCleaner(cleanerId, false));
    }

    @Test
    public void callDischargeCleaner_Test() throws InterruptedException {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(new Cleaner()));

        ResponseEntity<Map<String, String>> response = cleanerService.callDischargeCleaner(cleanerId, false);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Poruka sa bekenda:Usisivac se prazni...", response.getBody().get("message"));
        verify(asyncCleanerService).dischargeCleaner(cleanerId, false);
    }

    @Test
    public void callDischargeCleaner_Test_CleanerDoesNotExist() {
        Long cleanerId = 1L;
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.empty());

        assertThrows(InterruptedException.class, () -> cleanerService.callDischargeCleaner(cleanerId, false));
    }

    @Test
    public void scheduleCleaner_Test() throws Exception {
        Long cleanerId = 1L;
        String date = LocalDate.now().toString();
        String time = "10:00";
        String action = "start";

        // Schedule the cleaner (mocking TaskScheduler)
        cleanerService.scheduleCleaner(cleanerId, date, time, action);

        verify(taskScheduler).schedule(any(), any(Date.class));
    }

    // Additional tests for private methods can be added if needed

    // Optionally, test the private methods (doesCleanerContainsName, doesCleanerContainsStatus, isCleanerBetweenDates) if you decide to refactor them to package-private
}
