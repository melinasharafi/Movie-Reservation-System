package movieReservationSystem.service;

import jakarta.persistence.EntityExistsException;


import lombok.RequiredArgsConstructor;
import movieReservationSystem.dto.MovieDTO;
import movieReservationSystem.dto.request.UserRequestDTO;
import movieReservationSystem.dto.response.UserRegistrationResponseDTO;
import movieReservationSystem.model.Movie;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.UserInformationRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserInformationRepository adminRepository;
    private final MovieDAO movieDAO;
    private final JdbcUserDetailsManager userDetailsManager;
    private final BCryptPasswordEncoder encoder;

    // create new admin
    public UserRegistrationResponseDTO addNewAdmin(UserRequestDTO adminDTO) {

        if (userDetailsManager.userExists(adminDTO.getUserName())) {
            throw new RuntimeException(adminDTO.getUserName() + " already exists");

        }

        UserDetails newAdmin = User.builder()
                .username(adminDTO.getUserName())
                .password(encoder.encode(adminDTO.getPassword()))
                .roles("ADMIN")
                .build();

        userDetailsManager.createUser(newAdmin);

        return UserRegistrationResponseDTO.builder()
                .username(adminDTO.getUserName())
                .email(adminDTO.getEmail())
                .message(adminDTO.getUserName() + " registered successfully")
                .build();

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
            throw new EntityExistsException(newMovie.getTitle() + " already exists.");
        }

        Movie movie = new Movie(newMovie.getTitle(), newMovie.getDescription(), newMovie.getShowTime(),
                newMovie.getGenre(), newMovie.getCapacity(), newMovie.getAvailableSeats());

        movieDAO.save(movie);

        return movie.getTitle() + " added successfully";
    }


    // edit a movie
    public String editMovie(int id, MovieDTO updatedMovie) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive integer");
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
            throw new IllegalArgumentException("ID must be positive integer");
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

        if (movieId <= 0) {
            throw new IllegalArgumentException("Id must be greater than 1");
        } else if (movieDAO.findById(movieId) == null) {
            throw new IllegalArgumentException("Movie not found");
        } else {
            return movieDAO.findById(movieId);
        }
    }
}
