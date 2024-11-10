package com.Germina.Domain.Services;



import com.Germina.Api.Config.models.AuthResponse;
import com.Germina.Api.Config.models.AuthenticationRequest;
import com.Germina.Api.Config.models.RegisterRequest;
import com.Germina.Persistence.Entities.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthResponse register (RegisterRequest Request );
    AuthResponse registerAdmin (RegisterRequest Request );
    AuthResponse authenticate (AuthenticationRequest Request );
    User getCurrentUser(Authentication authentication);
}
