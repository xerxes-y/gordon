package com.ramsay.gordon.service;

import com.ramsay.gordon.domain.User;
import reactor.core.publisher.Mono;

public interface UserManagementService {
    Mono<User> getUserInfo(String username);
}
