package am.itspace.projectscope.service;

import am.itspace.projectscope.exception.ResourceNotFoundException;
import am.itspace.projectscope.model.Log;
import am.itspace.projectscope.model.Project;
import am.itspace.projectscope.model.User;
import am.itspace.projectscope.repository.LogRepository;
import am.itspace.projectscope.repository.ProjectRepository;
import am.itspace.projectscope.security.service.UserSecurityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final ProjectRepository projectRepository;
    private final UserSecurityService securityService;
    private final LogRepository logRepository;

    public LogService(ProjectRepository projectRepository, UserSecurityService securityService, LogRepository logRepository) {
        this.projectRepository = projectRepository;
        this.securityService = securityService;
        this.logRepository = logRepository;
    }


    public Log addLog(double hour, int id) {
        User user = securityService.fetchCurrentUser();
        List<Project> projectsList = projectRepository.findAllByUserId(user.getId());
        Log log = new Log();
        log.setUserId(user.getId());
        log.setProject(projectsList.get(id));
        log.setHours(hour);
        return logRepository.save(log);

    }

    public Log getLogById(int id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }


    public void deleteLog(Log log) {
        logRepository.delete(log);

    }

}
