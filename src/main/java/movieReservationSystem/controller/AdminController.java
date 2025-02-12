package movieReservationSystem.controller;

import jakarta.persistence.EntityExistsException;
import movieReservationSystem.dto.UserDTO;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final AdminService adminService;
    private final BCryptPasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @Autowired
    public AdminController(JdbcUserDetailsManager userDetailsManager, AdminService adminService,
                           BCryptPasswordEncoder encoder) {
        this.userDetailsManager = userDetailsManager;
        this.adminService = adminService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO adminDTO) {

        if (userDetailsManager.userExists(adminDTO.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"" + adminDTO.getUserName() + " already exists\"}");
        }

        UserDetails newAdmin = User.builder()
                .username(adminDTO.getUserName())
                .password(encoder.encode(adminDTO.getPassword()))
                .roles("ADMIN")
                .build();

        userDetailsManager.createUser(newAdmin);

        SecurityContextHolder.getContext().setAuthentication(null);
        adminService.addNewAdmin(adminDTO.getUserName(), adminDTO.getEmail());

        return ResponseEntity.ok(adminDTO.getUserName() + " successfully registered");

    }

    @PostMapping("/movie")
    public ResponseEntity<String> addNewMovie(@RequestBody MovieDTO newMovie) {
        try {
            return ResponseEntity.ok(adminService.addNewMovie(newMovie));
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


    @PutMapping("/movie/{movieId}")
    public ResponseEntity<String> updateMovie(@PathVariable int movieId, @RequestBody(required = false) MovieDTO updatedMovie) {

        try {
            return ResponseEntity.ok(adminService.editMovie(movieId, updatedMovie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


    @DeleteMapping("/movie/{movieId}")
    public String deleteMovie(@PathVariable int movieId) {

        return adminService.deleteMovie(movieId);
    }

    @GetMapping("/movie")
    public ResponseEntity<?> listOfAllMovie() {
        try {
            List<Movie> movies = adminService.listOfAllMovie();
            return ResponseEntity.ok(movies);
        } catch (RuntimeException e) {
            logger.error("Error retrieving list of movies", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error retrieving movies: " + e.getMessage()));
        }
    }


    @GetMapping("/movie/{movieId}")
    public Movie getMovie(@PathVariable int movieId) {

        return adminService.getMovie(movieId);
    }

}


class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
