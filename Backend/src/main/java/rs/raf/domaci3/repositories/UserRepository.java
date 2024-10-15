package rs.raf.domaci3.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.domaci3.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByEmail(String email);


}
