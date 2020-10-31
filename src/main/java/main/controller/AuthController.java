package main.controller;

import main.api.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private static final String CHECK = "/api/auth/check";

    @GetMapping(CHECK)
    public ResponseEntity<ResultResponse> authCheck() {
        return ResponseEntity.ok(new ResultResponse().setResult(false));
    }
}
