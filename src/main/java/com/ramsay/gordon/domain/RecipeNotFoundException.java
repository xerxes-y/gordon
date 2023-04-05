package com.ramsay.gordon.domain;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(String id) {
        super("recipe:" + id + " is not found.");
    }

}
