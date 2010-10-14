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

import com.bloatit.web.server.DispatchServer;
import com.bloatit.common.FatalErrorException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SCGIServer {

    private static ServerSocket providerSocket;

    public static void main(String[] args) {
        SCGIServer server = new SCGIServer();
        server.serve();
    }

    private void serve() {

        //Find a better way to clean the socket
        try {
            Socket cleanSocket = new Socket("127.0.0.1",4000);
            cleanSocket.close();
        } catch (IOException ex) {
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(SCGIServer.class.getName()).log(Level.SEVERE, null, ex);
        }

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

                byte[] postBytes = new byte[Integer.parseInt(env.get("CONTENT_LENGTH"))];
                bis.read(postBytes);

                /*
                for (String key : env.keySet()) {
                    System.err.println("" + key + " -> " + env.get(key));
                }
                */

                Map<String, String> query = parseQueryString(env.get("QUERY_STRING"));
                Map<String, String> post = parseQueryString(new String(postBytes));
                Map<String, String> cookies = parseCookiesString(env.get("HTTP_COOKIE"));
                List<String> preferredLangs = parseLanguageString(env.get("HTTP_ACCEPT_LANGUAGE"));


                DispatchServer dispatchServer = new DispatchServer(query, post, cookies, preferredLangs);

                String display;
                try {
                    display = dispatchServer.process();
                } catch (FatalErrorException e) {
                    display = "Content-type: text/html\r\n\r\n"+e.toString()+" :\n";
                    for(StackTraceElement s : e.getStackTrace()){
                       display+="<p>\t"+s+"</p>\n";
                    }
                    // TODO : Log
                    // TODO Debug Only
                    // TODO : print stack trace
                } catch (Exception e){
                    // Protects the server
                    display = "Content-type: text/html\r\n\r\n"+e.toString()+" :\n";
                    for(StackTraceElement s : e.getStackTrace()){
                       display+="<p>\t"+s+"</p>\n";
                    }
                }

                try {
                    clientSocket.getOutputStream().write(display.getBytes());
                } catch (Exception e){
                    e.printStackTrace();
                    // TODO Log
                }finally{
                    clientSocket.close();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private Map<String, String> parseQueryString(String url) {
        Map<String, String> params = new HashMap<String, String>();
        for (String param : url.split("&")) {
            try {

                String[] pair = param.split("=");
                String key;
                if(pair.length >= 2) {
                    key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = URLDecoder.decode(pair[1], "UTF-8");

                    params.put(key, value);
                }
            } catch (UnsupportedEncodingException ex) {
                //TODO: log
            }
        }


        return params;
    }

    private Map<String, String> parseCookiesString(String cookiesString) {
        Map<String, String> cookiesMap = new HashMap<String, String>();

        if(cookiesString != null){
            String[] cookies = cookiesString.split(";");
            for (String cookie : cookies) {
                String[] cookieParts = cookie.split("=");
                if (cookieParts.length == 2) {
                    cookiesMap.put(cookieParts[0].trim(), cookieParts[1].trim());
                }
            }
        }
        return cookiesMap;
    }

    private List<String> parseLanguageString(String languages) {
        return Arrays.asList(languages.split(","));
    }
}
