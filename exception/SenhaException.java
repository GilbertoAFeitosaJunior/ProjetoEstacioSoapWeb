package mobi.stos.projetoestacio.exception;

public class SenhaException extends Exception {

    public SenhaException() {
        super("Essa senha está incorreta. Verifique se você está usando a senha da sua conta do projetoestacio.");
    }

}
