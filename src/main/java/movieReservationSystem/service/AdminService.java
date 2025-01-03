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
import java.util.List;
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

    public AdminService() {
    }

    // create new admin
    public void addNewAdmin(String username, String email) {
        UserInformation admin = new UserInformation(username, email);
        adminDao.save(admin);
    }


    public class MovieValidator {

        public static void validate(MovieDTO newMovie) {
            if (newMovie.getTitle() == null) {
                throw new IllegalArgumentException("Movie title can not be null");
            }
            if (newMovie.getShowTime() == null) {
                throw new IllegalArgumentException("Show time can not be null");
            }
            if (newMovie.getAvailableSeats() > newMovie.getCapacity()) {
                throw new IllegalArgumentException("AvailableSeats must be less than capacity");
            }
        }
    }

    // create new movie
    public String addNewMovie(MovieDTO newMovie) {
        MovieValidator.validate(newMovie);

        if (movieDAO.findByTitle(newMovie.getTitle()) != null) {
            return newMovie.getTitle() + " already exists";
        }

        Movie movie = new Movie(newMovie.getTitle(), newMovie.getDescription(), newMovie.getShowTime(),
                newMovie.getGenre(), newMovie.getCapacity(), newMovie.getAvailableSeats());

        movieDAO.save(movie);

        return movie.getTitle() + " added successfully";
    }


    // edit a movie
    public String editMovie(int id, MovieDTO updatedMovie) {

        if (id <= 0) {
            throw new IllegalArgumentException("Id must be greater than 1");
        }

        if (updatedMovie == null) {
            throw new IllegalArgumentException("Updating movie can't be null");
        }


        Movie existingMovie = movieDAO.findById(id);
        if (existingMovie == null) {
            throw new IllegalArgumentException("Movie by id " + id + " not found");
        }


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


    // delete a movie
    public String deleteMovie(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Id must be greater than 1");
        } else if (movieDAO.findById(id) == null) {
            throw new IllegalArgumentException("Movie not found");

        } else {
            String title = movieDAO.findById(id).getTitle();

            movieDAO.deleteById(id);

            return title + " deleted successfully";
        }
    }


    // return list of all movies
    public List<Movie> listOfAllMovie() {

        List<Movie> movies = movieDAO.findAll();

        if (movies.isEmpty()) {
            throw new RuntimeException("No movie found");
        } else {
            return movieDAO.findAll();
        }
    }

    // return a single movie
    public Movie getMovie(int movieId) {

        Movie movie = movieDAO.findById(movieId);

        if (movie == null) {
            throw new RuntimeException("Movie not found");
        } else {
            return movieDAO.findById(movieId);
        }
    }
}
