package mobi.stos.projetoestacio.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class FCM {

    private static final String GOOGLE_SERVER_API_KEY = "AIzaSyDkGRYwgOps1biUVeZ_o87DSVEb74KazEo";

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String ACTION = "action";
    public static final String IMAGE = "image";
    public static final String BADGE = "badge";

    private final JSONObject data;
    private boolean padrao = false;

    public FCM() {
        data = new JSONObject();
    }

    public void addParam(String key, Object value) {
        data.put(key, String.valueOf(value));
    }

    public void setPadrao(boolean padrao) {
        this.padrao = padrao;
    }

    /**
     * Enviar mensagem FCM para o usuários registrados.
     *
     * @param token String
     * @return boolean enviado com sucesso = true
     */
    public ReturnFCM send(String token) {
        try {
            String mUrl = "https://fcm.googleapis.com/fcm/send";

            URL url = new URL(mUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true); // forçar conexão POST
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "key=" + GOOGLE_SERVER_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");

            JSONObject json = new JSONObject();
            if (padrao) {
                JSONObject notification = new JSONObject();
                notification.put(TITLE, data.get(TITLE));
                notification.put(BODY, data.get(BODY));
                notification.put(BADGE, data.get(BADGE));
                json.put("notification", notification);
            }
            json.put("data", data);
            json.put("priority", "high");
            json.put("to", token);

            System.out.println("json: " + json.toString());

            byte[] bytes = json.toString().getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(bytes.length));

            try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                dos.write(bytes, 0, bytes.length);
                dos.flush();
            }

            InputStream is;
            int responseCode = connection.getResponseCode();
            if (responseCode < 300) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            if (is != null) {
                String iss = this.inputStreamToString(is);
                if (StringUtils.isNotEmpty(iss)) {
                    JSONObject jSONObject = new JSONObject(iss);
                    int failure = jSONObject.getInt("failure");
                    if (failure == 0) {
                        return ReturnFCM.Ok;
                    } else {
                        JSONArray results = jSONObject.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject jResult = results.getJSONObject(i);
                            return ReturnFCM.valueOf(jResult.getString("error"));
                        }
                    }
                }
            }

            return ReturnFCM.TryAgain;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnFCM.TryAgain;
        }
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public enum ReturnFCM {

        Ok(false),
        MissingRegistration(true),
        InvalidRegistration(true),
        NotRegistered(true),
        InvalidPackageName(false),
        MismatchSenderId(false),
        MessageTooBig(false),
        InvalidDataKey(false),
        InvalidTtl(false),
        Unavailable(false),
        InternalServerError(false),
        DeviceMessageRate(false),
        TopicsMessageRate(false),
        TryAgain(false);

        private final boolean deleteToken;

        private ReturnFCM(boolean deleteToken) {
            this.deleteToken = deleteToken;
        }

        public boolean isDeleteToken() {
            return deleteToken;
        }

    }

}
