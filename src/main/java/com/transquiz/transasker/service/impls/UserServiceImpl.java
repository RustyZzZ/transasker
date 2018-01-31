package com.transquiz.transasker.service.impls;

import com.google.common.collect.Sets;
import com.transquiz.transasker.model.security.Authority;
import com.transquiz.transasker.model.security.User;
import com.transquiz.transasker.model.security.UserPrincipal;
import com.transquiz.transasker.repository.security.AuthorityRepository;
import com.transquiz.transasker.repository.security.UserRepository;
import com.transquiz.transasker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        return userPrincipal.getUser();
    }

    @Transactional
    @Override
    public User registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Authority userAuthority = authorityRepository.getAuthorityByName("USER");
        if (Objects.isNull(userAuthority)) {
            userAuthority = new Authority();
            userAuthority.setName("USER");
        }
        user.setAuthorities(Sets.newHashSet(userAuthority));
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public User registerUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Authority userAuthority = authorityRepository.getAuthorityByName(role);
        if (Objects.isNull(userAuthority)) {
            userAuthority = new Authority();
            userAuthority.setName(role);
        }
        user.setAuthorities(Sets.newHashSet(userAuthority));
        userRepository.save(user);
        return user;
    }
}
