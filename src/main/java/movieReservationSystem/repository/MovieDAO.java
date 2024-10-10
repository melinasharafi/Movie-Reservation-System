package movieReservationSystem.repository;

import movieReservationSystem.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDAO extends JpaRepository<Movie, Integer> {

}


