package movieReservationSystem.controller;

import movieReservationSystem.dto.AdminDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final JdbcUserDetailsManager userDetailsManager;

    public AdminController(JdbcUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping("/registration")
    public String register(@RequestBody AdminDTO adminDTO) {

        if (userDetailsManager.userExists(adminDTO.getUserName())) {
            return adminDTO.getUserName() + " already exist";
        }

        UserDetails newAdmin = User.builder()
                .username(adminDTO.getUserName())
                .password("{noop}" + adminDTO.getPassword())
                .roles("ADMIN")
                .build();

        userDetailsManager.createUser(newAdmin);

        return adminDTO.getUserName() + " successfully registered";

    }
}
