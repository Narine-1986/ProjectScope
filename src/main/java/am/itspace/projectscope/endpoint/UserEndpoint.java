package am.itspace.projectscope.endpoint;


import am.itspace.projectscope.dto.AuthRequest;
import am.itspace.projectscope.dto.UserDto;
import am.itspace.projectscope.model.User;
import am.itspace.projectscope.model.UserType;
import am.itspace.projectscope.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserEndpoint {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity auth(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest);
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/byType/")
    public List<User> getUsersByUserType(@RequestParam UserType userType) {
        return userService.getAllByUserType(userType);
    }

    @PutMapping("/image/{userId}")
    public void uploadImage(@PathVariable("userId") int userId, @RequestParam("image") MultipartFile file) throws IOException {
        userService.uploadUserImage(userId, file);
    }

    @GetMapping(value = "/get/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("name") String imageName) throws IOException {
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }
}
