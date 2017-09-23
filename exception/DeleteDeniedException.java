package mobi.stos.projetoestacio.exception;

public class DeleteDeniedException extends Exception {

    public DeleteDeniedException() {
        super("Exclusão não permitida.");
    }

}
