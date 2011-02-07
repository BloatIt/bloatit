package com.bloatit.framework.fcgiserver;

/**
 * <p>
 * How to run me ?
 * </p>
 * <p>
 * Create a configuration file for fcgi with : <br />
 * 
 * <pre>
 * server.modules += ( "mod_fastcgi" )
 * fastcgi.server = (
 *     "bloatit" =>
 *     (
 *         (
 *         "host" => "127.0.0.1",
 *         "port" => 4000,
 *         "check-local" => "disable",
 *         )
 *     ))
 * </pre>
 * 
 * </p>
 * <p>
 * Launch the VM with the option : -DFCGI_PORT=4000 (<b>Note : </b> it is a VM
 * option not a program option).
 * </p>
 * <p>
 * Enjoy ...
 * </p>
 */
public class TinyFCGI {
    public static void main(String args[]) {
        int count = 0;
        while (new FCGIInterface().FCGIaccept() >= 0) {
            count++;
            System.out.println("Content-type: text/html\r\n\r\n");
            System.out.println("<title>FastCGI Hello! (Java)</title>");
            System.out.println("<h1>FastCGI Hello! (Java)</h1>");
            System.out.println("request number " + count + " running on host <i>" + System.getProperty("SERVER_NAME") + "</i>");
        }
    }
}
