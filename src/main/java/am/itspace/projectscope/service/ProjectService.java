package am.itspace.projectscope.service;


import am.itspace.projectscope.dto.ProjectDto;
import am.itspace.projectscope.exception.ResourceNotFoundException;
import am.itspace.projectscope.model.Project;
import am.itspace.projectscope.model.QProject;
import am.itspace.projectscope.model.User;
import am.itspace.projectscope.repository.ProjectRepository;
import am.itspace.projectscope.repository.UserRepository;
import am.itspace.projectscope.security.service.UserSecurityService;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserSecurityService securityService;


    public ProjectService(ModelMapper modelMapper,
                          ProjectRepository projectRepository,
                          UserRepository userRepository,
                          UserSecurityService securityService) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    public Project createProject(ProjectDto projectDto, String deadline, String name) throws ParseException {
        Project newProject = modelMapper.map(projectDto, Project.class);
        User user = securityService.fetchCurrentUser();
        List<User> userList=userRepository.findAllByName(name);
        newProject.setMembers(userList);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = dateFormat.parse(deadline);
        newProject.setUserId(user.getId());
        newProject.setDeadLine(endDate);
        return projectRepository.save(newProject);
    }


    public List<Project> getAllByUserType(String userType) {
        List<Project> projects = projectRepository.findAll();
        projects = projects.stream()
                .filter(project -> project.getUserType().name().equals(userType))
                .collect(Collectors.toList());
        return projects;
    }


    public List<Project> getAllByMemberId(int id) {
       return projectRepository.findAllByUserId(id);

    }


    public Page<Project> searchProjectByName(Pageable pageable, String name) {
        PageRequest of = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        QProject project = QProject.project;
        BooleanExpression expression = project.name.containsIgnoreCase(name);

        return projectRepository.findAll(expression, of);

    }


    public List<Project> findAllByFiltersParam(LocalDate date, Date deadline) {
        List<Project> projects = projectRepository.findAll();
        projects = projects.stream()
                .filter(project -> project.getCreatedDate().isAfter(date) && project.getDeadLine().before(deadline))
                .collect(Collectors.toList());
        return projects;
    }


    public List<Project> getProjectsByUserId(int id) {
        QProject project = QProject.project;
        BooleanExpression expression =project.userId.eq(id);
        return Lists.newArrayList(projectRepository.findAll(expression));
    }

    public Project getProjectById(int id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }


    public void deleteProject(Project project) {
            projectRepository.delete(project);

        }


    }


