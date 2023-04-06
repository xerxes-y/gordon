package com.ramsay.gordon.repository;


import com.ramsay.gordon.BaseIt;
import com.ramsay.gordon.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserRepositoryTest extends BaseIt {

    @Autowired
    private UserRepository users;


    @ParameterizedTest
    @ValueSource(strings = {"user", "xerxes-y", "admin"})
    void testFindByUsername(String name) {
        this.users.save(User.builder().username(name).password("password").roles(List.of("ROLE_USER")).build())
                .flatMap(__ -> this.users.findByUsername(name))
                .as(StepVerifier::create)
                .consumeNextWith(user -> assertThat(user.getUsername()).isEqualTo(name))
                .verifyComplete();
    }

}
