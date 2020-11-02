package am.itspace.projectscope.service;


import am.itspace.projectscope.dto.AuthRequest;
import am.itspace.projectscope.dto.AuthResponse;
import am.itspace.projectscope.dto.UserDto;
import am.itspace.projectscope.exception.DuplicateEntityException;
import am.itspace.projectscope.exception.UserNotFoundException;
import am.itspace.projectscope.model.User;
import am.itspace.projectscope.model.UserType;
import am.itspace.projectscope.repository.UserRepository;
import am.itspace.projectscope.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper,  JwtUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User createUser(UserDto userDto) {
        if (!userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = modelMapper.map(userDto, User.class);
            return userRepository.save(user);
        } else {
            throw new DuplicateEntityException("Email already exists");

        }
    }

    public ResponseEntity login(AuthRequest authRequest) {
        Optional<User> byEmail = userRepository.findByEmail(authRequest.getEmail());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                String token = jwtTokenUtil.generateToken(user.getEmail());
                return ResponseEntity.ok(AuthResponse.builder()
                        .token(token)
                        .name(user.getName())
                        .surname(user.getSurname())
                        .build());
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    public List<User> getAllByUserType(UserType userType) {
        List<User> users = userRepository.findAllByUserType(userType);
        if (userType == UserType.TEAM_MEMBER) {
            return users;
        }
        throw new UserNotFoundException();
    }


    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void uploadUserImage(int userId, MultipartFile file) throws IOException {
        String profilePic = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir, profilePic);
        file.transferTo(image);
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            user.setProfilePic(profilePic);
            userRepository.save(user);
        }
        throw new UserNotFoundException(userId);
    }
}

