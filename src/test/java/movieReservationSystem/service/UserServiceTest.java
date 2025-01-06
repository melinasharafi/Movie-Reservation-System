package movieReservationSystem.service;

import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.ReservationDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    private UserService userService;
    private UserInformationDAO userDAO;
    private MovieDAO movieDAO;
    private ReservationDAO reservationDAO;

    @BeforeEach
    public void setUp() {
        movieDAO = mock(MovieDAO.class);
        userDAO = mock(UserInformationDAO.class);
        reservationDAO = mock(ReservationDAO.class);

        userService = new UserService(userDAO, movieDAO, reservationDAO);
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

//        // Test for adding successfully
        when(userDAO.findByUserName("melinaSharafi")).thenReturn(null);
        String result = userService.addNewUser("melinaSharafi", "melinasharafimiab@gmail.com");
        assertEquals("User added successfully", result);
    }

}
