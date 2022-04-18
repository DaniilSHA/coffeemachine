package com.example.coffeemachine.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddCoffeeMachineRequest {

    private String model;

    @JsonCreator
    public AddCoffeeMachineRequest(@JsonProperty("model") String model) {
        this.model = model;
    }
}
