package am.itspace.projectscope.endpoint;


import am.itspace.projectscope.dto.ProjectDto;
import am.itspace.projectscope.model.Project;
import am.itspace.projectscope.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/project")
public class ProjectEndpoint {

    private final ProjectService projectService;

    @PostMapping("/add")
    public Project create(@RequestBody ProjectDto project, String deadline, String name) throws ParseException {
        return projectService.createProject(project, deadline,name);
    }

    @GetMapping("/byTeamLeader")
    public List<Project> getProjectsByUserType(@RequestParam String userType) {
        return projectService.getAllByUserType(userType);
    }

    @GetMapping("/search")
    public Page<Project> search(@RequestParam(defaultValue = "") String name,
                                @PageableDefault Pageable pageable) {
        return projectService.searchProjectByName(pageable, name);
    }

    @GetMapping("/filter")
    public List<Project> filterByField(
            @RequestParam LocalDate date,
            @RequestParam Date deadLine) {
        return projectService.findAllByFiltersParam(date, deadLine);
    }

    @GetMapping("/byUserId/{userId}")
    public List<Project> getProjectsByUserId(@PathVariable int userId) {
        return projectService.getProjectsByUserId(userId);
    }
    @GetMapping("/byMember/{id}")
    public List<Project> getProjectsByMemberId(@PathVariable int id) {
        return projectService.getAllByMemberId(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable int id) {
        projectService.deleteProject(projectService.getProjectById(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable int id) {
        return projectService.getProjectById(id);
    }


}
