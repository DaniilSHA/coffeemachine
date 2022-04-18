package com.example.coffeemachine.servicies;

import com.example.coffeemachine.domain.CoffeeMachine;

import java.util.Optional;

public interface CoffeeMachineService {

    void saveOrUpdateCoffeeMachine(CoffeeMachine coffeeMachine);

    Optional<CoffeeMachine> findById(long coffeeMachineId);
}
