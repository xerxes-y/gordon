package com.ramsay.gordon.web;

import com.ramsay.gordon.domain.User;
import com.ramsay.gordon.repository.UserRepository;
import com.ramsay.gordon.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author xerxes-y
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService users;

    @GetMapping("/v1/users/{username}")
    public Mono<User> get(@PathVariable() String username) {
        return this.users.getUserInfo(username);
    }

}
