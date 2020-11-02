package am.itspace.projectscope.security.service;


import am.itspace.projectscope.model.User;

@FunctionalInterface
public interface SecurityService {
    User fetchCurrentUser();
}
