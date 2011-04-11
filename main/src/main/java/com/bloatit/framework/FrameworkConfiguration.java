package com.bloatit.framework;

import static com.bloatit.common.ConfigurationManager.SHARE_DIR;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicBoolean;

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

    // XCGI
    private int xcgiListenport;
    private int xcgiThreadsNumber;

    // DIRECTORIES
    private String documentationDir;
    private String wwwDir;
    private String resourcesDir;
    private String metaBugsDirStorage;

    // MAIL
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

    // SESSIONS
    private long sessionCleanTime;
    private long sessionDefaultDuration;
    private long sessionLoggedDuration;

    // JAVASCRIPT
    private String jsJquery;
    private String jsJqueryUi;
    private String jsFlexie;
    private String jsSelectivizr;
    private String jsDatePicker;
    private String jsShowdown;

    // OTHERS
    private AtomicBoolean htmlIndent;

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

    // ----------------------------------------------------------
    // DIRECTORIES
    // ----------------------------------------------------------
    public static String getDocumentationDir() {
        return configuration.documentationDir;
    }

    public static String getWwwDir() {
        return configuration.wwwDir;
    }

    /**
     * @return the path to the bloatitResourcesDir
     */
    public static String getResourcesDir() {
        return configuration.resourcesDir;
    }

    public static String getMetaBugsDirStorage() {
        return configuration.metaBugsDirStorage;
    }

    // ----------------------------------------------------------
    // JAVASCRIPT
    // ----------------------------------------------------------

    /**
     * @return the path to the jsJquery
     */
    public static String getJsJquery() {
        return getCommonsDir() + configuration.jsJquery;
    }

    /**
     * @return the path to the jsJqueryUi
     */
    public static String getJsJqueryUi() {
        return getCommonsDir() + configuration.jsJqueryUi;
    }

    /**
     * @return the path to the jsFlexie
     */
    public static String getJsFlexie() {
        return getCommonsDir() + configuration.jsFlexie;
    }

    /**
     * @return the path to the jsSelectivizr
     */
    public static String getJsSelectivizr() {
        return getCommonsDir() + configuration.jsSelectivizr;
    }

    /**
     * @return the path to the jsShowdown
     */
    public static String getJsShowdown() {
        return getCommonsDir() + configuration.jsShowdown;
    }

    /**
     * @return the path to the jsDatePicker
     */
    public static String getJsDatePicker(final String langCode) {
        return configuration.resourcesDir + "/" + langCode + configuration.jsDatePicker;
    }

    // ----------------------------------------------------------
    // OTHERS
    // ----------------------------------------------------------

    /**
     * @return <code>true</code> if html should be indented, <code>false</code>
     *         otherwise.
     */
    public static boolean isHtmlMinified() {
        return configuration.htmlIndent.get();
    }

    public static String getCommonsDir() {
        return configuration.resourcesDir + "/commons";
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
        sessionCleanTime = properties.getLong("session.clean.time");
        sessionDefaultDuration = properties.getLong("session.default.duration");
        sessionLoggedDuration = properties.getLong("session.logged.duration");

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

        // JAVASCRIPT
        jsJquery = properties.getString("bloatit.js.jquery");
        jsJqueryUi = properties.getString("bloatit.js.jqueryui");
        jsFlexie = properties.getString("bloatit.js.flexie");
        jsSelectivizr = properties.getString("bloatit.js.selectivizr");
        jsDatePicker = properties.getString("bloatit.js.datepicker");
        jsShowdown = properties.getString("bloatit.js.showdown");

        // DIRECTORIES
        documentationDir = properties.getString("bloatit.documentation.dir");
        wwwDir = properties.getString("bloatit.www.dir");
        resourcesDir = properties.getString("bloatit.resources.dir");

        // OTHERS
        htmlIndent = new AtomicBoolean(properties.getBoolean("bloatit.html.minify"));

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
