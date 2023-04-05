package com.ramsay.gordon.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeId implements Serializable {

    private String id;

}
