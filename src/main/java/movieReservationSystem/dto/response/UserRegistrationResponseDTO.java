package movieReservationSystem.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationResponseDTO {
    private String username;
    private String email;
    private String message;
}
