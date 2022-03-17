package io.github.nevalackin.oauth.util;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class WebUtil {
    public static void browse(String url) {
        try {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public static HashMap<String, String> getQuery(String queryData) {
        HashMap<String, String> query = new HashMap<>();
        if(!queryData.contains("=")) {
            return query;
        }
        if(queryData.contains("&")) {
            for(String splitQuery : queryData.split("&")) {
                String[] data = splitQuery.split("=");
                if(data.length > 1) {
                    query.put(data[0], data[1]);
                }
            }
        } else {
            String[] data = queryData.split("=");
            if(data.length > 1) {
                query.put(data[0], data[1]);
            }
        }
        return query;
    }
    public static String getQuery(HashMap<String,String> queryMap) {
        StringBuilder query = new StringBuilder();
        queryMap.forEach((k,v) -> {
            query.append(k).append("=").append(v).append("&");
        });
        query.deleteCharAt(query.length()-1);
        return query.toString();
    }
}
