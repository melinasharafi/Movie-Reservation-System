package movieReservationSystem.controller;

import movieReservationSystem.dto.ReservationDTO;
import movieReservationSystem.dto.request.UserRequestDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserController(JdbcUserDetailsManager userDetailsManager, UserService userService,
                          BCryptPasswordEncoder encoder) {
        this.userDetailsManager = userDetailsManager;
        this.userService = userService;
        this.encoder = encoder;
    }


    @PostMapping("/register")
    public String register(@RequestBody UserRequestDTO userRequestDTO) {

        if (userDetailsManager.userExists(userRequestDTO.getUserName())) {
            return userRequestDTO.getUserName() + " already exist";
        }

        UserDetails newUser = User.builder()
                .username(userRequestDTO.getUserName())
                .password(encoder.encode(userRequestDTO.getPassword()))
                .roles("USER")
                .build();

        userDetailsManager.createUser(newUser);

        SecurityContextHolder.getContext().setAuthentication(null);
        userService.addNewUser(userRequestDTO.getUserName(), userRequestDTO.getEmail());

        return userRequestDTO.getUserName() + " successfully registered";
    }

    @PostMapping("/{userId}/reserve")
    public String reserveMovie(@PathVariable int userId, @RequestBody ReservationDTO reservationInfo) {

        return userService.reserveMovie(userId, reservationInfo);
    }

    @DeleteMapping("/{userId}/reservation/cancel")
    public String cancelReservation(@PathVariable int userId, @RequestBody ReservationDTO reservation) {
        return userService.cancelReservation(userId, reservation);
    }

    @GetMapping("/{userId}/reservation")
    public List<Movie> userReservations(@PathVariable int userId) {
        return userService.userReservations(userId);
    }
}
