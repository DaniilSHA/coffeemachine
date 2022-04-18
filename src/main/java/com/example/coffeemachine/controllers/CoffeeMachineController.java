package com.example.coffeemachine.controllers;

import com.example.coffeemachine.dto.AddCoffeeMachineRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coffeemachine")
public class CoffeeMachineController {

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
        System.out.println(addCoffeeMachineRequest.getModel());
        throw new RuntimeException("hello");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Exception exception) {
        return (exception != null ? exception.getMessage() : "Unknown error");
    }

}
