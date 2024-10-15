package rs.raf.domaci3.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.raf.domaci3.filters.JwtFilter;
import rs.raf.domaci3.services.UserDetailService;


@EnableWebSecurity
@EnableAsync
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailService userDetailService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SpringSecurityConfig(UserDetailService userDetailService, JwtFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //Ovime smo rekli da ce sve nase putanje da imaju jwt proveru.Ne moras nigde posebno u controllerima da
        //stavljas anotacije,automatski ce se na osnovu ovoga primeniti na sve putanje.
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/users/login").permitAll()
                .antMatchers("/swagger-ui/**").permitAll() // omogućava pristup Swagger UI
                .antMatchers("/v3/**").permitAll() // omogućava pristup Swagger dokumenata
                .antMatchers("/api/users/add/**").hasAuthority("can_create_users")
                .antMatchers("/api/users/get/**").hasAuthority("can_read_users")
                .antMatchers("/api/users/update/**").hasAuthority("can_update_users")
                .antMatchers("/api/users/delete/**").hasAuthority("can_delete_users")
                .antMatchers("api/roles/all").permitAll()
                .antMatchers("/api/cleaners/allByUser/**").hasAuthority("can_search_cleaners")
                .antMatchers("/api/cleaners/errorsByUser/**").hasAuthority("can_search_cleaners")
                .antMatchers("/api/cleaners/start/**").hasAuthority("can_start_cleaners")
                .antMatchers("/api/cleaners/stop/**").hasAuthority("can_stop_cleaners")
                .antMatchers("/api/cleaners/discharge/**").hasAuthority("can_discharge_cleaners")
                .antMatchers("/api/cleaners/add/**").hasAuthority("can_add_cleaners")
                .antMatchers("/api/cleaners/remove/**").hasAuthority("can_remove_cleaners")
                .antMatchers("/api/cleaners/schedule/**").hasAuthority("can_schedule_cleaners")

                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //encoder za lozinke.
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5); // Set the desired pool size
        return taskScheduler;
    }

//    @Bean(name = "yourAsyncExecutor")
//    public AsyncTaskExecutor yourAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("yourAsyncExecutor-");
//        executor.initialize();
//        return executor;
//    }



}
