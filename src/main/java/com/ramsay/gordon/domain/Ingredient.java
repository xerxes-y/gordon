package com.ramsay.gordon.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String amount;


}
