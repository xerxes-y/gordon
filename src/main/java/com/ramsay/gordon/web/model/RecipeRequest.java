package com.ramsay.gordon.web.model;

import com.ramsay.gordon.domain.Ingredient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {
    @NotNull
    private String name;

    @NotNull
    private String foodType;
    @NotNull
    private List<Ingredient> ingredients;
    @NotNull
    private Integer serves;
    @NotNull
    private String instruction;

}
