//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework;

import static com.bloatit.common.ConfigurationManager.SHARE_DIR;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;
import com.bloatit.framework.mailsender.RetryPolicy;
import com.bloatit.framework.social.MicroBlogManager;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicBoolean;

/**
 * Everything must be final and non mutable to make sure there is no pb wit the
 * multi-thread.
 * 
 * @author thomas
 */
public class FrameworkConfiguration extends ReloadableConfiguration {

    private static final FrameworkConfiguration configuration = new FrameworkConfiguration();

    private PropertiesRetriever properties;

    private String ressourcesDirStorage;

    // XCGI
    private int xcgiListenport;
    private String xcgiListenAddress;
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
    private RetryPolicy mailRetryPolicy;

    // SESSIONS
    private long sessionCleanTime;
    private long sessionDefaultDuration;
    private long sessionLoggedDuration;

    // CSS
    private String cssShowdown;

    // JAVASCRIPT
    private String jsJquery;
    private String jsJqueryUi;
    private String jsFlexie;
    private String jsSelectivizr;
    private String jsDatePicker;
    private String jsShowdown;
    private String jsShowdownUi;

    private String bloatitLibravatarURI;
    private ResourceFinder finder;

    // OTHERS
    private AtomicBoolean htmlIndent;
    private AtomicBoolean https;
    private int memoryCacheMaxSize;
    private String imgFavicon;
    private String anonymousUserTokenClass;
    private MicroBlogManager microBlogs;

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

    public static RetryPolicy getMailRetryPolicy() {
        return configuration.mailRetryPolicy;
    }

    // -------------------------------------------
    // SERVER
    // -------------------------------------------

    public static int getXcgiListenport() {
        return configuration.xcgiListenport;
    }

    public static String getXcgiListenAddress() {
        return configuration.xcgiListenAddress;
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
    // CSS
    // ----------------------------------------------------------

    public static String getCssShowdown() {
        return configuration.finder.find(getCommonsDir() + configuration.cssShowdown);
    }

    // ----------------------------------------------------------
    // JAVASCRIPT
    // ----------------------------------------------------------

    /**
     * @return the path to the jsJquery
     */
    public static String getJsJquery() {
        return configuration.finder.find(getCommonsDir() + configuration.jsJquery);
    }

    /**
     * @return the path to the jsJqueryUi
     */
    public static String getJsJqueryUi() {
        return configuration.finder.find(getCommonsDir() + configuration.jsJqueryUi);
    }

    /**
     * @return the path to the jsFlexie
     */
    public static String getJsFlexie() {
        return configuration.finder.find(getCommonsDir() + configuration.jsFlexie);
    }

    /**
     * @return the path to the jsSelectivizr
     */
    public static String getJsSelectivizr() {
        return configuration.finder.find(getCommonsDir() + configuration.jsSelectivizr);
    }

    /**
     * @return the path to the jsShowdown
     */
    public static String getJsShowdown() {
        return configuration.finder.find(getCommonsDir() + configuration.jsShowdown);
    }

    /**
     * @return the path to the jsShowdown
     */
    public static String getJsShowdownUi() {
        return configuration.finder.find(getCommonsDir() + configuration.jsShowdownUi);
    }

    /**
     * @return the path to the jsDatePicker
     */
    public static String getJsDatePicker(final String langCode) {
        return configuration.finder.find(configuration.resourcesDir + "/" + langCode + configuration.jsDatePicker);
    }

    /**
     * @return the imgFavicon
     */
    public static String getImgFavicon() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgFavicon);
    }

    public static String getLibravatarURI() {
        return configuration.bloatitLibravatarURI;
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

    public static boolean isHttpsEnabled() {
        return configuration.https.get();
    }

    public static int getMemoryCacheMaxSize() {
        return configuration.memoryCacheMaxSize;
    }

    public static String getAnonymousUserTokenClass() {
        return configuration.anonymousUserTokenClass;
    }

    public static String getCommonsDir() {
        return configuration.resourcesDir + "/commons";
    }

    public static MicroBlogManager getMicroBlogs() {
        return configuration.microBlogs;
    }

    private void loadConfiguration() {
        
        properties = ConfigurationManager.loadProperties("framework.properties");

        // Server
        xcgiThreadsNumber = properties.getInt("xcgi.threads.number");
        xcgiListenport = properties.getInt("xcgi.listenport");
        xcgiListenAddress = properties.getString("xcgi.listenAddress");

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
        mailRetryPolicy = new RetryPolicy(properties.getString("mail.retry.policy", "[15s, 1min, 2min, 5min, 5min, 5min, 10min, 20min, ...]"));

        // CSS
        cssShowdown = properties.getString("bloatit.css.showdown");

        // JAVASCRIPT
        jsJquery = properties.getString("bloatit.js.jquery");
        jsJqueryUi = properties.getString("bloatit.js.jqueryui");
        jsFlexie = properties.getString("bloatit.js.flexie");
        jsSelectivizr = properties.getString("bloatit.js.selectivizr");
        jsDatePicker = properties.getString("bloatit.js.datepicker");
        jsShowdown = properties.getString("bloatit.js.showdown");
        jsShowdownUi = properties.getString("bloatit.js.showdown.ui");

        bloatitLibravatarURI = properties.getString("bloatit.libravatar.uri");

        // DIRECTORIES
        documentationDir = properties.getString("bloatit.documentation.dir");
        wwwDir = properties.getString("bloatit.www.dir");
        resourcesDir = properties.getString("bloatit.resources.dir");

        // OTHERS
        htmlIndent = new AtomicBoolean(properties.getBoolean("bloatit.html.minify"));
        https = new AtomicBoolean(properties.getBoolean("bloatit.https"));
        memoryCacheMaxSize = properties.getInt("bloatit.memory.cache.max.size");
        imgFavicon = properties.getString("bloatit.img.favicon");
        anonymousUserTokenClass = properties.getString("bloatit.anonymousUserToken.class");
        microBlogs = new MicroBlogManager(properties.getStringArray("micro.blogs"), properties.getString("micro.blogs.password"));
        
        finder = new ResourceFinder(wwwDir);
    }

    protected static void load() {
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
