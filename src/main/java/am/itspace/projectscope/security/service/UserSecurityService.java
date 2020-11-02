package am.itspace.projectscope.security.service;

import am.itspace.projectscope.exception.UnauthorizedAccessException;
import am.itspace.projectscope.model.User;
import am.itspace.projectscope.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserSecurityService implements UserDetailsService, SecurityService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s didn't found", username)));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public User fetchCurrentUser() {
        String loggedInUserEmail = getLoggedInUserEmail();
        return userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new UnauthorizedAccessException("No user has been authenticated on this request"));
    }

    private String getLoggedInUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User)
                return ((org.springframework.security.core.userdetails.User) principal).getUsername();
        }

        throw new UnauthorizedAccessException("No user has been authenticated on this request");
    }
}
