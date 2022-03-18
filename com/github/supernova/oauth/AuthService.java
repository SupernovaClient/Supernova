package com.github.supernova.oauth;

import com.github.supernova.oauth.util.WebUtil;
import com.github.supernova.oauth.web.AuthHTTPServer;
import com.github.supernova.oauth.web.AuthHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class AuthService {

    public AuthService() {
        handler = new AuthHandler();
        server = new AuthHTTPServer(handler);
    }

    private final CloseableHttpClient client = HttpClients.createDefault();
    private final JsonParser parser = new JsonParser();

    private final String app_id = "22d5608c-7892-4b7e-b5e6-55bc88af50e6";
    private final String app_secret = "Lt47Q~UJvSDFLIWFlz4z8.cVix1I0URMXU8UE";

    private final AuthHandler handler;
    private final AuthHTTPServer server;

    public String responseMessage = "";
    public Session session;

    public int redirectPort = 8000;

    private boolean grabbedToken = false;

    public void doAuth(String refresh_token) {
        server.openServer();
        auth(refresh_token);
        server.closeServer();
        LogManager.getLogger().info(responseMessage);
    }

    private void auth(String refreshToken) {
        if(refreshToken == null || refreshToken.equals("")) {
            WebUtil.browse("https://login.live.com/oauth20_authorize.srf?"+getQuery(app_id));
            responseMessage = "Waiting for user authentication";
        }
        String authenticationToken = refreshToken;
        long initTime = System.currentTimeMillis();
        while (responseMessage.equals("Waiting for user authentication")) {
            if(System.currentTimeMillis() - initTime > 100) {
                LogManager.getLogger().info("Waiting for auth");
                if (handler.updated) {
                    LogManager.getLogger().info("Updated auth");
                    authenticationToken = handler.authentication_code;
                    handler.updated = false;
                    grabbedToken = true;
                    break;
                }
                initTime = System.currentTimeMillis();
            }
        }
        //Microsoft Live Auth
        String[] liveAuth = liveAuth(authenticationToken, grabbedToken);
        if(liveAuth[0].equals("FAILED")) {
            LogManager.getLogger().error(liveAuth[1]);
            responseMessage = "Live Authentication Failed";
            return;
        }

        //Xbox Live Auth
        String[] xboxAuth = xboxAuth(liveAuth[0]);
        if(xboxAuth[0].equals("FAILED")) {
            LogManager.getLogger().error(xboxAuth[1]);
            responseMessage = "Xbox Authentication Failed";
            return;
        }
        //XSTS Auth
        String[] xstsAuth = xstsAuth(xboxAuth[0]);
        if(xstsAuth[0].equals("FAILED")) {
            LogManager.getLogger().error(xstsAuth[1]);
            responseMessage = "XSTS Authentication Failed";
            return;
        }
        //Minecraft Auth
        String[] mcAuth = mcAuth(xstsAuth[0], xstsAuth[1]);
        if(mcAuth[0].equals("FAILED")) {
            LogManager.getLogger().error(mcAuth[1]);
            responseMessage = "Minecraft Authentication Failed";
        }
        //Create Session
        String[] user_data = getUser(mcAuth[1]);
        this.session = new Session(user_data[1], user_data[0], mcAuth[1], "mojang");
        Minecraft.getMinecraft().session = this.session;
        responseMessage = "Logged in as user "+this.session.getUsername();
    }

    private String getQuery(String app_id) {
        HashMap<String, String> query = new HashMap<>();
        query.put("response_type", "code");
        query.put("client_id", app_id);
        query.put("redirect_uri", "http://localhost:8000/authenticate");
        query.put("scope", "XboxLive.signin+offline_access");
        return WebUtil.getQuery(query);
    }
    private String[] getUser(String access_token) {
        String results = "null";
        try {
            results = get("https://api.minecraftservices.com/minecraft/profile", access_token);
            JsonObject json = (JsonObject) parser.parse(results);
            return new String[]{json.get("id").getAsString(), json.get("name").getAsString()};
        } catch (Exception e) {
            return new String[] {"FAILED", results};
        }
    }
    private String[] liveAuth(String access_token, boolean refreshToken) {
        refreshToken = false;
        HashMap<String, String> data = new HashMap<>();
        data.put("client_id", app_id);

        if(refreshToken) {
            data.put("refresh_token", access_token);
        } else {
            data.put("code", access_token);
        }
        data.put("grant_type", refreshToken ? "refresh_token" : "authorization_code");
        data.put("redirect_uri", "http://localhost:8000/authenticate");
        data.put("scope", "XboxLive.signin offline_access");
        data.put("client_secret", app_secret);
        data.put("response_type", "code");

        String results = "null";
        try {
            results = post("https://login.live.com/oauth20_authorize.srf", data);
            JsonObject json = (JsonObject) parser.parse(results);
            return new String[] {json.get("access_token").getAsString(), json.get("refresh_token").getAsString()};
        } catch (Exception e) {
            return new String[] {"FAILED", results};
        }
    }
    private String[] xboxAuth(String live_token) {
        JsonObject postData = new JsonObject();
        JsonObject properties = new JsonObject();
        properties.addProperty("AuthMethod", "RPS");
        properties.addProperty("SiteName", "user.auth.xboxlive.com");
        properties.addProperty("RpsTicket", "d="+live_token);
        postData.add("Properties", properties);
        postData.addProperty("RelyingParty", "http://auth.xboxlive.com");
        postData.addProperty("TokenType", "JWT");
        String results = "null";
        try {
            results = post("https://user.auth.xboxlive.com/user/authenticate", postData.toString());
            JsonObject result = (JsonObject) parser.parse(results);
            return new String[] {result.get("Token").getAsString(),
                    result.get("DisplayClaims").getAsJsonObject()
                            .get("xui").getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("uhs").getAsString()};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[] {"FAILED", results};
        }
    }
    private String[] xstsAuth(String xbox_token) {
        JsonArray userTokens = new JsonArray();
        userTokens.add(new JsonPrimitive(xbox_token));

        JsonObject properties = new JsonObject();
        properties.addProperty("SandboxId", "RETAIL");
        properties.add("UserTokens", userTokens);

        JsonObject postData = new JsonObject();
        postData.add("Properties", properties);
        postData.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        postData.addProperty("TokenType", "JWT");

        String results = "null";
        try {
            results = post("https://xsts.auth.xboxlive.com/xsts/authorize", postData.toString());
            JsonObject result = (JsonObject) parser.parse(results);
            return new String[] {result.get("Token").getAsString(),
                    result.get("DisplayClaims").getAsJsonObject()
                            .get("xui").getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("uhs").getAsString()};
        } catch (Exception e) {
            return new String[] {"FAILED", results};
        }
    }
    private String[] mcAuth(String xsts_token, String uhs_token) {
        JsonObject postData = new JsonObject();
        postData.addProperty("identityToken", "XBL3.0 x="+uhs_token+";"+xsts_token);

        String results = "null";
        try {
            results = post("https://api.minecraftservices.com/authentication/login_with_xbox", postData.toString());
            JsonObject result = (JsonObject) parser.parse(results);
            return new String[] {"BALLS", result.get("access_token").getAsString()};
        } catch (Exception e) {
            return new String[] {"FAILED", results};
        }

    }


    private String post(String url, HashMap<String,String> post_data) throws Exception {
        /*
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        for(Map.Entry<String, String> entry : post_data.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8);

         */
        URL postUrl = new URL(url+"?"+WebUtil.getQuery(post_data));
        System.out.println(postUrl.toString());
        HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", "0");
        int response = conn.getResponseCode();
        StringBuilder builder = new StringBuilder();
        if(response >= 200 && response <= 205) {
            new BufferedReader(new InputStreamReader(conn.getInputStream())).lines().forEach(builder::append);
        } else {
            new BufferedReader(new InputStreamReader(conn.getErrorStream())).lines().forEach(builder::append);
        }
        System.out.println(builder);
        return builder.toString();
    }
    private String post(String url, String post_data) throws Exception {
        URL postUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.connect();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(post_data);
        bw.flush();
        bw.close();

        int response = conn.getResponseCode();
        StringBuilder builder = new StringBuilder();
        if(response >= 200 && response <= 205) {
            new BufferedReader(new InputStreamReader(conn.getInputStream())).lines().forEach(builder::append);
        } else {
            System.out.println(conn.getResponseCode());
            System.out.println(conn.getResponseMessage());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            if(br.ready()) {
                br.lines().forEach(builder::append);
            }
        }

        System.out.println(builder);
        return builder.toString();
    }
    private String get(String url, String header) throws Exception {
        HttpGet get = new HttpGet(url);
        get.setHeader("Authorization", "Bearer "+ header);

        CloseableHttpResponse response;
        response = client.execute(get);

        return EntityUtils.toString(response.getEntity());
    }

}
