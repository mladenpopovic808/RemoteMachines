package rs.raf.domaci3.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.raf.domaci3.model.Cleaner;
import rs.raf.domaci3.model.ErrorMessage;
import rs.raf.domaci3.model.status.Status;
import rs.raf.domaci3.repositories.CleanerRepository;
import rs.raf.domaci3.repositories.ErrorMessageRepository;
import rs.raf.domaci3.repositories.UserRepository;
import rs.raf.domaci3.services.AsyncCleanerService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AsyncCleanerServiceTests {


    @Mock
    private CleanerRepository cleanerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ErrorMessageRepository errorMessageRepository;

    @InjectMocks
    private AsyncCleanerService asyncCleanerService;

    private Cleaner cleaner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cleaner = new Cleaner();
        cleaner.setId(1L);
        cleaner.setActive(true);
        cleaner.setStatus(Status.STOPPED);
        cleaner.setBlocked(false);
        cleaner.setCycleCounter(0);
    }

    @Test
    public void testStartCleaner_WhenCleanerIsStopped() throws InterruptedException {
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.startCleaner(1L, false);

        verify(cleanerRepository, times(2)).save(any(Cleaner.class));
        verify(errorMessageRepository, never()).save(any(ErrorMessage.class));
        assertEquals(cleaner.getStatus(), Status.RUNNING);
    }

    @Test
    public void testStopCleaner_WhenCleanerIsRunning() throws InterruptedException {
        cleaner.setStatus(Status.RUNNING);
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.stopCleaner(1L, false);

        verify(cleanerRepository, times(2)).save(any(Cleaner.class));
        verify(errorMessageRepository, never()).save(any(ErrorMessage.class));
        assertEquals(cleaner.getStatus(), Status.STOPPED);

        assertEquals(cleaner.getCycleCounter() ,1);
    }
    @Test
    public void testStopCleaner_WhenCleanerCycleCounterIsThree() throws InterruptedException {
        cleaner.setStatus(Status.RUNNING);
        cleaner.setCycleCounter(2); // Postavljamo brojač na 2 da bismo proverili da li se povećava na 3
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.stopCleaner(1L, false);

        // Proveri da li je metoda dischargeCleaner pozvana
        verify(cleanerRepository, times(5)).save(any(Cleaner.class)); //2 save-a u stop metodi, i 3 u discharge kada udje u if
        verify(errorMessageRepository, never()).save(any(ErrorMessage.class));
        assertEquals(cleaner.getCycleCounter(), 3); // Proveri da je brojač ciklusa 3
    }

    @Test
    public void testStopCleaner_WhenCleanerIsBlocked() throws InterruptedException {
        cleaner.setBlocked(true); // Postavljamo cleaner na blokiranog
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.stopCleaner(1L, false);

        // Proveri da nije pozvana metoda save
        verify(cleanerRepository, never()).save(any(Cleaner.class));
        verify(errorMessageRepository, never()).save(any(ErrorMessage.class));
    }

    @Test
    public void testStopCleaner_WhenCleanerIsNotActiveAndScheduled() throws InterruptedException {
        cleaner.setActive(false);
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.stopCleaner(1L, true);

        // Proveri da nije pozvana metoda save za cleaner
        verify(cleanerRepository, never()).save(any(Cleaner.class));
        // Proveri da je ErrorMessage sačuvan
        verify(errorMessageRepository, times(1)).save(any(ErrorMessage.class));
    }


    @Test
    public void testDischargeCleaner_WhenCleanerIsStopped() throws InterruptedException {
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.dischargeCleaner(1L, false);

        verify(cleanerRepository, times(3)).save(any(Cleaner.class));
        assertEquals(cleaner.getStatus(), Status.STOPPED);
    }

    @Test
    public void testStartCleaner_WhenCleanerIsBlocked() throws InterruptedException {
        cleaner.setBlocked(true);
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.startCleaner(1L, false);

        verify(cleanerRepository, never()).save(any(Cleaner.class));
        verify(errorMessageRepository, never()).save(any(ErrorMessage.class));
    }
    @Test
    public void testStartCleaner_WhenCleanerNotStoppedAndScheduled() throws InterruptedException {
        // Postavi stanje usisivača
        cleaner.setStatus(Status.RUNNING); // Cleaner nije zaustavljen
        cleaner.setActive(true); // Cleaner je aktiviran
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.startCleaner(1L, true);

        // Proveri da je errorMessageRepository.save pozvana tačno jednom
        verify(errorMessageRepository, times(1)).save(any(ErrorMessage.class));
        // Proveri da cleanerRepository.save nije pozvana
        verify(cleanerRepository, times(0)).save(any(Cleaner.class));
    }
    @Test
    public void testStartCleaner_WhenCleanerNotActivatedAndScheduled() throws InterruptedException {
        // Postavi stanje usisivača
        cleaner.setActive(false); // Cleaner nije aktiviran
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.startCleaner(1L, true);

        // Proveri da je errorMessageRepository.save pozvana tačno jednom
        verify(errorMessageRepository, times(1)).save(any(ErrorMessage.class));
        // Proveri da cleanerRepository.save nije pozvana
        verify(cleanerRepository, times(0)).save(any(Cleaner.class));
    }



    @Test
    public void testStopCleaner_WhenCleanerIsNotActive() throws InterruptedException {
        cleaner.setActive(false);
        when(cleanerRepository.findById(1L)).thenReturn(Optional.of(cleaner));

        asyncCleanerService.stopCleaner(1L, true);

        verify(cleanerRepository, never()).save(any(Cleaner.class));
        verify(errorMessageRepository, times(1)).save(any(ErrorMessage.class));
    }

}
