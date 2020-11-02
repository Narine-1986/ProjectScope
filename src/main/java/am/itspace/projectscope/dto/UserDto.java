package am.itspace.projectscope.dto;

import am.itspace.projectscope.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String name;
    private String surname;
    private String email;
    private String password;
    private UserType userType=UserType.TEAM_MEMBER;;



}