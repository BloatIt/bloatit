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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Serve {

    private static ServerSocket providerSocket;

    public static void main(String[] args) {

        serve();
    }

    private static void serve() {
        try {
            System.err.println("Start BloatIt serveur");
            providerSocket = new ServerSocket(4000);
            
            while(true) {
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

                System.err.println("post "+bis.toString());
                for(String key : env.keySet()) {
                    System.err.println(""+key+" -> "+env.get(key));
                }

                String hello = "ContentType : text/html \r\n\r\nHello BloatIt !";

                clientSocket.getOutputStream().write(hello.getBytes());

                clientSocket.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
