package com.bloatit.web;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;

public class WebConfiguration {
    public static final WebConfiguration configuration = new WebConfiguration();
    private final PropertiesRetriever properties;

    // DIRECTORIES
    private final String documentationDir;
    private final String wwwDir;
    private final String resourcesDir;

    // OTHERS
    private final Boolean htmlIndent;

    // CSS
    private final String css;
    private final String cssDatePicker;

    // IMAGES
    private final String imgLogo;
    private final String imgPresentation;
    private final String imgMoneyDown;
    private final String imgMoneyDownSmall;
    private final String imgMoneyUp;
    private final String imgMoneyUpSmall;
    private final String imgNoAvatar;
    private final String imgValidIcon;
    private final String imgSoftwareNoLogo;

    // JAVASCRIPT
    private final String jsJquery;
    private final String jsJqueryUi;
    private final String jsFlexie;
    private final String jsSelectivizr;
    private final String jsDatePicker;

    private WebConfiguration() {
        properties = ConfigurationManager.loadProperties("web.properties");

        // DIRECTORIES
        documentationDir = properties.getString("bloatit.documentation.dir");
        wwwDir = properties.getString("bloatit.www.dir");
        resourcesDir = properties.getString("bloatit.resources.dir");

        // OTHERS
        htmlIndent = properties.getBoolean("bloatit.html.indent");

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
        imgSoftwareNoLogo= properties.getString("bloatit.img.software.no.logo");

        // JAVASCRIPT
        jsJquery = properties.getString("bloatit.js.jquery");
        jsJqueryUi = properties.getString("bloatit.js.jqueryui");
        jsFlexie = properties.getString("bloatit.js.flexie");
        jsSelectivizr = properties.getString("bloatit.js.selectivizr");
        jsDatePicker = properties.getString("bloatit.js.datepicker");
    }

    /**
     * Make sure the configuration file is loaded.
     */
    public static void loadConfiguration() {
        configuration.getClass();
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
    public static boolean isHtmlIndent() {
        return configuration.htmlIndent.booleanValue();
    }
    
    public static String getCommonsDir(){
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
    
}
