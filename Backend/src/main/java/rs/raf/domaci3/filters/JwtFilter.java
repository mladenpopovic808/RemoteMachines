package rs.raf.domaci3.filters;

import io.jsonwebtoken.Jwt;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.services.UserDetailService;
import rs.raf.domaci3.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    public JwtFilter(UserDetailService userDetailService,JwtUtil jwtUtil) {
        this.userDetailService = userDetailService;
        this.jwtUtil=jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String jwt=null;
        String email=null;

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            jwt=authHeader.substring(7);
            email=jwtUtil.extractUsername(jwt);
        }
        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailService.loadUserByUsername(email);

            if(jwtUtil.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                //setovali kontext
                //BITNA PRICA ZBOG PROJEKTA: U OVAJ CONTEXT MOZES DA STAVIS STA GOD HOCES,NEKE PERMISIJE ITD,TAKO DA PO NJIMA NESTO PROVERAVAS
                //I POSLE MOZES DA PRISTUPAS TOM CONTEXTU BILO GDE U KODU (iskoristili smo u StudentRestController)
                //To zapravo sluzi da ne moramo stalno da citamo iz JWT-a,Nego prvi put kada procitamo JWT-smestimo tu i posle citamo iz koda.
                //Posle kada pozivas rute,pogledaj commandLiner klasu,na dnu imamo user1,kada se ulogujes sa tim kredencijalima se logujes i on ti vrati jwt.
            }
        }
        filterChain.doFilter(request, response);




    }
}
