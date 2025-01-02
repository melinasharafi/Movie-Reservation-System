package movieReservationSystem.service;

import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private AdminService adminService;
    private UserInformationDAO adminDao;
    private MovieDAO movieDAO;
    private MovieDTO firstMovieDTO;
    private MovieDTO secondMovieDTO;
    private MovieDTO thirdMovieDTO;
    private MovieDTO forthMovieDTO;

    @BeforeEach
    public void setUp() {

        // Mock the DAO
        movieDAO = mock(MovieDAO.class);
        adminDao = mock(UserInformationDAO.class);

        // Inject the mock into AdminService
        adminService = new AdminService(adminDao, movieDAO);

        firstMovieDTO = new MovieDTO("Inception", "A mind-bending thriller",
                Timestamp.valueOf("2025-01-01 12:34:56.789"), "Sci-Fi", 100, 100);

        secondMovieDTO = new MovieDTO(null, "A mind-bending thriller",
                Timestamp.valueOf("2025-01-01 12:34:56.789"), "Sci-Fi", 100, 100);

        thirdMovieDTO = new MovieDTO("Inception", "A mind-bending thriller",
                null, "Sci-Fi", 100, 100);

        forthMovieDTO = new MovieDTO("Inception", "A mind-bending thriller",
                Timestamp.valueOf("2025-01-01 12:34:56.789"), "Sci-Fi", 100, 120);
    }


    @Test
    public void addNewMovieTest() {

        // Mock behavior for the DAO
        when(movieDAO.findByTitle("Inception")).thenReturn(null); // Simulate movie not existing
        assertEquals("Inception added successfully", adminService.addNewMovie(firstMovieDTO));

        when(movieDAO.findByTitle("Inception")).thenReturn(new Movie()); // Simulate movie already existing
        assertEquals("Inception already exists", adminService.addNewMovie(firstMovieDTO));

        // Test validation errors
        try {
            adminService.addNewMovie(secondMovieDTO); // Title is null
        } catch (IllegalArgumentException ex) {
            assertEquals("Movie title can not be null", ex.getMessage());
        }

        try {
            adminService.addNewMovie(thirdMovieDTO); // Show time is null
        } catch (IllegalArgumentException ex) {
            assertEquals("Show time can not be null", ex.getMessage());
        }

        try {
            adminService.addNewMovie(forthMovieDTO); // AvailableSeats > Capacity
        } catch (IllegalArgumentException ex) {
            assertEquals("AvailableSeats must be less than capacity", ex.getMessage());
        }
    }


    @Test
    public void editMovieTest() {

        try {
            adminService.editMovie(-1, secondMovieDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Id must be greater than 1", e.getMessage());
        }

        try {
            adminService.editMovie(0, secondMovieDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Id must be greater than 1", e.getMessage());
        }

        try {
            adminService.editMovie(2, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Id must be greater than 1", e.getMessage());
        }

        when(movieDAO.findById(2)).thenReturn(null);
        try {
            adminService.editMovie(2, secondMovieDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Movie by id 2 not found", e.getMessage());
        }

        when(movieDAO.findById(2)).thenReturn(new Movie());
        assertEquals(secondMovieDTO.getTitle() + "updated successfully",
                adminService.editMovie(2, secondMovieDTO));


    }

}
