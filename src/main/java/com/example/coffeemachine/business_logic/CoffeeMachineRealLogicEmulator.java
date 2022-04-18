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
public class CoffeeMachineRealLogicEmulator implements Runnable {

    @Autowired
    CoffeeMachineService coffeeMachineService;

    ThreadLocal<Thread> currentThread = new ThreadLocal<>();
    ThreadLocal<CoffeeMachine> coffeeMachine = new ThreadLocal<>();

    public void turnOn(long coffeeMachineId) {
        currentThread.set(new Thread(new CoffeeMachineRealLogicEmulator()));
        currentThread.get().start();
        coffeeMachine.set(detectedCoffeeMachine(coffeeMachineId));
        setStatusCoffeeMachine(StateCoffeeMachine.TURNED_ON);
    }

    public void turnOff() {
        currentThread.get().interrupt();
        setStatusCoffeeMachine(StateCoffeeMachine.TURNED_OFF);
    }

    public void setStatusCoffeeMachine(StateCoffeeMachine stateCoffeeMachine) {
        coffeeMachine.get().setState(stateCoffeeMachine);
        coffeeMachineService.saveOrUpdateCoffeeMachine(coffeeMachine.get());
    }

    private CoffeeMachine detectedCoffeeMachine(long coffeeMachineId) {
        return coffeeMachineService
                .findById(coffeeMachineId)
                .orElseThrow(() -> new RuntimeException("coffee machine not found"));
    }

    @Override
    public void run() {
        while (!currentThread.get().isInterrupted()) {
            try {
                doWork();
            } catch (InterruptedException e) {
                log.error("COFFEE MACHINE BROKEN");
            } finally {
                setStatusCoffeeMachine(StateCoffeeMachine.BROKEN);
            }
        }
    }

    private void doWork() throws InterruptedException {
        StateCoffeeMachine state = coffeeMachine.get().getState();

       setStatusCoffeeMachine(StateCoffeeMachine.MADE_COFFEE);

         switch (state) {
             case MAKES_LATTE: makeLatte();break;
             case MAKES_AMERICANO: makeAmericano(); break;
             case MAKES_CAPPUCCINO: makeCappuccino(); break;
         }

        setStatusCoffeeMachine(StateCoffeeMachine.WAITING);
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
