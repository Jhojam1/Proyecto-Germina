package com.Germina.Domain.Services;

import com.Germina.Api.Config.Exception.Exceptions;
import com.Germina.Api.Config.models.AuthResponse;
import com.Germina.Api.Config.models.AuthenticationRequest;
import com.Germina.Api.Config.models.RegisterRequest;
import com.Germina.Persistence.Entities.Role;
import com.Germina.Persistence.Entities.User;
import com.Germina.Persistence.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Método para obtener la URL base de la aplicación (puede configurarse según el entorno)
    private String getBaseUrl() {
        // En producción, debes reemplazar localhost por tu dominio real
        return "http://localhost:5175"; // Usa tu URL de dominio real en producción
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByMail(request.getMail())) {
            throw new Exceptions.EmailAlreadyExistsException("El correo ya existe.");
        }

        // Verificar si el número de identificación ya existe
        if (userRepository.existsByNumberIdentification(request.getNumberIdentification())) {
            throw new Exceptions.IdentificationNumberAlreadyExistsException("El número de identificación ya está registrado.");
        }

        var user = User.builder()
                .fullName(request.getFullName())
                .numberIdentification(request.getNumberIdentification())
                .state("Inactivo")
                .mail(request.getMail())
                .role(Role.Usuario)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.genereteToken((UserDetails) user);
        // Obtener el enlace de activación con la URL base
        String baseUrl = getBaseUrl();
        String activationLink = baseUrl + "/activate/" + jwtToken;

        String mensajeHtml = String.format(
                "<h1>Hola %s</h1>" +
                        "<p>Gracias por iniciar el proceso de verificación de identidad en nuestra plataforma. Para completar la verificación, por favor haz clic en el siguiente enlace:" +
                        "<br /><br />" +
                        "<a href=\"%s\">Verificar Identidad</a>" +
                        "<br /><br />" +
                        "Este enlace te llevará a una página donde podrás confirmar tu identidad. Una vez completado este paso, tu verificación estará finalizada y podrás acceder a todos los beneficios de nuestra plataforma de manera segura." +
                        "<br /><br />" +
                        "Si tienes alguna pregunta o necesitas asistencia durante este proceso, no dudes en contactarnos respondiendo a este correo." +
                        "<br /><br />" +
                        "Gracias de nuevo por tu colaboración." +
                        "<br /><br />" +
                        "<br /><br />" +
                        "<br /><br />" +
                        "PQRSmart<br /><br />" ,
                user.getFullName(), activationLink
        );

        emailService.sendEmails(
                new String[]{user.getMail()},
                "Confirma tu correo",
                mensajeHtml
        );

        return AuthResponse.builder()
                .token(jwtToken).build();
    }

    // El método registerAdmin sigue el mismo patrón que register
    @Override
    public AuthResponse registerAdmin(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByMail(request.getMail())) {
            throw new Exceptions.EmailAlreadyExistsException("El correo ya existe.");
        }

        // Verificar si el número de identificación ya existe
        if (userRepository.existsByNumberIdentification(request.getNumberIdentification())) {
            throw new Exceptions.IdentificationNumberAlreadyExistsException("El número de identificación ya está registrado.");
        }

        var user = User.builder()
                .fullName(request.getFullName())
                .numberIdentification(request.getNumberIdentification())
                .state("Inactivo")
                .mail(request.getMail())
                .role(Role.Administrador)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.genereteToken((UserDetails) user);
        // Obtener el enlace de activación con la URL base
        String baseUrl = getBaseUrl();
        String activationLink = baseUrl + "/activate/" + jwtToken;

        String mensajeHtml = String.format(
                "<h1>Hola %s</h1>" +
                        "<p>Gracias por iniciar el proceso de verificación de identidad en nuestra plataforma. Para completar la verificación, por favor haz clic en el siguiente enlace:" +
                        "<br /><br />" +
                        "<a href=\"%s\">Verificar Identidad</a>" +
                        "<br /><br />" +
                        "Este enlace te llevará a una página donde podrás confirmar tu identidad. Una vez completado este paso, tu verificación estará finalizada y podrás acceder a todos los beneficios de nuestra plataforma de manera segura." +
                        "<br /><br />" +
                        "Si tienes alguna pregunta o necesitas asistencia durante este proceso, no dudes en contactarnos respondiendo a este correo." +
                        "<br /><br />" +
                        "Gracias de nuevo por tu colaboración." +
                        "<br /><br />" +
                        "<br /><br />" +
                        "<br /><br />" +
                        "PQRSmart<br /><br />" ,
                user.getFullName(), activationLink
        );
        log.info("El enlace de activación generado es: " + activationLink);

        emailService.sendEmails(
                new String[]{user.getMail()},
                "Confirma tu correo",
                mensajeHtml
        );

        return AuthResponse.builder()
                .token(jwtToken).build();
    }

    // Método para la autenticación
    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUser(),
                        request.getPassword()
                )
        );
        UserDetails user = userRepository.findUserByMail(request.getUser()).orElseThrow();
        String jwtToken = jwtService.genereteToken(user);
        List<String> roles = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(jwtToken)
                .authorities(roles)
                .build();
    }

    // Obtener el usuario actual autenticado
    @Override
    public User getCurrentUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
