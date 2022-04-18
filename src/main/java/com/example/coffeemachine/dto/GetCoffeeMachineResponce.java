package com.example.coffeemachine.dto;

import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetCoffeeMachineResponce {

    private Long id;
    private StateCoffeeMachine stateCoffeeMachine;
    private String model;

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonProperty
    public StateCoffeeMachine getStateCoffeeMachine() {
        return stateCoffeeMachine;
    }

    @JsonProperty
    public String getModel() {
        return model;
    }
}
