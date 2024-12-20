package movieReservationSystem.controller;

import movieReservationSystem.dto.UserDTO;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final AdminService adminService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AdminController(JdbcUserDetailsManager userDetailsManager, AdminService adminService,
                           BCryptPasswordEncoder encoder) {
        this.userDetailsManager = userDetailsManager;
        this.adminService = adminService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDTO adminDTO) {

        if (userDetailsManager.userExists(adminDTO.getUserName())) {
            return adminDTO.getUserName() + " already exist";
        }

        UserDetails newAdmin = User.builder()
                .username(adminDTO.getUserName())
                .password(encoder.encode(adminDTO.getPassword()))
                .roles("ADMIN")
                .build();

        userDetailsManager.createUser(newAdmin);

        SecurityContextHolder.getContext().setAuthentication(null);
        adminService.addNewAdmin(adminDTO.getUserName(), adminDTO.getEmail());

        return adminDTO.getUserName() + " successfully registered";
    }

    @PostMapping("/movie")
    public String addNewMovie(@RequestBody MovieDTO newMovie) {

        return adminService.addNewMovie(newMovie);
    }

    @PutMapping("/movie/{movieId}")
    public String updateMovie(@PathVariable int movieId, @RequestBody MovieDTO updatedMovie) {

        return adminService.editMovie(movieId, updatedMovie);
    }

    @DeleteMapping("/movie/{movieId}")
    public String deleteMovie(@PathVariable int movieId) {

        return adminService.deleteMovie(movieId);
    }

    @GetMapping("/movie")
    public List<Movie> listOfAllMovie() {
        return adminService.listOfAllMovie();
    }

    @GetMapping("/movie/{movieId}")
    public Movie getMovie(@PathVariable int movieId) {

        return adminService.getMovie(movieId);
    }


}
