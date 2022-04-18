package com.example.coffeemachine.servicies;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.repositories.CoffeeMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private CoffeeMachineRepository coffeeMachineRepository;

    @Autowired
    public CoffeeMachineServiceImpl(CoffeeMachineRepository coffeeMachineRepository) {
        this.coffeeMachineRepository = coffeeMachineRepository;
    }

    @Transactional
    @Override
    public void addCoffeeMachine(CoffeeMachine coffeeMachine) {
        coffeeMachineRepository.saveAndFlush(coffeeMachine);
    }
}
