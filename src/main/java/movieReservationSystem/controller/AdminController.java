package movieReservationSystem.controller;

import movieReservationSystem.dto.AdminDTO;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final AdminService adminService;

    @Autowired
    public AdminController(JdbcUserDetailsManager userDetailsManager, AdminService adminService) {
        this.userDetailsManager = userDetailsManager;
        this.adminService = adminService;
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

        SecurityContextHolder.getContext().setAuthentication(null);
        adminService.addNewAdmin(adminDTO.getUserName(), adminDTO.getEmail());

        return adminDTO.getUserName() + " successfully registered";
    }

    @PostMapping("/addNewMovie")
    public String addNewMovie(@RequestBody MovieDTO movieDTO) {
        adminService.addNewMovie(movieDTO.getTitle(), movieDTO.getDescription(),
                movieDTO.getShowTime(), movieDTO.getGenre(), movieDTO.getCapacity(), movieDTO.getAvailableSeats());

        return movieDTO.getTitle() + " added successfully";
    }

    @PutMapping("/updateMovie/{movieId}")
    public String updateMovie(@PathVariable int movieId, @RequestBody MovieDTO newMovie) {

        return adminService.editMovie(movieId, newMovie);
    }


}
