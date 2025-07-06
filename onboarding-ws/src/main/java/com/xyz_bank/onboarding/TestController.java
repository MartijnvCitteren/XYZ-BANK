package com.xyz_bank.onboarding;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @PostMapping("/test")
    public ResponseEntity createAccount() {
        testService.createCustomer();
        return ResponseEntity.ok().build();
    }
}
