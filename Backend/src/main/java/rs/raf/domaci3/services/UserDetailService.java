package rs.raf.domaci3.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailService implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Ne po username-u vec po emailu
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=this.userRepository.findByEmail(email);

        if(user==null){
            throw new UsernameNotFoundException("Ne postoji takav korisnik"); //iz springa exception
        }

        List<SimpleGrantedAuthority> authorities=new ArrayList<>();

        for(Role role:user.getRoles()){

            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return  new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);




    }
}
