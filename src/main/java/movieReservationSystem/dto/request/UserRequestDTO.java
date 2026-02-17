package movieReservationSystem.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String userName;
    private String password;
    private String email;
}
