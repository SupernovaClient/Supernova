package com.github.supernova.oauth.web;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AuthHTTPServer {

    public AuthHTTPServer(AuthHandler handler) {
        this.handler = handler;
    }

    AuthHandler handler;
    HttpServer server;
    boolean online = false;

    public void openServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/authenticate", handler);
            server.start();
            online = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeServer() {
        if(server != null) {
            server.stop(0);
            server = null;
            online = false;
        }
    }
}
