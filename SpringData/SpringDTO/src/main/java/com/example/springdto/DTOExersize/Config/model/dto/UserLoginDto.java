package com.example.springdto.DTOExersize.Config.model.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class UserLoginDto {

    private String email;
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Email(message = "Enter valid email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Pattern(regexp = "[A-za-z\\d]{6,}",
            message = "Enter valid password. Length must be at least 6 symbols and " +
                    "must contain at least 1 uppercase, 1 lowercase letter and 1 digit")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
