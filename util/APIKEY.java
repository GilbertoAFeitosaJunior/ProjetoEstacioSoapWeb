package mobi.stos.projetoestacio.util;

public enum APIKEY {

    GOOGLE_SERVER_API_KEY("AIzaSyBhV6E-bXEpBVDWJ27z_W9dJEwZszRXC-0");
    private final String KEY;

    private APIKEY(String KEY) {
        this.KEY = KEY;
    }

    public String getKEY() {
        return KEY;
    }

}
