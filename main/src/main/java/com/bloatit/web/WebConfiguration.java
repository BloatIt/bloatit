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
package com.bloatit.web;

import java.math.BigDecimal;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;
import com.bloatit.framework.FrameworkConfiguration;

public class WebConfiguration extends ReloadableConfiguration {
    private static final WebConfiguration configuration = new WebConfiguration();
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
    private String imgIdea;
    private String imgTeam;
    private String imgMessage;
    private String imgAccountCharge;
    private String imgAccountWithdraw;
    private BigDecimal defaultChargingAmount;

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
     * @return the imgValidIcon
     */
    public static String getImgSoftwareNoLogo() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgSoftwareNoLogo;
    }

    /**
     * @return the imgIdea
     */
    public static String getImgIdea() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgIdea;
    }

    public static String getImgMessage() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgMessage;
    }

    public static String getImgTeam() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgTeam;
    }

    public static String getImgAccountCharge() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgAccountCharge;
    }

    public static String getImgAccountWithdraw() {
        return FrameworkConfiguration.getCommonsDir() + configuration.imgAccountWithdraw;
    }
    
    public static BigDecimal getDefaultChargingAmount() {
        return configuration.defaultChargingAmount;
    }

    private void loadConfiguration() {
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
        imgMoneyUpSmall = properties.getString("bloatit.img.money.up.small");
        imgNoAvatar = properties.getString("bloatit.img.no.avatar");
        imgValidIcon = properties.getString("bloatit.img.valid");
        imgSoftwareNoLogo = properties.getString("bloatit.img.software.no.logo");
        imgFeatureStateSuccess = properties.getString("bloatit.img.feature.state.success");
        imgFeatureStateFailed = properties.getString("bloatit.img.feature.state.failed");
        imgIdea = properties.getString("bloatit.img.idea");
        imgTeam = properties.getString("bloatit.img.team");
        imgMessage = properties.getString("bloatit.img.message");
        imgAccountCharge = properties.getString("bloatit.img.account.charge");
        imgAccountWithdraw = properties.getString("bloatit.img.account.withdraw");
        
        // OTHERS
        defaultChargingAmount = properties.getBigDecimal("bloatit.default.charging.amount");
    }

    protected static void load() {
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
