package com.ramsay.gordon.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialFilter {
    private List<String> includeIngredients;
    private List<String> excludeIngredients;
    private String instructionSearch;
    private Integer serves;
}
