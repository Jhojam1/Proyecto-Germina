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

    // Excepción para cuando se excede la hora límite
    public static class CutoffTimeExceededException extends RuntimeException {
        public CutoffTimeExceededException(String message) {
            super(message);
        }
    }

    // Excepción para cuando no se encuentra el plato
    public static class DishNotFoundException extends RuntimeException {
        public DishNotFoundException(String message) {
            super(message);
        }
    }

    // Excepción para cuando se alcanza el límite diario de pedidos
    public static class DailyOrderLimitExceededException extends RuntimeException {
        public DailyOrderLimitExceededException(String message) {
            super(message);
        }
    }
}
