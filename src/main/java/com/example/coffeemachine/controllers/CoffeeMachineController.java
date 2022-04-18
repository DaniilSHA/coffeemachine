package com.example.coffeemachine.controllers;

import com.example.coffeemachine.business_logic.CoffeeMachineRealLogicEmulator;
import com.example.coffeemachine.business_logic.CoffeeMachineUtil;
import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.example.coffeemachine.dto.AddCoffeeMachineRequest;
import com.example.coffeemachine.dto.GetCoffeeMachineListResponce;
import com.example.coffeemachine.dto.GetCoffeeMachineResponce;
import com.example.coffeemachine.servicies.CoffeeMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/{coffeeMachineId}/change-status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void turnStatus(
            @PathVariable("coffeeMachineId") long coffeeMachineId,
            @Schema(allowableValues = {
                    "TURNED_ON",
                    "TURNED_OFF",
                    "MAKES_LATTE",
                    "MAKES_CAPPUCCINO",
                    "MAKES_AMERICANO",
            }, defaultValue = "TURNED_OFF")
            @RequestParam(name = "status") String status) {
        StateCoffeeMachine state = validateStateParam(status);

        switch (state) {
            case TURNED_ON: {
                if (isCoffeeMachineAlreadyTurnedOn(coffeeMachineId))
                    throw new RuntimeException("coffee machine already turned on");
                coffeeMachineRealLogicEmulator.turnOn(coffeeMachineId);
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.TURNED_ON);
            }
            break;
            case MAKES_AMERICANO:
                if (!isCoffeeMachineAlreadyTurnedOn(coffeeMachineId))
                    throw new RuntimeException("coffee machine is turned off");
                if (isCoffeeMachineAlreadyMakingCoffee(coffeeMachineId))
                    throw new RuntimeException("coffee machine is busy");
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.MAKES_AMERICANO);
                break;
            case MAKES_CAPPUCCINO:
                if (!isCoffeeMachineAlreadyTurnedOn(coffeeMachineId))
                    throw new RuntimeException("coffee machine is turned off");
                if (isCoffeeMachineAlreadyMakingCoffee(coffeeMachineId))
                    throw new RuntimeException("coffee machine is busy");
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.MAKES_CAPPUCCINO);
                break;
            case MAKES_LATTE:
                if (!isCoffeeMachineAlreadyTurnedOn(coffeeMachineId))
                    throw new RuntimeException("coffee machine is turned off");
                if (isCoffeeMachineAlreadyMakingCoffee(coffeeMachineId))
                    throw new RuntimeException("coffee machine is busy");
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.MAKES_LATTE);
                break;
            case TURNED_OFF:
                CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachineId, StateCoffeeMachine.TURNED_OFF);
                break;
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
                            schema = @Schema(implementation = GetCoffeeMachineResponce.class))),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/{coffeeMachineId}")
    @ResponseStatus(HttpStatus.OK)
    public GetCoffeeMachineResponce getCoffeeMachine(@PathVariable("coffeeMachineId") long coffeeMachineId) {
        CoffeeMachine coffeeMachine = coffeeMachineService
                .findById(coffeeMachineId)
                .orElseThrow(() -> new RuntimeException("coffee machine not found"));
        return new GetCoffeeMachineResponce(
                coffeeMachine.getId(),
                coffeeMachine.getState(),
                coffeeMachine.getModel()
        );
    }

    @Operation(
            summary = "get all coffee machine",
            description = "get all coffee machine"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCoffeeMachineListResponce.class))),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public GetCoffeeMachineListResponce getAll() {
        List<CoffeeMachine> allCoffeeMachines = coffeeMachineService.getAllCoffeeMachines();
        return new GetCoffeeMachineListResponce(allCoffeeMachines);
    }

    @Operation(
            summary = "turn off all coffee machine",
            description = "turn off all coffee machine"
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "202"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @PutMapping("/turned-off-all")
    @ResponseStatus(HttpStatus.OK)
    public void turnedOffAll() {
        List<CoffeeMachine> allCoffeeMachines = coffeeMachineService.getAllCoffeeMachines();
        allCoffeeMachines.forEach((coffeeMachine) -> {
            CoffeeMachineUtil.setStatusCoffeeMachine(coffeeMachine.getId(), StateCoffeeMachine.TURNED_OFF);
        });
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Exception exception) {
        return (exception != null ? exception.getMessage() : "Unknown error");
    }

    private StateCoffeeMachine validateStateParam(String status) {
        switch (status) {
            case "TURNED_ON":
                return StateCoffeeMachine.TURNED_ON;
            case "TURNED_OFF":
                return StateCoffeeMachine.TURNED_OFF;
            case "MAKES_LATTE":
                return StateCoffeeMachine.MAKES_LATTE;
            case "MAKES_CAPPUCCINO":
                return StateCoffeeMachine.MAKES_CAPPUCCINO;
            case "MAKES_AMERICANO":
                return StateCoffeeMachine.MAKES_AMERICANO;
            default:
                throw new RuntimeException("Invalid input param");
        }
    }

    private boolean isCoffeeMachineAlreadyMakingCoffee(long coffeeMachineId) {
        switch (CoffeeMachineUtil.getCurrentStatus(coffeeMachineId)) {
            case MAKES_AMERICANO:
            case MAKES_CAPPUCCINO:
            case MAKES_LATTE:
                return true;
        }
        return false;
    }

    private boolean isCoffeeMachineAlreadyTurnedOn(long coffeeMachineId) {
        switch (CoffeeMachineUtil.getCurrentStatus(coffeeMachineId)) {
            case MAKES_AMERICANO:
            case MAKES_CAPPUCCINO:
            case MAKES_LATTE:
            case WAITING:
            case TURNED_ON:
                return true;
        }
        return false;
    }
}
