package br.com.lrbarros.assinador.exception;

public class CryptoException extends RuntimeException {
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
