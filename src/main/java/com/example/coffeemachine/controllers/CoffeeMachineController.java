package com.example.coffeemachine.controllers;

import com.example.coffeemachine.business_logic.CoffeeMachineRealLogicEmulator;
import com.example.coffeemachine.business_logic.CoffeeMachineUtil;
import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.example.coffeemachine.dto.AddCoffeeMachineRequest;
import com.example.coffeemachine.dto.getCoffeeMachineResponce;
import com.example.coffeemachine.servicies.CoffeeMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
    CoffeeMachineRealLogicEmulator coffeeMachineRealLogicEmulator;

    @Autowired
    public CoffeeMachineController(CoffeeMachineService coffeeMachineService,
                                   CoffeeMachineRealLogicEmulator coffeeMachineRealLogicEmulator) {
        this.coffeeMachineService = coffeeMachineService;
        this.coffeeMachineRealLogicEmulator = coffeeMachineRealLogicEmulator;
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
                    "WAITING"
            }, defaultValue = "TURNED_OFF")
            @RequestParam(name = "status") String status) {
        StateCoffeeMachine state = validateStateParam(status);
        switch (state) {
            case TURNED_ON: {
                coffeeMachineRealLogicEmulator.turnOn(coffeeMachineId);
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.TURNED_ON);
            } break;
            case MAKES_AMERICANO: CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.MAKES_AMERICANO); break;
            case MAKES_CAPPUCCINO: CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.MAKES_CAPPUCCINO); break;
            case MAKES_LATTE: CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.MAKES_LATTE); break;
            case TURNED_OFF: CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.TURNED_OFF); break;
        }
    }

    @Operation(
            summary = "get coffee machine",
            description = "get specified coffee machine"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = getCoffeeMachineResponce.class))),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/{coffeeMachineId}")
    @ResponseStatus(HttpStatus.OK)
    public getCoffeeMachineResponce getCoffeeMachine(@PathVariable("coffeeMachineId") long coffeeMachineId) {
        CoffeeMachine coffeeMachine = coffeeMachineService
                .findById(coffeeMachineId)
                .orElseThrow(() -> new RuntimeException("coffee machine not found"));
        return new getCoffeeMachineResponce (
                coffeeMachine.getId(),
                coffeeMachine.getState(),
                coffeeMachine.getModel()
        );
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
            case "WAITING" : return StateCoffeeMachine.WAITING;
            default: throw new RuntimeException("Invalid input param");
        }
    }
}
