package rs.raf.domaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import rs.raf.domaci3.model.ErrorMessage;
import rs.raf.domaci3.model.User;

import javax.persistence.LockModeType;
import java.util.Collection;



@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage,Long> {



    public Collection<ErrorMessage> findAllByCleaner_User(User user);//Pronadji sve errore za cleaner koju je napravio korisnik
}

