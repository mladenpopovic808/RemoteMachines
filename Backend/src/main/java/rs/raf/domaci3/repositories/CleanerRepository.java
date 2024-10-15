package rs.raf.domaci3.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.Cleaner;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Collection<Cleaner> findAllByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public Optional<Cleaner> findById(Long id);
}
