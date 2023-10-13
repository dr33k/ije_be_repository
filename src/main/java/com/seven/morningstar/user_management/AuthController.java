package com.seven.morningstar.user_management;

import com.seven.morningstar.responses.Response;
import jakarta.validation.Valid;

import static com.seven.morningstar.util.Responder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.seven.morningstar.util.AppConstants.VERSION;

@RestController
@RequestMapping(VERSION)
public class AuthController {
    UserService userService;
    AuthenticationProvider authenticationProvider;
    public AuthController(UserService userService, AuthenticationProvider authenticationProvider) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> createResource(@Valid @RequestBody UserCreateRequest request) {
            UserDTO userDTO = userService.register(request);
            return ok(userDTO.data, userDTO.token);

//            EntityModel<ResponseEntity<Response>> entityModel = okHal(record);
//
//            Map<String, Object> refToInvocationObjectMap = Map.of("view",methodOn(UserController.class).getResource());
//
//            createAndIncludeLinks(refToInvocationObjectMap, entityModel);
    }

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> login(@Valid LoginRequest request){
        User user = (User) authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())).getPrincipal();

        UserDTO userDTO = userService.login(user);
        return ok(userDTO.data, userDTO.token);
    }
}
