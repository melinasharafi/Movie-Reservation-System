package movieReservationSystem.repository;

import movieReservationSystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Stack;

public interface ReservationDAO extends JpaRepository<Reservation, Integer> {

    Reservation findByUserIdAndMovieId(int userId, int movieId);
    Stack<Reservation> findAllByUserId(int userId);

}


