package movieReservationSystem.repository;

import movieReservationSystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationDAO extends JpaRepository<Reservation, Integer> {

    Reservation findByUserIdAndMovieId(int userId, int movieId);

}


