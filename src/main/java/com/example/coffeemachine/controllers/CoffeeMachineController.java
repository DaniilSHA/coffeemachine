package com.example.coffeemachine.controllers;

import com.example.coffeemachine.domain.CoffeeMachine;
import com.example.coffeemachine.domain.StateCoffeeMachine;
import com.example.coffeemachine.dto.AddCoffeeMachineRequest;
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
@RequestMapping("/coffeemachine")
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
            @ApiResponse(
                    description = "OK", responseCode = "201", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = AddCoffeeMachineRequest.class)
            )),
            @ApiResponse(
                    description = "Internal server error", responseCode = "500")
    })
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void add (@RequestBody AddCoffeeMachineRequest addCoffeeMachineRequest) {
        CoffeeMachine newCoffeeMachine = new CoffeeMachine();
        newCoffeeMachine.setModel(addCoffeeMachineRequest.getModel());
        newCoffeeMachine.setState(StateCoffeeMachine.TURNED_OFF);
        coffeeMachineService.addCoffeeMachine(newCoffeeMachine);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Exception exception) {
        return (exception != null ? exception.getMessage() : "Unknown error");
    }

}
