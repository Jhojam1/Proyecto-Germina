package com.Germina.Domain.Mappers;


import com.Germina.Domain.Dtos.TempUserDTO;
import com.Germina.Persistence.Entities.UserTemp;

public class TempUserMapper {

    public static UserTemp toEntity(TempUserDTO tempUserDTO) {
        UserTemp tempUser = new UserTemp();
        tempUser.setId(tempUserDTO.getId());
        tempUser.setFullName(tempUserDTO.getFullName());
        tempUser.setNumberIdentification(tempUserDTO.getNumberIdentification());
        tempUser.setMail(tempUserDTO.getMail());
        return tempUser;
    }

    public static TempUserDTO toDto(UserTemp tempUser) {
        TempUserDTO tempUserDTO = new TempUserDTO();
        tempUserDTO.setId(tempUser.getId());
        tempUserDTO.setFullName(tempUser.getFullName());
        tempUserDTO.setNumberIdentification(tempUser.getNumberIdentification());
        tempUserDTO.setMail(tempUser.getMail());
        return tempUserDTO;
    }
}
