package com.bloatit.framework.social;

import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;

import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;

/**
 * A class that allows sending messages to a micro blogging service
 */
public class MicroBlog {
    private final String url;
    private final String login;
    private final String password;

    private static final MultiThreadedHttpConnectionManager connManager = new MultiThreadedHttpConnectionManager();
    private final String realm;

    /**
     * Creates a new connection with a micro blogging service.
     * 
     * @param url The url to the micro blogging service
     * @param login the login used to connect to the service
     * @param password the password used to connect to the service
     * @param realm the realm used to authenticate to the service. For example
     *            <i>identi.ca</i> or <i>twitter.com</i>
     */
    public MicroBlog(String url, String login, String password, String realm) {
        this.url = url;
        this.login = login;
        this.password = password;
        this.realm = realm;
    }

    /**
     * Posts <code>message</code> to the micro blogging service.
     * <p>
     * Note: There is no guarantee that the message will actually be posted. If
     * a similar message has been posted recently, it likely won't be sent
     * </p>
     * 
     * @param message the message to post
     * @throws ExternalErrorException if a connection error happens
     */
    public void post(String message) {
        HttpClient client = new HttpClient(connManager);
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(login, password);
        client.getState().setCredentials(new AuthScope(realm, 80, AuthScope.ANY_REALM), defaultcreds);

        PostMethod post = new PostMethod(url);
        post.setParameter("status", message);

        try {
            client.executeMethod(post);
        } catch (IOException e) {
            throw new ExternalErrorException("Cannot send message to micro blogging service [" + realm + "]", e);
        } finally {
            post.releaseConnection();
        }
    }

    public static void main(String... args) throws HttpException, IOException {
        MicroBlog identica = new MicroBlog("http://identi.ca/api/statuses/update.xml", "elveos", "EH9iygELxdwrz", "identi.ca");
        identica.post("This is a new message. It's fantastic !");
    }
}
