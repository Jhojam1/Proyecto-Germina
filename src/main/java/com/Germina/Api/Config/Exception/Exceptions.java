package com.Germina.Api.Config.Exception;

public class Exceptions {


    // Excepción para cuando el email ya existe
    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    // Excepción para cuando el número de identificación ya existe
    public static class IdentificationNumberAlreadyExistsException extends RuntimeException {
        public IdentificationNumberAlreadyExistsException(String message) {
            super(message);
        }
    }


}
