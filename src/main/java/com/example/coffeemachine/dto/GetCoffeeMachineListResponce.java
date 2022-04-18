package com.example.coffeemachine.dto;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetCoffeeMachineListResponce {
    private List<CoffeeMachine> coffeeMachineList;

    @JsonProperty
    public List<CoffeeMachine> getCoffeeMachineList() {
        return coffeeMachineList;
    }
}
