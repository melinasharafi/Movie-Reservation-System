package movieReservationSystem.repository;

import movieReservationSystem.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationDAO extends JpaRepository<UserInformation, Integer> {

}


