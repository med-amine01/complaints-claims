package de.tekup.complaintsclaims.controller;

import de.tekup.complaintsclaims.dto.request.AuthRequest;
import de.tekup.complaintsclaims.dto.response.ApiResponse;
import de.tekup.complaintsclaims.dto.response.AuthResponse;
import de.tekup.complaintsclaims.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    public static final String SUCCESS = "SUCCESS";

    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AuthResponse>> createJwtToken(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        AuthResponse authResponse = jwtService.generateJwtToken(authRequest);

        ApiResponse<AuthResponse> response = ApiResponse
                .<AuthResponse>builder()
                .status(SUCCESS)
                .results(authResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}