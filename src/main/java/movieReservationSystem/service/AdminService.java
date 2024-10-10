package movieReservationSystem.service;

import movieReservationSystem.model.Movie;
import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.MovieDAO;
import movieReservationSystem.repository.UserInformationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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
}
