package movieReservationSystem.repository;

import movieReservationSystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDAO extends JpaRepository<Reservation, Integer> {

}


