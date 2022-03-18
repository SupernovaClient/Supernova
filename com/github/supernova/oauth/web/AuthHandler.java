package com.github.supernova.oauth.web;

import com.github.supernova.oauth.util.WebUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class AuthHandler implements HttpHandler {

    public String authentication_code = "";
    public boolean updated = false;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "<p>idk what to put here but if you see this it is normal</p>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
        HashMap<String, String> query = WebUtil.getQuery(exchange.getRequestURI().getQuery());
        if(query.containsKey("code")) {
            authentication_code = query.get("code");
            updated = true;
        } else {
            authentication_code = "error";
            updated = true;
        }
    }
}
