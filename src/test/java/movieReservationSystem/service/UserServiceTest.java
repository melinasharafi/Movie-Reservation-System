package movieReservationSystem.service;

import movieReservationSystem.dto.ReservationDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.model.Reservation;
import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.ReservationDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    private UserService userService;
    private UserInformationDAO userDAO;
    private MovieDAO movieDAO;
    private ReservationDAO reservationDAO;
    private ReservationDTO movie;

    @BeforeEach
    public void setUp() {
        movieDAO = mock(MovieDAO.class);
        userDAO = mock(UserInformationDAO.class);
        reservationDAO = mock(ReservationDAO.class);

        userService = new UserService(userDAO, movieDAO, reservationDAO);

        movie = new ReservationDTO();
        movie.setMovieName("Inception");
    }

    @Test
    public void addNewUserTest() {
        // Test for null input
        try {
            userService.addNewUser(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Enter username", e.getMessage());
        }

        // Test for invalid input
        try {
            userService.addNewUser("ml", "melinasharafi@gmail.com");
        } catch (IllegalArgumentException e) {
            assertEquals("Username must contain at least 3 characters", e.getMessage());
        }

        try {
            userService.addNewUser("12meli", "melinasharafi@gmail.com");
        } catch (IllegalArgumentException e) {
            assertEquals("Username must start with character", e.getMessage());
        }

        try {
            userService.addNewUser("m1234", "melinasharafi@gmail.com");
        } catch (IllegalArgumentException e) {
            assertEquals("Username must contain at least 3 characters", e.getMessage());
        }

        try {
            userService.addNewUser("melina12", "melinasharafi@gmail.com");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid email address", e.getMessage());
        }

        // Test for adding duplicated user
        when(userDAO.findByUserName("melinaSharafi")).thenReturn(new UserInformation());
        try {
            userService.addNewUser("melinaSharafi", "melinasharafi@gmail.com");
        } catch (IllegalArgumentException e) {
            assertEquals("User already exists", e.getMessage());
        }

        // Test for adding successfully
        when(userDAO.findByUserName("melinaSharafi")).thenReturn(null);
        String result = userService.addNewUser("melinaSharafi", "melinasharafimiab@gmail.com");
        assertEquals("User added successfully", result);
    }


    @Test
    public void reserveMovieTest() {


        // Test for not existing movie
        when(movieDAO.findByTitle(movie.getMovieName())).thenReturn(null);
        when(userDAO.findById(1)).thenReturn(new UserInformation());
        try {
            userService.reserveMovie(1, movie);
        } catch (NoSuchElementException e) {
            assertEquals(movie.getMovieName() + " doesn't exit", e.getMessage());
        }


        // Test for no available seat
        Movie movie = new Movie();
        when(movieDAO.findByTitle(this.movie.getMovieName())).thenReturn(movie);
        movie.setAvailableSeats(0);
        try {
            userService.reserveMovie(1, this.movie);
        } catch (IllegalArgumentException e) {
            assertEquals("No available seat for " + movie.getTitle(), e.getMessage());
        }


        // Test for successful approach
        when(movieDAO.findByTitle(this.movie.getMovieName())).thenReturn(movie);
        movie.setCapacity(20);
        movie.setAvailableSeats(4);
        assertEquals("Your reservation was successful", userService.reserveMovie(1, this.movie));
    }

    @Test
    public void cancelReservationTest() {

        // Test for not existing user
        when(userDAO.findById(2)).thenReturn(null);
        try {
            userService.cancelReservation(2, movie);
        } catch (NoSuchElementException e) {
            assertEquals("No such user found", e.getMessage());
        }

        // Test for not existing movie
        when(movieDAO.findByTitle(movie.getMovieName())).thenReturn(null);
        when(userDAO.findById(2)).thenReturn(new UserInformation());
        try {
            userService.cancelReservation(2, movie);
        } catch (NoSuchElementException e) {
            assertEquals("No such movie found", e.getMessage());
        }


        // Test for not existing reservation
        when(userDAO.findById(2)).thenReturn(new UserInformation());
        when(movieDAO.findByTitle(movie.getMovieName())).thenReturn(new Movie());
        when(reservationDAO.findByUserIdAndMovieId(2, 2)).thenReturn(null);
        try {
            userService.cancelReservation(2, movie);
        } catch (NoSuchElementException e) {
            assertEquals("No such reservation found", e.getMessage());
        }


        // Test for successful approach
        Movie existingMovie = movieDAO.findByTitle(movie.getMovieName());
        existingMovie.setAvailableSeats(100);
        UserInformation user = new UserInformation();
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setMovie(existingMovie);
        when(reservationDAO.findByUserIdAndMovieId(user.getId(), existingMovie.getId())).thenReturn(reservation);
        assertEquals("Reservation deleted successfully", userService.cancelReservation(2, movie));
    }

}
