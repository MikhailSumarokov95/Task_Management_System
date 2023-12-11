package ru.sumarokov.task_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sumarokov.task_management_system.dto.TokenDto;
import ru.sumarokov.task_management_system.dto.UserDto;
import ru.sumarokov.task_management_system.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "The auth API")
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @Operation(summary = "Register", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered"),
    })
    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(service.register(userDto));
    }

    @Operation(summary = "Authenticate", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(service.authenticate(userDto));
    }
}
