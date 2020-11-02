package am.itspace.projectscope.dto;

import am.itspace.projectscope.model.User;
import am.itspace.projectscope.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private String name;
    private double hours;
    private UserType userType=UserType.TEAM_LEADER;


}
