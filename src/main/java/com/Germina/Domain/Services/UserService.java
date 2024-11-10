package com.Germina.Domain.Services;


import com.Germina.Domain.Dtos.UserDTO;
import com.Germina.Domain.Mappers.UserMapper;
import com.Germina.Persistence.Entities.User;
import com.Germina.Persistence.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtener todos los Usuarios.
     */
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Guardar o actualizar un Usuario.
     */
    public UserDTO save(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        userRepository.save(user);
        return userDTO;
    }


    /**
     * Actualiza El Estado Y Nombre De un Usuario.
     */
    public UserDTO upda(UserDTO userDTO) {
        Optional<User> existingUserOptional = userRepository.findById(userDTO.getId());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setState(userDTO.getState());
            userRepository.save(existingUser);
            return userDTO;
        } else {
            userRepository.save(UserMapper.toEntity(userDTO));
            return userDTO;
        }
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDto);
    }


    public void verifyUser(String mail) {

        User user = userRepository.findByMail(mail);
        if (user != null){
            user.setState("Activo");
            userRepository.save(user);
        }
    }

    public Optional<User> findByIds(Long id) {
        return userRepository.findById(id);
    }


    public User saves(User usuario) {
        userRepository.save(usuario);
        return usuario;
    }


}
