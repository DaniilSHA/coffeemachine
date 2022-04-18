package com.example.coffeemachine.business_logic;

import org.springframework.stereotype.Component;

@Component
public class CoffeeMachineRealLogicEmulator implements Runnable {



    public void turnOn() {
        new Thread(new CoffeeMachineRealLogicEmulator()).start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            makeWork();
        }
    }
}
