package movieReservationSystem.service;

import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.UserInformationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserInformationDAO userDao;

    @Autowired
    public UserService(UserInformationDAO userDao) {
        this.userDao = userDao;
    }

    // add new user
    public void addNewUser(String username, String email) {
        UserInformation user = new UserInformation(username, email);
        userDao.save(user);
    }
}
