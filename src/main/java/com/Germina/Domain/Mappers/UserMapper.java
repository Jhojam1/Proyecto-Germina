package com.Germina.Domain.Mappers;


import com.Germina.Domain.Dtos.UserDTO;
import com.Germina.Persistence.Entities.User;

public class UserMapper {

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFullName(userDTO.getFullName());
        user.setNumberIdentification(userDTO.getNumberIdentification());
        user.setMail(userDTO.getMail());
        user.setState(userDTO.getState());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public static UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setNumberIdentification(user.getNumberIdentification());
        userDTO.setMail(user.getMail());
        userDTO.setState(user.getState());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
