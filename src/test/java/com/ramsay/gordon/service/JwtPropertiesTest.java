package com.ramsay.gordon.service;

import com.ramsay.gordon.BaseIt;
import com.ramsay.gordon.jwt.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JwtPropertiesTest extends BaseIt {

    @Nested
    class UnitTest {
        private JwtProperties properties;

        @BeforeEach
        void setup() {
            properties = new JwtProperties();
        }

        @Test
        void testProperties() {
            assertThat(this.properties.getSecretKey()).isEqualTo("rzxlszyykpbgqcflzxsqcysyhljt");
            assertThat(this.properties.getValidityInMs()).isEqualTo(3600000L);
        }
    }

    @Nested
    @SpringBootTest
    @TestPropertySource(properties = {
            "jwt.secretKey=testrzxlszyykpbgqcflzxsqcysyhljt",
            "jwt.validityInMs=100"
    })
    class IntegrationTest {

        @Autowired
        private JwtProperties properties;

        @Test
        void testProperties() {
            assertThat(this.properties.getSecretKey()).isEqualTo("testrzxlszyykpbgqcflzxsqcysyhljt");
            assertThat(this.properties.getValidityInMs()).isEqualTo(100);
        }
    }

}
