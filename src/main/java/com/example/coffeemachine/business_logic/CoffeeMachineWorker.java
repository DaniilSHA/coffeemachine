package com.example.coffeemachine.business_logic;

import com.example.coffeemachine.domain.StateCoffeeMachine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoffeeMachineWorker extends Thread {

    private final Long coffeeMachineId;

    public CoffeeMachineWorker(long coffeeMachineId) {
        this.coffeeMachineId = coffeeMachineId;
    }

    @Override
    public void run() {
        log.info(Thread.currentThread().getName() + " -> start work with coffee machine №" + coffeeMachineId);
        while (!isInterrupted()) {
            try {
                doWork();
            } catch (InterruptedException e) {
                log.error("COFFEE MACHINE BROKEN");
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.BROKEN);
            }
        }
    }

    private void doWork() throws InterruptedException {;
        StateCoffeeMachine state = CoffeeMachineUtil.getCurrentStatus(coffeeMachineId);
        switch (state) {
            case MAKES_LATTE:
                makeLatte();
                break;
            case MAKES_AMERICANO:
                makeAmericano();
                break;
            case MAKES_CAPPUCCINO:
                makeCappuccino();
                break;
            case TURNED_OFF: {
                log.info(Thread.currentThread().getName() + " -> end work with coffee machine №" + coffeeMachineId);
                Thread.currentThread().interrupt();
                return;
            }
            default: Thread.sleep(4000);
        }
        log.info(Thread.currentThread().getName() + " -> waiting for the task on coffee machine №" + coffeeMachineId);
    }

    private void makeLatte() throws InterruptedException {
        log.info(Thread.currentThread().getName() + " -> start making latte on №" + coffeeMachineId + " coffee machine");
        Thread.sleep(7000);
        CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.WAITING);
        log.info(Thread.currentThread().getName() + " -> end make latte on №" + coffeeMachineId + " coffee machine");
    }

    private void makeCappuccino() throws InterruptedException {
        log.info(Thread.currentThread().getName() + " -> start making cappuccino on №" + coffeeMachineId + " coffee machine");
        Thread.sleep(10000);
        CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.WAITING);
        log.info(Thread.currentThread().getName() + " -> end make cappuccino on №" + coffeeMachineId + " coffee machine");
    }

    private void makeAmericano() throws InterruptedException {
        log.info(Thread.currentThread().getName() + " -> start making americano on №" + coffeeMachineId + " coffee machine");
        Thread.sleep(12000);
        CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.WAITING);
        log.info(Thread.currentThread().getName() + " -> end make americano on №" + coffeeMachineId + " coffee machine");
    }
}
