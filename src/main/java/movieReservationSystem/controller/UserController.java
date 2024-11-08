package movieReservationSystem.controller;

import movieReservationSystem.dto.ReservationDTO;
import movieReservationSystem.dto.UserDTO;
import movieReservationSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final UserService userService;

    @Autowired
    public UserController(JdbcUserDetailsManager userDetailsManager, UserService userService) {
        this.userDetailsManager = userDetailsManager;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public String register(@RequestBody UserDTO userDTO) {

        if (userDetailsManager.userExists(userDTO.getUserName())) {
            return userDTO.getUserName() + " already exist";
        }

        UserDetails newUser = User.builder()
                .username(userDTO.getUserName())
                .password("{noop}" + userDTO.getPassword())
                .roles("USER")
                .build();

        userDetailsManager.createUser(newUser);

        SecurityContextHolder.getContext().setAuthentication(null);
        userService.addNewUser(userDTO.getUserName(), userDTO.getEmail());

        return userDTO.getUserName() + " successfully registered";
    }

    @PostMapping("/reservation/{userId}")
    public String reserveMovie(@PathVariable int userId, @RequestBody ReservationDTO reservationInfo) {

        return userService.reserveMovie(userId, reservationInfo);
    }
}
