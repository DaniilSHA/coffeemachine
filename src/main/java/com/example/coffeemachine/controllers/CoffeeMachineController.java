package com.example.coffeemachine.controllers;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.example.coffeemachine.dto.AddCoffeeMachineRequest;
import com.example.coffeemachine.servicies.CoffeeMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coffee_machine")
public class CoffeeMachineController {

    CoffeeMachineService coffeeMachineService;

    @Autowired
    public CoffeeMachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }

    @Operation(
            summary = "add coffee machine",
            description = "Creates new coffee machine"
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "201"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody AddCoffeeMachineRequest addCoffeeMachineRequest) {
        CoffeeMachine newCoffeeMachine = new CoffeeMachine();
        newCoffeeMachine.setModel(addCoffeeMachineRequest.getModel());
        newCoffeeMachine.setState(StateCoffeeMachine.TURNED_OFF);
        coffeeMachineService.saveOrUpdateCoffeeMachine(newCoffeeMachine);
    }

    @Operation(
            summary = "turn status coffee machine",
            description = "turned status specified coffee machine"
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "202"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @PutMapping("/{coffeeMachineId}/change_status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void turnStatus(
            @PathVariable("coffeeMachineId") long coffeeMachineId,
            @Schema(allowableValues = {
                    "TURNED_ON",
                    "TURNED_OFF",
                    "BROKEN",
                    "REPAIR",
                    "MAKES_LATTE",
                    "MAKES_CAPPUCCINO",
                    "MAKES_AMERICANO",
                    "MADE_COFFEE",
                    "WAITING"
            }, defaultValue = "TURNED_OFF")
            @RequestParam(name = "status") String status) {
        StateCoffeeMachine state = validateStateParam(status);
        CoffeeMachine coffeeMachine = coffeeMachineService
                .findById(coffeeMachineId)
                .orElseThrow(() -> new RuntimeException("coffee machine not found"));
        coffeeMachine.setState(state);
        coffeeMachineService.saveOrUpdateCoffeeMachine(coffeeMachine);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Exception exception) {
        return (exception != null ? exception.getMessage() : "Unknown error");
    }

    private StateCoffeeMachine validateStateParam(String status) {
        switch (status) {
            case "TURNED_ON" : return StateCoffeeMachine.TURNED_ON;
            case "TURNED_OFF" : return  StateCoffeeMachine.TURNED_OFF;
            case "BROKEN" : return StateCoffeeMachine.BROKEN;
            case "REPAIR" : return StateCoffeeMachine.REPAIR;
            case "MAKES_LATTE" : return StateCoffeeMachine.MAKES_LATTE;
            case "MAKES_CAPPUCCINO" : return StateCoffeeMachine.MAKES_CAPPUCCINO;
            case "MAKES_AMERICANO" : return StateCoffeeMachine.MAKES_AMERICANO;
            case "MADE_COFFEE" : return StateCoffeeMachine.MADE_COFFEE;
            case "WAITING" : return StateCoffeeMachine.WAITING;
            default: throw new RuntimeException("Invalid input param");
        }
    }
}
