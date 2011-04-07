package com.bloatit.framework;

import static com.bloatit.common.ConfigurationManager.SHARE_DIR;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;

/**
 * Everything must be final and non mutable to make sure there is no pb wit the
 * multi-thread.
 *
 * @author thomas
 */
public class FrameworkConfiguration extends ReloadableConfiguration {

    public static final FrameworkConfiguration configuration = new FrameworkConfiguration();

    private PropertiesRetriever properties;
    private String ressourcesDirStorage;
    private String mailDirTmp;
    private String mailDirSend;
    private String mailLogin;
    private String mailPassword;
    private String mailFrom;
    private String sessionDumpfile;
    private String mailSmtpHost;
    private String mailSmptSocketFactoryPort;
    private String mailSmtpSoketFactoryClass;
    private String mailSmtpAuth;
    private String mailSmtpPort;
    private int xcgiListenport;
    private int xcgiThreadsNumber;
    private String metaBugsDirStorage;

    private long sessionCleanTime;
    private long sessionDefaultDuration;
    private long sessionLoggedDuration;

    private FrameworkConfiguration() {
        super();
        loadConfiguration();
    }

    public static PropertiesRetriever getProperties() {
        return configuration.properties;
    }

    // -------------------------------------------
    // RESOURCES
    // -------------------------------------------
    
    public static String getRessourcesDirStorage() {
        return configuration.ressourcesDirStorage;
    }

    // -------------------------------------------
    // SESSIONS
    // -------------------------------------------
    
    public static String getSessionDumpfile() {
        return configuration.sessionDumpfile;
    }
    
    public static long getSessionCleanTime() {
        return configuration.sessionCleanTime;
    }
    
    public static long getSessionDefaultDuration() {
        return configuration.sessionDefaultDuration;
    }
    
    public static long getSessionLoggedDuration() {
        return configuration.sessionLoggedDuration;
    }

    // -------------------------------------------
    // MAIL
    // -------------------------------------------

    public static String getMailSmtpHost() {
        return configuration.mailSmtpHost;
    }

    public static String getMailSmptSocketFactoryPort() {
        return configuration.mailSmptSocketFactoryPort;
    }

    public static String getMailSmtpSoketFactoryClass() {
        return configuration.mailSmtpSoketFactoryClass;
    }

    public static String getMailSmtpAuth() {
        return configuration.mailSmtpAuth;
    }

    public static String getMailSmtpPort() {
        return configuration.mailSmtpPort;
    }

    public static String getMailDirTmp() {
        return configuration.mailDirTmp;
    }

    public static String getMailDirSend() {
        return configuration.mailDirSend;
    }

    public static String getMailLogin() {
        return configuration.mailLogin;
    }

    public static String getMailPassword() {
        return configuration.mailPassword;
    }

    public static String getMailFrom() {
        return configuration.mailFrom;
    }

    // -------------------------------------------
    // SERVER
    // -------------------------------------------
    
    public static int getXcgiListenport() {
        return configuration.xcgiListenport;
    }

    public static int getXcgiThreadsNumber() {
        return configuration.xcgiThreadsNumber;
    }
    
    // -------------------------------------------
    // BUGS
    // -------------------------------------------

    public static String getMetaBugsDirStorage() {
        return configuration.metaBugsDirStorage;
    }

    protected void loadConfiguration() {
        properties = ConfigurationManager.loadProperties("framework.properties");

        // Server
        xcgiThreadsNumber = properties.getInt("xcgi.threads.number");
        xcgiListenport = properties.getInt("xcgi.listenport");
        
        // Resources
        ressourcesDirStorage = SHARE_DIR + properties.getString("ressources.dir.storage", "file_storage");
        
        // Sessions.
        sessionDumpfile = SHARE_DIR + properties.getString("session.dumpfile", "sessions.dump");
        sessionCleanTime =  properties.getLong("session.clean.time");
        sessionDefaultDuration =  properties.getLong("session.default.duration");
        sessionLoggedDuration =  properties.getLong("session.logged.duration");
        
        // Bugs
        metaBugsDirStorage = SHARE_DIR + properties.getString("meta.bugs.dir.storage", "bug_storage");

        // Mail configuration
        mailDirTmp = SHARE_DIR + properties.getString("mail.dir.tmp", "temp_mail");
        mailDirSend = SHARE_DIR + properties.getString("mail.dir.send", "sent_mail");
        mailSmtpHost = properties.getString("mail.smtp.host");
        mailSmptSocketFactoryPort = properties.getString("mail.smtp.socketFactory.port");
        mailSmtpSoketFactoryClass = properties.getString("mail.smtp.socketFactory.class");
        mailSmtpAuth = properties.getString("mail.smtp.auth");
        mailSmtpPort = properties.getString("mail.smtp.port");
        mailLogin = properties.getString("mail.login");
        mailPassword = properties.getString("mail.password");
        mailFrom = properties.getString("mail.from");
    }

    public static void load() {
        configuration.loadConfiguration();
    }

    @Override
    public String getName() {
        return "Framework";
    }

    @Override
    protected void doReload() {
        configuration.loadConfiguration();
    }
}
