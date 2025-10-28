package pl.myc22ka.mathapp.utils.security.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    @GetMapping("/success")
    public String success(@NotNull OAuth2AuthenticationToken authentication) {
        return "Zalogowano przez: " + authentication.getAuthorizedClientRegistrationId() +
                " jako " + authentication.getPrincipal().getAttributes().get("email");
    }

    @GetMapping("/failure")
    public String failure() {
        return "Logowanie OAuth2 nie powiodło się.";
    }
}
