package com.transquiz.transasker;

import com.transquiz.transasker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Objects;

@SpringBootApplication
public class TransaskerApplication {
    @Autowired
    private UserService userService;

    @PostConstruct
    public void registerAdministrator() {
        String adminUsername = System.getenv("ADUSER");
        String adminPassword = System.getenv("ADPASS");
        Boolean needreg = Boolean.valueOf(System.getenv("NEEDREG"));
        if (Objects.nonNull(needreg) && needreg) {
            userService.registerUser(adminUsername, adminPassword, "ADMIN");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(TransaskerApplication.class, args);
    }
}
