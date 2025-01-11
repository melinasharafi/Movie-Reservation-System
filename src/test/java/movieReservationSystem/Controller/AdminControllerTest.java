package movieReservationSystem.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.when;

import movieReservationSystem.controller.AdminController;
import movieReservationSystem.model.Movie;
import movieReservationSystem.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class) // Ensure this is your controller's class name
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JdbcUserDetailsManager userDetailsManager;
    @MockBean
    private AdminService adminService;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getMovieTest() throws Exception {
        int movieId = 1;
        Movie mockMovie = new Movie("title", "description", null, "genre",
                30, 20);

        // Mock the service call
        when(adminService.getMovie(movieId)).thenReturn(mockMovie);

        // Perform the GET request and verify the response
        this.mvc.perform(get("/admin/movie/{movieId}", movieId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.genre").value("genre"))
                .andExpect(jsonPath("$.capacity").value(30))
                .andExpect(jsonPath("$.availableSeats").value(20));
    }
}
