package am.itspace.projectscope.endpoint;


import am.itspace.projectscope.model.Log;
import am.itspace.projectscope.model.Project;
import am.itspace.projectscope.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/log")
public class LogEndpoint {

    private final LogService logService;

    @PostMapping("/addLog")
    public Log crateLog(@RequestParam double hour,
                        @RequestParam int id) {
        return logService.addLog( hour, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Log> deleteLog(@PathVariable int id) {
        logService.deleteLog(logService.getLogById(id));
        return ResponseEntity.ok().build();
    }


}
