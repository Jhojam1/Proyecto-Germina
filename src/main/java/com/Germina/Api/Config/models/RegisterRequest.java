package com.Germina.Api.Config.models;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private Long id;
    private String fullName;
    private Long numberIdentification;
    private String state;
    private String mail;
    private String password;
    private String role;
}
