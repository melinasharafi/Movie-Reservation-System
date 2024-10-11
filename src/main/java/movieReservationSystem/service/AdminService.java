package movieReservationSystem.service;

import jakarta.persistence.EntityNotFoundException;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class AdminService {

    private UserInformationDAO adminDao;
    private MovieDAO movieDAO;


    @Autowired
    public AdminService(UserInformationDAO adminDao, MovieDAO movieDAO) {
        this.adminDao = adminDao;
        this.movieDAO = movieDAO;
    }


    // create new admin
    public void addNewAdmin(String username, String email) {
        UserInformation admin = new UserInformation(username, email);
        adminDao.save(admin);
    }


    // create new movie
    public void addNewMovie(String title, String description, Timestamp data, String genre,
                            int capacity, int availableSeats) {

        Movie movie = new Movie(title, description, data, genre, capacity, availableSeats);

        movieDAO.save(movie);
    }


    // edit a movie
    public String editMovie(int id, MovieDTO updatedMovie) {

        Movie existingMovie = movieDAO.findById(id);

        if (existingMovie == null) {
            return "Movie by id " + id + " not found";
        } else {

            if (updatedMovie.getTitle() != null) {
                existingMovie.setTitle(updatedMovie.getTitle());
            }

            if (updatedMovie.getDescription() != null) {
                existingMovie.setDescription(updatedMovie.getDescription());
            }

            if (updatedMovie.getGenre() != null) {
                existingMovie.setGenre(updatedMovie.getGenre());
            }

            if (updatedMovie.getShowTime() != null) {
                existingMovie.setShowTime(updatedMovie.getShowTime());
            }

            if (updatedMovie.getCapacity() != null) {
                existingMovie.setCapacity(updatedMovie.getCapacity());
            }

            if (updatedMovie.getAvailableSeats() != null) {
                existingMovie.setAvailableSeats(updatedMovie.getAvailableSeats());
            }

            movieDAO.save(existingMovie);

            return existingMovie.getTitle() + " updated successfully";
        }

    }
}
