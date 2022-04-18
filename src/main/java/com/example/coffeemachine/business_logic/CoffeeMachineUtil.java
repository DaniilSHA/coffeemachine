package com.example.coffeemachine.business_logic;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.example.coffeemachine.servicies.CoffeeMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoffeeMachineUtil {

    public static CoffeeMachineService coffeeMachineService;

    @Autowired
    public CoffeeMachineUtil(CoffeeMachineService coffeeMachineService) {
        CoffeeMachineUtil.coffeeMachineService = coffeeMachineService;
    }

    private static CoffeeMachine findById(long coffeeMachineId) {
        return coffeeMachineService.findById(coffeeMachineId).orElseThrow(() -> new RuntimeException("coffee machine don't found"));
    }

    public synchronized static void setStatusCoffeeMachine(long coffeeMachineId, StateCoffeeMachine state) {
        CoffeeMachine coffeeMachineById = findById(coffeeMachineId);
        coffeeMachineById.setState(state);
        coffeeMachineService.saveOrUpdateCoffeeMachine(coffeeMachineById);
    }

    public static StateCoffeeMachine getCurrentStatus(long coffeeMachineId) {
        return findById(coffeeMachineId).getState();
    }
}
