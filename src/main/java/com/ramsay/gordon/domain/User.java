package com.ramsay.gordon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xerxes-y
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @Id
    private String id;

    private String username;

    @JsonIgnore
    private String password;

    @Email
    private String email;

    @Builder.Default()
    private boolean active = true;

    @Builder.Default()
    private List<String> roles = new ArrayList<>();

}
