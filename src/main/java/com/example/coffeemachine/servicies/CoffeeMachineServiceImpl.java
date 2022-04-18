package com.example.coffeemachine.servicies;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.repositories.CoffeeMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private CoffeeMachineRepository coffeeMachineRepository;

    @Autowired
    public CoffeeMachineServiceImpl(CoffeeMachineRepository coffeeMachineRepository) {
        this.coffeeMachineRepository = coffeeMachineRepository;
    }

    @Override
    public void saveOrUpdateCoffeeMachine(CoffeeMachine coffeeMachine) {
        coffeeMachineRepository.saveAndFlush(coffeeMachine);
    }

    @Override
    public Optional<CoffeeMachine> findById(long coffeeMachineId) {
        return coffeeMachineRepository.findById(coffeeMachineId);
    }

    @Override
    public List<CoffeeMachine> getAllCoffeeMachines() {
        return coffeeMachineRepository.findAll();
    }
}
