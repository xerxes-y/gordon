package com.ramsay.gordon.service.impl;

import com.ramsay.gordon.domain.User;
import com.ramsay.gordon.repository.UserRepository;
import com.ramsay.gordon.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository users;

    /**
     * get user info
     * @param username
     * @return
     */
    @Override
    public Mono<User> getUserInfo(String username) {
        return users.findByUsername(username);
    }
}
