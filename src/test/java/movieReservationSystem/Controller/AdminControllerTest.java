package movieReservationSystem.Controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import movieReservationSystem.controller.AdminController;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.dto.UserDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.service.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AdminController.class)
@Import(movieReservationSystem.config.SecurityConfig.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JdbcUserDetailsManager userDetailsManager;
    @MockBean
    private AdminService adminService;
    @MockBean
    private BCryptPasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;


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


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listOfAllMoviesTest() throws Exception {

        // Test for null list
        when(adminService.listOfAllMovie()).thenThrow(new RuntimeException("No movie found"));

        this.mvc.perform(get("/admin/movie"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Check content type
                .andExpect(jsonPath("$.message", is("Error retrieving movies: No movie found")));


        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("title1", "description1", null, "genre1",
                30, 20));
        movies.add(new Movie("title2", "description2", null, "genre2",
                30, 20));

        Mockito.doReturn(movies).when(adminService).listOfAllMovie();

        this.mvc.perform(get("/admin/movie"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("title1")))
                .andExpect(jsonPath("$[0].description", is("description1")))
                .andExpect(jsonPath("$[0].genre", is("genre1")))
                .andExpect(jsonPath("$[0].capacity", is(30)))
                .andExpect(jsonPath("$[0].availableSeats", is(20)))

                .andExpect(jsonPath("$[1].title", is("title2")))
                .andExpect(jsonPath("$[1].description", is("description2")))
                .andExpect(jsonPath("$[1].genre", is("genre2")))
                .andExpect(jsonPath("$[1].capacity", is(30)))
                .andExpect(jsonPath("$[1].availableSeats", is(20)));
    }


    @Test
    public void registerTest() throws Exception {

        UserDTO admin = new UserDTO("melina", "Mm@1234567", "melinaSharafi@gmail.com");

        when(userDetailsManager.userExists(admin.getUserName())).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders.post("/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("" +
                        "{\"message\": \"melina already exists\"}"));


        when(userDetailsManager.userExists(admin.getUserName())).thenReturn(false);
        when(encoder.encode(admin.getPassword())).thenReturn("hashedPassword");
        mvc.perform(MockMvcRequestBuilders.post("/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("melina successfully registered"));


    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addNewMovieTest() throws Exception {

        MovieDTO movie = new MovieDTO("title", "description", Timestamp.valueOf("2025-02-10 18:30:00"),
                "genre", 100, 20);

        // using Mockito.any(MovieDTO.class) to handle deserialization issues
        Mockito.when(adminService.addNewMovie(Mockito.any(MovieDTO.class))).thenReturn("title added successfully");
        mvc.perform(MockMvcRequestBuilders.post("/admin/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("title added successfully"));

        Mockito.when(adminService.addNewMovie(Mockito.any(MovieDTO.class))).thenThrow(new EntityExistsException(movie.getTitle() + " already exists"));
        mvc.perform(MockMvcRequestBuilders.post("/admin/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\": \"title already exists\"}"));


    }

}


