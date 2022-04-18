package com.example.coffeemachine.business_logic;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.example.coffeemachine.servicies.CoffeeMachineService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class CoffeeMachineRealLogicEmulator {

    public void turnOn(long coffeeMachineId) {
        new CoffeeMachineWorker(coffeeMachineId).start();
    }

}
