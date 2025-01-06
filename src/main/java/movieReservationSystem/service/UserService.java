package movieReservationSystem.service;

import jakarta.persistence.PostUpdate;
import movieReservationSystem.dto.ReservationDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.model.Reservation;
import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.ReservationDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class UserService {

    private UserInformationDAO userDao;
    private MovieDAO movieDAO;
    private ReservationDAO reservationDAO;

    @Autowired
    public UserService(UserInformationDAO userDao, MovieDAO movieDAO, ReservationDAO reservationDAO) {
        this.userDao = userDao;
        this.movieDAO = movieDAO;
        this.reservationDAO = reservationDAO;
    }


    // To verify that a string contains more than three non-digit characters
    public boolean validUserName(String userName) {

        int nonDigitChar = 0;

        for (char c : userName.toCharArray()) {
            if (!Character.isDigit(c)) {
                nonDigitChar++;
            }
        }

        return nonDigitChar >= 3;
    }

    // add new user
    public String addNewUser(String username, String email) {

        if (username == null) {
            throw new IllegalArgumentException("Enter username");
        } else if (username.length() <= 3) {
            throw new IllegalArgumentException("Username must contain at least 3 characters");
        } else if (Character.isDigit(username.charAt(0))) {
            throw new IllegalArgumentException("Username must start with character");
        } else if (!validUserName(username)) {
            throw new IllegalArgumentException("Username must contain at least 3 characters");
        } else if (email != null && (!email.contains("@gmail.com") && !email.contains("@yahoo.com"))) {
            throw new IllegalArgumentException("Invalid email address");
        } else {

            UserInformation user = new UserInformation(username, email);

            if (userDao.findByUserName(username) != null) {
                throw new IllegalArgumentException("User already exists");
            }

            userDao.save(user);
            return "User added successfully";
        }

    }

    // reserve new movie
    public String reserveMovie(int id, ReservationDTO reservationInfo) {

        Movie movie = movieDAO.findByTitle(reservationInfo.getMovieName());

        // check the movie exits or not
        if (movie == null) {
            return reservationInfo.getMovieName() + " doesn't exit";
        }

        // check for movie's capacity
        if (movie.getAvailableSeats() == 0) {
            return "The capacity of " + reservationInfo.getMovieName() + " has been completed";
        }

        int seatNumber = movie.getCapacity() - movie.getAvailableSeats() + 1;
        Reservation reservation = new Reservation(userDao.findById(id), movie, seatNumber);

        movie.setAvailableSeats(movie.getAvailableSeats() - 1);
        movieDAO.save(movie);

        reservationDAO.save(reservation);

        return "Your reservationInfo for " + reservationInfo.getMovieName() + " was successfully";
    }


    // cancel a reservation
    public String cancelReservation(int userId, ReservationDTO reservation) {

        UserInformation user = userDao.findById(userId);
        if (user == null) {
            return "No user found by id = " + userId;
        }

        Movie movie = movieDAO.findByTitle(reservation.getMovieName());
        if (movie == null) {
            return reservation.getMovieName() + " not found";
        }

        Reservation reservationToCancel = reservationDAO.findByUserIdAndMovieId(user.getId(), movie.getId());
        if (reservationToCancel == null) {
            return "No reservation for " + movie.getTitle() + " by " + user.getUsername();
        }

        reservationDAO.delete(reservationToCancel);

        // increase movie capacity by 1
        movie.setAvailableSeats(movie.getAvailableSeats() + 1);
        movieDAO.save(movie);

        return "Reservation deleted successfully";

    }

    // user can see their reservation
    @Transactional
    public List<Movie> userReservations(int userId) {

        List<Movie> movies = new ArrayList<>();

        List<Reservation> reservations = reservationDAO.findAllByUserId(userId);

        if (reservations != null && !reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                movies.add(reservation.getMovie());
            }
        }


        return movies;
    }
}
