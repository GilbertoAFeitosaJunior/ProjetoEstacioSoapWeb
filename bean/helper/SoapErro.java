package mobi.stos.projetoestacio.bean.helper;

/**
 *
 * @author feito
 */
public class SoapErro {

    private int code;
    private String message;
    private String cause;

    public SoapErro() {
    }

    public SoapErro(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public SoapErro(int code, String message, String cause) {
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

}
