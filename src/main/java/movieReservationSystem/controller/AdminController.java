package movieReservationSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import movieReservationSystem.dto.request.UserRequestDTO;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.dto.response.UserRegistrationResponseDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin API")
@RequiredArgsConstructor
public class AdminController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final AdminService adminService;
    private final BCryptPasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @PostMapping("/register")
    @Operation(summary = "Register new admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "409", description = "Admin Already exists")
    })
    public ResponseEntity<?> register(@RequestBody UserRequestDTO adminDTO) {

        try {
            return ResponseEntity.ok(adminService.addNewAdmin(adminDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(409).body(exception.getMessage());
        }
    }


    @PostMapping("/add-movie")
    @Operation(summary = "Add new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New movie successfully added"),
            @ApiResponse(responseCode = "409", description = "Movie already existed")
    })
    public ResponseEntity<?> addNewMovie(@RequestBody MovieDTO newMovie) {
        try {
            Movie savedMovie = adminService.addNewMovie(newMovie);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
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
    public ResponseEntity<String> deleteMovie(@PathVariable int movieId) {

        try {
            return ResponseEntity.ok(adminService.deleteMovie(movieId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        }

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
