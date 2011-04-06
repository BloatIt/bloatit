package com.bloatit.web;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicBoolean;

public class WebConfiguration extends ReloadableConfiguration {
    public static final WebConfiguration configuration = new WebConfiguration();
    private PropertiesRetriever properties;

    // DIRECTORIES
    private String documentationDir;
    private String wwwDir;
    private String resourcesDir;

    // OTHERS
    private AtomicBoolean htmlIndent;

    // CSS
    private String css;
    private String cssDatePicker;

    // IMAGES
    private String imgLogo;
    private String imgPresentation;
    private String imgMoneyDown;
    private String imgMoneyDownSmall;
    private String imgMoneyUp;
    private String imgMoneyUpSmall;
    private String imgNoAvatar;
    private String imgValidIcon;
    private String imgSoftwareNoLogo;

    // JAVASCRIPT
    private String jsJquery;
    private String jsJqueryUi;
    private String jsFlexie;
    private String jsSelectivizr;
    private String jsDatePicker;

    private WebConfiguration() {
        super();
        loadConfiguration();
    }

    public static WebConfiguration getConfiguration() {
        return configuration;
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

    // ----------------------------------------------------------
    // CSS
    // ----------------------------------------------------------

    /**
     * @return the path to the css
     */
    public static String getCss() {
        return getCommonsDir() + configuration.css;
    }

    public static String getCssDatePicker() {
        return getCommonsDir() + configuration.cssDatePicker;
    }

    // ----------------------------------------------------------
    // IMAGES
    // ----------------------------------------------------------

    /**
     * @return the path to the imgLogo
     */
    public static String getImgLogo() {
        return getCommonsDir() + configuration.imgLogo;
    }

    /**
     * @return the path to the imgPresentation
     */
    public static String getImgPresentation() {
        return getCommonsDir() + configuration.imgPresentation;
    }

    /**
     * @return the imgMoneyDown
     */
    public static String getImgMoneyDown() {
        return getCommonsDir() + configuration.imgMoneyDown;
    }

    /**
     * @return the imgMoneyDownSmall
     */
    public static String getImgMoneyDownSmall() {
        return getCommonsDir() + configuration.imgMoneyDownSmall;
    }

    /**
     * @return the imgMoneyUp
     */
    public static String getImgMoneyUp() {
        return getCommonsDir() + configuration.imgMoneyUp;
    }

    /**
     * @return the imgMoneyUpSmall
     */
    public static String getImgMoneyUpSmall() {
        return getCommonsDir() + configuration.imgMoneyUpSmall;
    }

    /**
     * @return the imgNoAvatar
     */
    public static String getImgNoAvatar() {
        return getCommonsDir() + configuration.imgNoAvatar;
    }

    /**
     * @return the imgValidIcon
     */
    public static String getImgValidIcon() {
        return getCommonsDir() + configuration.imgValidIcon;
    }

    /**
     * @return the imgValidIcon
     */
    public static String getImgSoftwareNoLogo() {
        return getCommonsDir() + configuration.imgSoftwareNoLogo;
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
     * @return the path to the jsDatePicker
     */
    public static String getJsDatePicker(String langCode) {
        return configuration.resourcesDir + "/" + langCode + configuration.jsDatePicker;
    }

    protected void loadConfiguration() {
        properties = ConfigurationManager.loadProperties("web.properties");

        // DIRECTORIES
        documentationDir = properties.getString("bloatit.documentation.dir");
        wwwDir = properties.getString("bloatit.www.dir");
        resourcesDir = properties.getString("bloatit.resources.dir");

        // OTHERS
        htmlIndent = new AtomicBoolean(properties.getBoolean("bloatit.html.minify"));

        // CSS
        css = properties.getString("bloatit.css");
        cssDatePicker = properties.getString("bloatit.css.datepicker");

        // IMAGES
        imgLogo = properties.getString("bloatit.img.logo");
        imgPresentation = properties.getString("bloatit.img.presentation");
        imgMoneyDown = properties.getString("bloatit.img.money.down");
        imgMoneyDownSmall = properties.getString("bloatit.img.money.down.small");
        imgMoneyUp = properties.getString("bloatit.img.money.up");
        imgMoneyUpSmall = properties.getString("bloatit.img.money.up");
        imgNoAvatar = properties.getString("bloatit.img.no.avatar");
        imgValidIcon = properties.getString("bloatit.img.valid");
        imgSoftwareNoLogo = properties.getString("bloatit.img.software.no.logo");

        // JAVASCRIPT
        jsJquery = properties.getString("bloatit.js.jquery");
        jsJqueryUi = properties.getString("bloatit.js.jqueryui");
        jsFlexie = properties.getString("bloatit.js.flexie");
        jsSelectivizr = properties.getString("bloatit.js.selectivizr");
        jsDatePicker = properties.getString("bloatit.js.datepicker");
    }

    public static void load() {
        configuration.loadConfiguration();
    }

    @Override
    public String getName() {
        return "Web";
    }

    @Override
    protected void doReload() {
        configuration.loadConfiguration();
    }
}
