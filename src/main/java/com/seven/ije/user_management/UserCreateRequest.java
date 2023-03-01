package com.seven.ije.user_management;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.AppRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserCreateRequest implements AppRequest {
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^[A-Za-z'\\-]{2,30}$",message = "Name must be at least 2 characters long")
    private String firstName;
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^[A-Za-z'\\-]{2,30}$",message = "Name must be at least 2 characters long")
    private String lastName;
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^[+-][0-9]{10,14}$",message = "Name must be at least 2 characters long")
    private String phoneNo;
    @NotBlank(message = "Required field")
    @Email(regexp = "[A-Za-z0-9\\-]{2,}@[A-Za-z'\\-]{2,}\\.[A-Za-z'\\-]{2,}", message = "Invalid email format")
    private String email;
    @NotBlank(message = "Required field")
    private String password;
    @Past(message = "Future and current dates not allowed")
    private LocalDate dateBirth;
}
