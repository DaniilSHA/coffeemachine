package com.example.coffeemachine.business_logic;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoffeeMachineWorker extends Thread {

    ThreadLocal<Long> coffeeMachineId = new ThreadLocal<>();

    public CoffeeMachineWorker(long coffeeMachineId) {
        this.coffeeMachineId.set(coffeeMachineId);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                doWork();
            } catch (InterruptedException e) {
                log.error("COFFEE MACHINE BROKEN");
            } finally {
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId.get(), StateCoffeeMachine.BROKEN);
            }
        }
    }

    private void doWork() throws InterruptedException {
        StateCoffeeMachine state = CoffeeMachineUtil.getCurrentStatus(coffeeMachineId.get());

        CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId.get(), StateCoffeeMachine.MADE_COFFEE);

        switch (state) {
            case MAKES_LATTE: makeLatte();break;
            case MAKES_AMERICANO: makeAmericano(); break;
            case MAKES_CAPPUCCINO: makeCappuccino(); break;
        }

        CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId.get(), StateCoffeeMachine.WAITING);
    }

    private void makeLatte() throws InterruptedException {
        Thread.sleep(7000);
    }

    private void makeCappuccino() throws InterruptedException {
        Thread.sleep(10000);
    }

    private void makeAmericano() throws InterruptedException {
        Thread.sleep(12000);
    }
}
