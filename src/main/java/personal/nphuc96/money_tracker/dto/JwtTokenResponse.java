package personal.nphuc96.money_tracker.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenResponse {
    private String token;

    private Integer userId;
}
