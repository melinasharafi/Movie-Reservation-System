package movieReservationSystem.repository;

import movieReservationSystem.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDAO extends JpaRepository<Movie, Integer> {

    Movie findByTitle(String title);
    Movie findById(int id);
    Boolean existsByTitle(String title);
}


