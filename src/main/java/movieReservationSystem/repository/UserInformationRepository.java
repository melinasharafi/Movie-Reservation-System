package movieReservationSystem.repository;

import movieReservationSystem.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationRepository extends JpaRepository<UserInformation, Integer> {

    UserInformation findById(int id);
    UserInformation findByUsername(String userName);
}


