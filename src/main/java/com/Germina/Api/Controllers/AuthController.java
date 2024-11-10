package com.Germina.Api.Controllers;

import com.Germina.Api.Config.Exception.Exceptions;
import com.Germina.Api.Config.models.AuthResponse;
import com.Germina.Api.Config.models.AuthenticationRequest;
import com.Germina.Api.Config.models.RegisterRequest;
import com.Germina.Domain.Dtos.UserDTO;
import com.Germina.Domain.Services.AuthService;
import com.Germina.Domain.Services.JwtService;
import com.Germina.Domain.Services.UserService;
import com.Germina.Persistence.Entities.User;
import com.Germina.Persistence.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody RegisterRequest request){
        try {
            // Intentar registrar al usuario
            AuthResponse authResponse = authService.register(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exceptions.EmailAlreadyExistsException e) {
            // Manejar email duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo electrónico ya está en uso.");
        } catch (Exceptions.IdentificationNumberAlreadyExistsException e) {
            // Manejar número de identificación duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El número de identificación ya está registrado.");
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor.");
        }
    }


    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request){
        try {
            // Intentar registrar al usuario
            AuthResponse authResponse = authService.registerAdmin(request);
            return ResponseEntity.ok(authResponse);

        } catch (Exceptions.EmailAlreadyExistsException e) {
            // Manejar email duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo electrónico ya está en uso.");
        } catch (Exceptions.IdentificationNumberAlreadyExistsException e) {
            // Manejar número de identificación duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El número de identificación ya está registrado.");
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor.");
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            // Intentar autenticar al usuario usando el servicio
            AuthResponse authResponse = authService.authenticate(request);

            // Obtener el usuario desde el repositorio
            User user = userRepository.findByMail(request.getUser());

            // Verificar el estado del usuario
            if (user.getState().equals("Inactivo")) {
                // Usuario bloqueado (DESACTIVADO)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La cuenta está Inactivada.");
            }
            // Si todo está bien, retornar la respuesta de autenticación
            return ResponseEntity.ok(authResponse);

        } catch (BadCredentialsException e) {
            // Manejar credenciales incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas. Verifique su usuario y contraseña.");

        }
         catch (DisabledException e) {
            // Manejar cuenta inactiva
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La cuenta está inactiva.");

        } catch (AuthenticationException e) {
            // Manejar errores generales de autenticación
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No se pudo autenticar. Por favor, verifique los datos.");

        } catch (Exception e) {
            // Manejar errores generales (problemas en el servidor, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor. Por favor, intente más tarde.");
        }
    }

//Buscar Un Usuario En Especifico
    @GetMapping("/editar")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authService.getCurrentUser(authentication));
    }


//Activa Un Usuario
    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestBody UserDTO userDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        userDTO.setMail(username);  // Assuming the UserDTO has a user field to store the username
        UserDTO updatedUser = userService.upda(userDTO);
        return ResponseEntity.ok(updatedUser);
    }



    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            if (jwtService.validateToken(token)) {
                String mail = jwtService.getUserName(token);
                userService.verifyUser(mail); // Implementa la lógica para marcar al usuario como verificado
                return ResponseEntity.ok("Correo electrónico verificado correctamente.");
            } else {
                return ResponseEntity.badRequest().body("Enlace de verificación inválido o expirado.");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.internalServerError().body("error"+e);
        }

    }


    @PutMapping("/activate-email")
    public ResponseEntity<String> activateEmail(@RequestParam("token") String token) {
        try {
            // Extraer los valores desde el token
            Map<String, Object> extractedValues = jwtService.extractClaimsFromToken(token);
            Long userId = (Long) extractedValues.get("userId");
            String newEmail = (String) extractedValues.get("newEmail");

            // Buscar el usuario por su ID
            Optional<User> optionalUser = userService.findByIds(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Actualizar el correo y el estado
                user.setMail(newEmail);
                user.setState("Activo"); // Puedes cambiar este valor según tu lógica

                // Guardar cambios en la base de datos
                userService.saves(user);

                return ResponseEntity.ok("Email actualizado y usuario activado.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o expirado.");
        }
    }

}
