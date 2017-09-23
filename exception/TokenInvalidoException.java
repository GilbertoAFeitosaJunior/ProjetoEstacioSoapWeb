package mobi.stos.projetoestacio.exception;

public class TokenInvalidoException extends Exception {

    public TokenInvalidoException() {
        super("O token é inválido ou já está expirado, solicite outro token.");
    }
    
    
    
}
