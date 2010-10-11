/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.scgiserver;

import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.server.DispatchServer;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public class SCGIServer {

    private static ServerSocket providerSocket;

    public static void main(String[] args) {
        SCGIServer server = new SCGIServer();
        server.serve();

        
    }
    private String tr(String string) {
        I18n i18n = I18nFactory.getI18n(LoginPage.class, "i18n.Messages", java.util.Locale.FRANCE);
        return i18n.tr(string);
    }

    private void serve() {

        
        System.out.println(tr("This text will be translated"));
        System.out.println(tr("This one also"));

        

        try {
            System.err.println("Start BloatIt serveur");
            providerSocket = new ServerSocket(4000);

            while (true) {
                //Wait for connection
                System.out.println("Waiting for connection");

                // Load the SCGI headers.
                Socket clientSocket = providerSocket.accept();

                BufferedInputStream bis;
                bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
                Map<String, String> env = SCGI.parse(bis);
                //SCGI.parse(bis);
                // Read the body of the request.
                bis.read(new byte[Integer.parseInt(env.get("CONTENT_LENGTH"))]);

                System.err.println("post " + bis.toString());

                for (String key : env.keySet()) {
                    System.err.println("" + key + " -> " + env.get(key));
                }


                Map<String, String> query = parseQueryString(env.get("QUERY_STRING"));
                Map<String, String> post = parseQueryString(bis.toString());
                Map<String, String> cookies = parseCookiesString(env.get("HTTP_COOKIE"));
                List<String> preferredLangs = parseLanguageString(env.get("HTTP_ACCEPT_LANGUAGE"));


                DispatchServer dispatchServer = new DispatchServer(query, post, cookies, preferredLangs);
                
                try {
                    clientSocket.getOutputStream().write(dispatchServer.process().getBytes());
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }

                clientSocket.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

         
    }

    private Map<String, String> parseQueryString(String url) {
        Map<String, String> params = new HashMap<String, String>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                try {

                    String[] pair = param.split("=");
                    String key;
                    key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = URLDecoder.decode(pair[1], "UTF-8");

                    params.put(key, value);
                } catch (UnsupportedEncodingException ex) {
                    //TODO: log
                }
            }

        }
        return params;
    }

    private Map<String, String> parseCookiesString(String cookiesString) {
        Map<String, String> cookiesMap = new HashMap<String, String>();
        String[] cookies = cookiesString.split(";");
        for (String cookie : cookies) {
            String[] cookieParts = cookie.split("=");
            if (cookieParts.length == 2) {
                cookiesMap.put(strip(cookieParts[0]), strip(cookieParts[1]));
            }
        }
        return cookiesMap;
    }

    private static String strip(String s) {
        String good =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (good.indexOf(s.charAt(i)) >= 0) {
                result += s.charAt(i);
            }
        }
        return result;
    }

    private List<String> parseLanguageString(String languages) {
        return Arrays.asList(languages.split(","));
    }
}
