package com.bloatit.web;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;
import com.bloatit.framework.FrameworkConfiguration;

public class WebConfiguration extends ReloadableConfiguration {
    public static final WebConfiguration configuration = new WebConfiguration();
    private PropertiesRetriever properties;

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
    private String imgFeatureStateSuccess;
    private String imgFeatureStateFailed;
    private String imgFeatureStateDeveloping;
    private String imgFeatureStateFunding;

    private WebConfiguration() {
        super();
        loadConfiguration();
    }

    public static WebConfiguration getConfiguration() {
        return configuration;
    }

    // ----------------------------------------------------------
    // CSS
    // ----------------------------------------------------------

    /**
     * @return the path to the css
     */
    public static String getCss() {
        return FrameworkConfiguration.getCommonsDir() + configuration.css;
    }

    public static String getCssDatePicker() {
        return FrameworkConfiguration.getCommonsDir() + configuration.cssDatePicker;
    }

    // ----------------------------------------------------------
    // IMAGES
    // ----------------------------------------------------------

    /**
     * @return the path to the imgLogo
     */
    public static String getImgLogo() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgLogo;
    }

    /**
     * @return the path to the imgPresentation
     */
    public static String getImgPresentation(final String langCode) {
        return FrameworkConfiguration.getResourcesDir() + "/" + langCode + configuration.imgPresentation;
    }

    /**
     * @return the imgMoneyDown
     */
    public static String getImgMoneyDown() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyDown;
    }

    /**
     * @return the imgMoneyDownSmall
     */
    public static String getImgMoneyDownSmall() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyDownSmall;
    }

    /**
     * @return the imgMoneyUp
     */
    public static String getImgMoneyUp() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyUp;
    }

    /**
     * @return the imgMoneyUpSmall
     */
    public static String getImgMoneyUpSmall() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyUpSmall;
    }

    /**
     * @return the imgNoAvatar
     */
    public static String getImgNoAvatar() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgNoAvatar;
    }

    /**
     * @return the imgValidIcon
     */
    public static String getImgValidIcon() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgValidIcon;
    }

    /**
     * @return the imgFeatureStateSuccess
     */
    public static String getImgFeatureStateSuccess(final String langCode) {
        return FrameworkConfiguration.getResourcesDir() + "/" + langCode + configuration.imgFeatureStateSuccess;
    }

    /**
     * @return the imgFeatureStateSuccess
     */
    public static String getImgFeatureStateFailed(final String langCode) {
        return FrameworkConfiguration.getResourcesDir() + "/" + langCode + configuration.imgFeatureStateFailed;
    }

    /**
     * @return the imgFeatureStateSuccess
     */
    public static String getImgFeatureStateDeveloping(final String langCode) {
        return FrameworkConfiguration.getResourcesDir() + "/" + langCode + configuration.imgFeatureStateDeveloping;
    }

    /**
     * @return the imgFeatureStateSuccess
     */
    public static String getImgFeatureStateFunding(final String langCode) {
        return FrameworkConfiguration.getResourcesDir() + "/" + langCode + configuration.imgFeatureStateFunding;
    }

    /**
     * @return the imgValidIcon
     */
    public static String getImgSoftwareNoLogo() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgSoftwareNoLogo;
    }

    protected void loadConfiguration() {
        properties = ConfigurationManager.loadProperties("web.properties");

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
        imgFeatureStateSuccess = properties.getString("bloatit.img.feature.state.success");
        imgFeatureStateFailed = properties.getString("bloatit.img.feature.state.failed");
        imgFeatureStateDeveloping = properties.getString("bloatit.img.feature.state.developing");
        imgFeatureStateFunding = properties.getString("bloatit.img.feature.state.funding");
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
