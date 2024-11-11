package movieReservationSystem.service;

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

    // add new user
    public void addNewUser(String username, String email) {
        UserInformation user = new UserInformation(username, email);
        userDao.save(user);
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
