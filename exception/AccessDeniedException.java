package mobi.stos.projetoestacio.exception;

public class AccessDeniedException extends Exception {

    public AccessDeniedException() {
        super("Acesso não autorizado!");
    }
}
