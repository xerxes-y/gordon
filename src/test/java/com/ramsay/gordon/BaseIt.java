package com.ramsay.gordon;

import com.ramsay.gordon.domain.Ingredient;
import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.domain.User;
import com.ramsay.gordon.repository.RecipeRepository;
import com.ramsay.gordon.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

import static org.testcontainers.images.PullPolicy.alwaysPull;

@Testcontainers
@SpringBootTest
public class BaseIt {


    static MongoDBContainer mongoDBContainer ;
    static {
        mongoDBContainer=new MongoDBContainer("mongo:5.0.10");
        mongoDBContainer.waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

    }

}
