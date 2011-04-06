package com.bloatit.framework;

import static com.bloatit.common.ConfigurationManager.SHARE_DIR;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.MasterConfiguration;

/**
 * Everything must be final and non mutable to make sure there is no pb wit the
 * multi-thread.
 * 
 * @author thomas
 */
public class FrameworkConfiguration extends MasterConfiguration {

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
    private int mailSmptSocketFactoryPort;
    private String mailSmtpSoketFactoryClass;
    private Boolean mailSmtpAuth;
    private int mailSmtpPort;
    private int xcgiListenport;
    private int xcgiThreadsNumber;
    private String metaBugsDirStorage;

    private FrameworkConfiguration() {
        super();
        loadConfiguration();
    }

    public static String getRessourcesDirStorage() {
        return configuration.ressourcesDirStorage;
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

    public static String getSessionDumpfile() {
        return configuration.sessionDumpfile;
    }

    public static PropertiesRetriever getProperties() {
        return configuration.properties;
    }

    public static String getMailSmtpHost() {
        return configuration.mailSmtpHost;
    }

    public static int getMailSmptSocketFactoryPort() {
        return configuration.mailSmptSocketFactoryPort;
    }

    public static String getMailSmtpSoketFactoryClass() {
        return configuration.mailSmtpSoketFactoryClass;
    }

    public static Boolean getMailSmtpAuth() {
        return configuration.mailSmtpAuth;
    }

    public static int getMailSmtpPort() {
        return configuration.mailSmtpPort;
    }

    public static int getXcgiListenport() {
        return configuration.xcgiListenport;
    }

    public static int getXcgiThreadsNumber() {
        return configuration.xcgiThreadsNumber;
    }

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
        // Bugs
        metaBugsDirStorage = SHARE_DIR + properties.getString("meta.bugs.dir.storage", "bug_storage");

        // Mail configuration
        mailDirTmp = SHARE_DIR + properties.getString("mail.dir.tmp", "temp_mail");
        mailDirSend = SHARE_DIR + properties.getString("mail.dir.send", "sent_mail");
        mailSmtpHost = properties.getString("mail.smtp.host");
        mailSmptSocketFactoryPort = properties.getInt("mail.smtp.socketFactory.port");
        mailSmtpSoketFactoryClass = properties.getString("mail.smtp.socketFactory.class");
        mailSmtpAuth = properties.getBoolean("mail.smtp.auth");
        mailSmtpPort = properties.getInt("mail.smtp.port");
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
