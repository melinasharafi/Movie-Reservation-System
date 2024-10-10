package movieReservationSystem.service;

import movieReservationSystem.model.UserInformation;
import movieReservationSystem.repository.UserInformationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private UserInformationDAO adminDao;


    @Autowired
    public AdminService(UserInformationDAO adminDao) {
        this.adminDao = adminDao;
    }


    public void addNewAdmin(String username, String email) {
        UserInformation admin = new UserInformation(username, email);
        adminDao.save(admin);
    }
}
