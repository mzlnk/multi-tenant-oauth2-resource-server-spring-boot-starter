package pl.mzlnk.demo;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class DemoController {

    @GetMapping("/auth/callback")
    public String showAuthorizationCode(@RequestParam String code) {
        return code;
    }

    @GetMapping("/authorized/me")
    public String me(Principal principal) {
        return "User: " + principal.getName();
    }

    @GetMapping("/non-authorized")
    public String helloWorld() {
        return "Hello World from non authorized URI!";
    }

    @GetMapping("/non-authorized/cookies")
    public List<Cookie> getCookies(HttpServletRequest request) {
        SecurityContextHolder.getContext()
        return Stream.of(request.getCookies())
                .collect(Collectors.toList());
    }

}
