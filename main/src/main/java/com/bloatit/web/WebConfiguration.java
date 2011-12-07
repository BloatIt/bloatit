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
import com.bloatit.framework.ResourceFinder;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;

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
    private String imgSoftware;
    private String imgMessage;
    private String imgAccountCharge;
    private String imgAccountWithdraw;
    private String imgTwitterIcon;
    private String imgIdenticaIcon;
    private String imgMercanetVISA;
    private String imgMercanetMastercard;
    private String imgMercanetCB;
    private String imgAtom;
    private BigDecimal defaultChargingAmount;
    private ResourceFinder finder;
    private int feedItemNumber;

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
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.css);
    }

    public static String getCssDatePicker() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.cssDatePicker);
    }

    // ----------------------------------------------------------
    // IMAGES
    // ----------------------------------------------------------

    /**
     * @return the path to the imgLogo
     */
    public static String getImgLogo() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgLogo);
    }

    /**
     * @return the path to the imgPresentation
     */
    public static String getImgPresentation(final String langCode) {
        return find(configuration.imgPresentation, langCode);

    }

    /**
     * @return the imgMoneyDown
     */
    public static String getImgMoneyDown() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyDown);
    }

    /**
     * @return the imgMoneyDownSmall
     */
    public static String getImgMoneyDownSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyDownSmall);
    }

    /**
     * @return the imgMoneyUp
     */
    public static String getImgMoneyUp() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyUp);
    }

    /**
     * @return the imgMoneyUpSmall
     */
    public static String getImgMoneyUpSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMoneyUpSmall);
    }

    /**
     * @return the imgNoAvatar
     */
    public static String getImgNoAvatar() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgNoAvatar);
    }
    
    /**
     * @return the imgNoAvatar
     */
    public static String getImgNoTeamAvatar() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/avatar_team.png");
    }

    /**
     * @return the imgValidIcon
     */
    public static String getImgValidIcon() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgValidIcon);
    }

    /**
     * @return the imgFeatureStateSuccess
     */
    public static String getImgFeatureStateSuccess(final String langCode) {
        return find(configuration.imgFeatureStateSuccess, langCode);
    }

    /**
     * @return the imgFeatureStateSuccess
     */
    public static String getImgFeatureStateFailed(final String langCode) {
        return find(configuration.imgFeatureStateFailed, langCode);
    }

    /**
     * @return the imgValidIcon
     */
    public static String getImgSoftwareNoLogo() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgSoftwareNoLogo);
    }

    public static String getImgSoftware() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgSoftware);
    }

    /**
     * @return the imgIdea
     */
    public static String getImgIdea() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgIdea);
    }

    public static String getImgMessage() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMessage);
    }

    public static String getImgTeam() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgTeam);
    }

    public static String getImgAccountCharge() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgAccountCharge);
    }

    public static String getImgAccountWithdraw() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgAccountWithdraw);
    }

    public static String getImgTwitterIcon() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgTwitterIcon);
    }

    public static String getImgIdenticaIcon() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgIdenticaIcon);
    }

    public static String getImgMercanetVISA() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMercanetVISA);
    }

    public static String getImgMercanetMastercard() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMercanetMastercard);
    }

    public static String getImgMercanetCB() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgMercanetCB);
    }

    public static String getAtomImg() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + configuration.imgAtom);
    }

    public static BigDecimal getDefaultChargingAmount() {
        return configuration.defaultChargingAmount;
    }

    public static int getFeedItemNumber() {
        return configuration.feedItemNumber;
    }

    private void loadConfiguration() {

        finder = new ResourceFinder(FrameworkConfiguration.getWwwDir());

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
        imgSoftware = properties.getString("bloatit.img.software");
        imgMessage = properties.getString("bloatit.img.message");
        imgAccountCharge = properties.getString("bloatit.img.account.charge");
        imgAccountWithdraw = properties.getString("bloatit.img.account.withdraw");
        imgTwitterIcon = properties.getString("bloatit.img.twitter.icon");
        imgIdenticaIcon = properties.getString("bloatit.img.identica.icon");
        imgMercanetVISA = properties.getString("bloatit.img.mercanet.visa");
        imgMercanetMastercard = properties.getString("bloatit.img.mercanet.mastercard");
        imgMercanetCB = properties.getString("bloatit.img.mercanet.cb");
        imgAtom = properties.getString("bloatit.img.atom");

        // OTHERS
        defaultChargingAmount = properties.getBigDecimal("bloatit.default.charging.amount");
        feedItemNumber = properties.getInt("bloatit.feed.item.number", 5);

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

    private static String find(String resource, String langCode) {
        try {
            return configuration.finder.find(FrameworkConfiguration.getResourcesDir() + "/" + langCode + resource);
        } catch (ExternalErrorException e) {
            return configuration.finder.find(FrameworkConfiguration.getResourcesDir() + "/en" + resource);
        }
    }

    public static String getImgIdeaSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/idea_small.png");
    }

    public static String getImgManageSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/settings_small.png");
    }

    public static String getImgManageNotifSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/mail_settings_small.png");
    }

    public static String getImgRssSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/rss_small.png");
    }

    public static String getImgIdeaTiny() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/idea_tiny.png");
    }
    
    public static String getImgContributionTiny() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/coins_tiny.png");
    }
    
    public static String getImgOfferTiny() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/offer_tiny.png");
    }
    
    public static String getImgCommentTiny() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/comment_tiny.png");
    }
    
    public static String getImgReleaseTiny() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/release_tiny.png");
    }
    
    public static String getImgBugTiny() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/bug_tiny.png");
    }

    public static String getImgLogoSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/elveos_logo_small.png");
    }

    public static String getImgTimelineSmall() {
        return configuration.finder.find(FrameworkConfiguration.getCommonsDir() + "/img/timeline_small.png");
    }

}
