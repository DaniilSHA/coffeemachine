package com.example.coffeemachine.repositories;

import com.example.coffeemachine.domain.CoffeeMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeMachineRepository extends JpaRepository<CoffeeMachine, Long> {
}
