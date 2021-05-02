package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class CallbackController {

    @GetMapping("/callback")
    public String showAuthorizationCode(@RequestParam String code) {
        return code;
    }

}
