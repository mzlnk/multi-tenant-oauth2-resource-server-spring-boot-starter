package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/non-authorized")
public class NonAuthorizedController {

    @GetMapping
    public String helloWorld() {
        return "Hello World from non authorized resource!";
    }

}
