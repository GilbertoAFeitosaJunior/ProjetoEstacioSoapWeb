package mobi.stos.projetoestacio.exception;

public class LoginException extends Exception {

    public LoginException() {
        super("Esta conta de acesso não existe.");
    }

}
