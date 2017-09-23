package mobi.stos.projetoestacio.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

public class Captcha {

    public static boolean solveCaptcha(HttpServletRequest request) {
        try {
            Properties properties = new Properties();
            properties.load(Captcha.class.getClassLoader().getResourceAsStream("system.properties"));

            URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");

            StringBuilder params = new StringBuilder();
            params.append("secret=").append(properties.getProperty("captcha.privatekey")).append("&");
            params.append("response=").append(request.getParameter("g-recaptcha-response")).append("&");
            params.append("remoteip=").append(request.getRemoteAddr());

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(params.toString());
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                JSONObject json = new JSONObject(Util.inputStreamToString(connection.getInputStream()));
                System.out.println(json.toString());
                return json.getBoolean("success");
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

}
