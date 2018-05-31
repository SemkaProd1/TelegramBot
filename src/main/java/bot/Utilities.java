package bot;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public String authorization(String token) {
        JSONObject json = null;
        try {
            json = readJsonFromUrl("https://api.telegra.ph/getAccountInfo?access_token=" + token + "&fields=[\"auth_url\"]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert json != null;
        json = (JSONObject) json.opt("result");
        return (String) json.get("auth_url");
    }

    public String regex(String s) {
        String regex = "^[0-9]{9}$";
        Pattern pat = Pattern.compile(regex);
        if (s.length() == 16) {
            s = s.substring(s.length() - 9);

            Matcher matcher = pat.matcher(s);
            if (matcher.find()) {
                return s;
            }
        } else s = "/start";
        return s;
    }
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}