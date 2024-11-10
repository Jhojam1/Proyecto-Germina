package com.Germina.Domain.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String fullName;
    private Long numberIdentification;
    private String state;
    private String mail;
    private String password;

}
