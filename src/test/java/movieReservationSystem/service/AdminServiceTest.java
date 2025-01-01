package movieReservationSystem.service;

import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        when(movieDAO.findByTitle("Inception")).thenReturn(null);
        assertEquals("Inception added successfully", adminService.addNewMovie(firstMovieDTO));

        when(movieDAO.findByTitle("Inception")).thenReturn(new Movie());
        assertEquals("Inception already exists", adminService.addNewMovie(firstMovieDTO));

        assertEquals("Movie title can not be null", adminService.addNewMovie(secondMovieDTO));
        assertEquals("Show time can not be null", adminService.addNewMovie(thirdMovieDTO));
        assertEquals("AvailableSeats must be less than capacity", adminService.addNewMovie(forthMovieDTO));
    }
}
