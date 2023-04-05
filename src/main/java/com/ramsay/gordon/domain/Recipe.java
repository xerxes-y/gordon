package com.ramsay.gordon.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recipe implements PersistentEntity, Serializable {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private Integer serves;

    @NotBlank
    @TextIndexed
    private String instruction;

    @NotBlank
    private List<Ingredient> ingredients;

    @Builder.Default
    private FoodType type = FoodType.UNKNOWN;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private Username createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    private Username lastModifiedBy;

    public enum FoodType {

        VEGETARIAN,NON_VEGETARIAN,UNKNOWN;

    }

}
