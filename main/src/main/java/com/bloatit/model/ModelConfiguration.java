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
package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ReloadableConfiguration;

public class ModelConfiguration extends ReloadableConfiguration {
    private static ModelConfiguration configuration = new ModelConfiguration();

    private PropertiesRetriever properties;

    private int kudosableDefaultTurnValid;
    private int kudosableDefaultTurnRejected;
    private int kudosableDefaultTurnHidden;
    private int kudosableDefaultTurnPending;
    private int kudosableCommentTurnValid;
    private int kudosableCommentTurnRejected;
    private int kudosableCommentTurnHidden;
    private int kudosableCommentTurnPending;
    private int kudosableFeatureTurnValid;
    private int kudosableFeatureTurnRejected;
    private int kudosableFeatureTurnHidden;
    private int kudosableFeatureTurnPending;
    private int kudosableOfferTurnValid;
    private int kudosableOfferTurnRejected;
    private int kudosableOfferTurnHidden;
    private int kudosableOfferTurnPending;
    private int kudosableTranslationTurnValid;
    private int kudosableTranslationTurnRejected;
    private int kudosableTranslationTurnHidden;
    private int kudosableTranslationTurnPending;
    private int kudosableMinKarmaToUnkudos;
    private int kudosableMinKarmaToKudos;
    private int kudosableMinKarmaToComment;
    private int kudosableMinKarmaToMakeOffer;
    private int kudosableMinKarmaToCreateFeature;
    private int kudosableStepToGainKarma;
    private int karmaInitialAmount;
    private int karmaActivationAmount;
    private int karmaHideThreshold;

    private int recentHistoryDays;
    private String[] administratorMails;
    private BigDecimal linkeosTaxesRate;
    private String linkeosName;
    private String linkeosTaxesIdentification;
    private String invoiceLinkeosLogo;
    private String linkeosInvoiceTemplate;
    private String linkeosStreet;
    private String linkeosExtras;
    private String linkeosCity;
    private String linkeosCountry;
    private String linkeosLegalIdentification;

    private ModelConfiguration() {
        super();
        load();
    }

    /**
     * Make sure the configuration file is loaded.
     */
    protected static void loadConfiguration() {
        configuration.getClass();
    }

    public static ModelConfiguration getConfiguration() {
        return configuration;
    }

    public static PropertiesRetriever getProperties() {
        return configuration.properties;
    }

    public static int getKudosableDefaultTurnValid() {
        return configuration.kudosableDefaultTurnValid;
    }

    public static int getKudosableDefaultTurnRejected() {
        return configuration.kudosableDefaultTurnRejected;
    }

    public static int getKudosableDefaultTurnHidden() {
        return configuration.kudosableDefaultTurnHidden;
    }

    public static int getKudosableDefaultTurnPending() {
        return configuration.kudosableDefaultTurnPending;
    }

    public static int getKudosableCommentTurnValid() {
        return configuration.kudosableCommentTurnValid;
    }

    public static int getKudosableCommentTurnRejected() {
        return configuration.kudosableCommentTurnRejected;
    }

    public static int getKudosableCommentTurnHidden() {
        return configuration.kudosableCommentTurnHidden;
    }

    public static int getKudosableCommentTurnPending() {
        return configuration.kudosableCommentTurnPending;
    }

    public static int getKudosableFeatureTurnValid() {
        return configuration.kudosableFeatureTurnValid;
    }

    public static int getKudosableFeatureTurnRejected() {
        return configuration.kudosableFeatureTurnRejected;
    }

    public static int getKudosableFeatureTurnHidden() {
        return configuration.kudosableFeatureTurnHidden;
    }

    public static int getKudosableFeatureTurnPending() {
        return configuration.kudosableFeatureTurnPending;
    }

    public static int getKudosableOfferTurnValid() {
        return configuration.kudosableOfferTurnValid;
    }

    public static int getKudosableOfferTurnRejected() {
        return configuration.kudosableOfferTurnRejected;
    }

    public static int getKudosableOfferTurnHidden() {
        return configuration.kudosableOfferTurnHidden;
    }

    public static int getKudosableOfferTurnPending() {
        return configuration.kudosableOfferTurnPending;
    }

    public static int getKudosableTranslationTurnValid() {
        return configuration.kudosableTranslationTurnValid;
    }

    public static int getKudosableTranslationTurnRejected() {
        return configuration.kudosableTranslationTurnRejected;
    }

    public static int getKudosableTranslationTurnHidden() {
        return configuration.kudosableTranslationTurnHidden;
    }

    public static int getKudosableTranslationTurnPending() {
        return configuration.kudosableTranslationTurnPending;
    }

    public static int getKudosableMinKarmaToUnkudos() {
        return configuration.kudosableMinKarmaToUnkudos;
    }

    public static int getKudosableMinKarmaToKudos() {
        return configuration.kudosableMinKarmaToKudos;
    }

    public static int getKudosableMinKarmaToComment() {
        return configuration.kudosableMinKarmaToComment;
    }

    public static int getKudosableMinKarmaToMakeOffer() {
        return configuration.kudosableMinKarmaToMakeOffer;
    }

    public static int getKudosableMinKarmaToCreateFeature() {
        return configuration.kudosableMinKarmaToCreateFeature;
    }

    public static int getKudosableStepToGainKarma() {
        return configuration.kudosableStepToGainKarma;
    }

    public static int getKarmaInitialAmount() {
        return configuration.karmaInitialAmount;
    }

    public static int getKarmaActivationAmount() {
        return configuration.karmaActivationAmount;
    }

    public static int getKarmaHideThreshold() {
        return configuration.karmaHideThreshold;
    }

    // Others

    protected static int getRecentHistoryDays() {
        return configuration.recentHistoryDays;
    }

    public static String[] getAdminstratorMails() {
        return configuration.administratorMails;
    }

    public static String getInvoiceLinkeosLogo() {
        return ConfigurationManager.SHARE_DIR + "/resources/" + configuration.invoiceLinkeosLogo;
    }

    private void load() {
        properties = ConfigurationManager.loadProperties("model.properties");

        recentHistoryDays = properties.getInt("recent.history.days");

        // Kudosable configuration
        kudosableDefaultTurnValid = properties.getInt("kudosable.default.turn_valid", 5);
        kudosableDefaultTurnRejected = properties.getInt("kudosable.default.turn_rejected", -10);
        kudosableDefaultTurnHidden = properties.getInt("kudosable.default.turn_hidden", -5);
        kudosableDefaultTurnPending = properties.getInt("kudosable.default.turn_pending", 0);
        kudosableStepToGainKarma = properties.getInt("kudosable.karma.stepToGain", 5);
        karmaInitialAmount = properties.getInt("karma.InitialAmount", -10);
        karmaActivationAmount = properties.getInt("karma.activationAmount", 10);
        karmaHideThreshold = properties.getInt("karma.hideThreshold", -10);

        // Comment feature offer translation
        kudosableCommentTurnValid = properties.getInt("kudosable.comment.turn_valid", kudosableDefaultTurnValid);
        kudosableCommentTurnRejected = properties.getInt("kudosable.comment.turn_rejected", kudosableDefaultTurnRejected);
        kudosableCommentTurnHidden = properties.getInt("kudosable.comment.turn_hidden", kudosableDefaultTurnHidden);
        kudosableCommentTurnPending = properties.getInt("kudosable.comment.turn_pending", kudosableDefaultTurnPending);

        kudosableFeatureTurnValid = properties.getInt("kudosable.feature.turn_valid", kudosableDefaultTurnValid);
        kudosableFeatureTurnRejected = properties.getInt("kudosable.feature.turn_rejected", kudosableDefaultTurnRejected);
        kudosableFeatureTurnHidden = properties.getInt("kudosable.feature.turn_hidden", kudosableDefaultTurnHidden);
        kudosableFeatureTurnPending = properties.getInt("kudosable.feature.turn_pending", kudosableDefaultTurnPending);

        kudosableOfferTurnValid = properties.getInt("kudosable.offer.turn_valid", kudosableDefaultTurnValid);
        kudosableOfferTurnRejected = properties.getInt("kudosable.offer.turn_rejected", kudosableDefaultTurnRejected);
        kudosableOfferTurnHidden = properties.getInt("kudosable.offer.turn_hidden", kudosableDefaultTurnHidden);
        kudosableOfferTurnPending = properties.getInt("kudosable.offer.turn_pending", kudosableDefaultTurnPending);

        kudosableTranslationTurnValid = properties.getInt("kudosable.tranlation.turn_valid", kudosableDefaultTurnValid);
        kudosableTranslationTurnRejected = properties.getInt("kudosable.tranlation.turn_rejected", kudosableDefaultTurnRejected);
        kudosableTranslationTurnHidden = properties.getInt("kudosable.tranlation.turn_hidden", kudosableDefaultTurnHidden);
        kudosableTranslationTurnPending = properties.getInt("kudosable.tranlation.turn_pending", kudosableDefaultTurnPending);

        kudosableMinKarmaToUnkudos = properties.getInt("kudosable.min_karma.unkudos", -9);
        kudosableMinKarmaToKudos = properties.getInt("kudosable.min_karma.kudos", -9);
        kudosableMinKarmaToComment = properties.getInt("kudosable.min_karma/comment", -9);
        kudosableMinKarmaToMakeOffer = properties.getInt("kudosable.min_karma.make_offer", -9);
        kudosableMinKarmaToCreateFeature = properties.getInt("kudosable.min_karma.create_feature", -9);

        administratorMails = properties.getStringArray("administrator.emails");

        linkeosName = properties.getString("linkeos.name");
        linkeosInvoiceTemplate = properties.getString("linkeos.invoice_template");
        linkeosStreet = properties.getString("linkeos.street");
        linkeosExtras = properties.getString("linkeos.extras");
        linkeosCity = properties.getString("linkeos.city");
        linkeosCountry = properties.getString("linkeos.country");
        linkeosLegalIdentification = properties.getString("linkeos.legal_identification");

        linkeosTaxesIdentification = properties.getString("linkeos.taxes_identification");
        linkeosTaxesRate = properties.getBigDecimal("linkeos.taxes_rate");

        invoiceLinkeosLogo = properties.getString("invoice.linkeos.logo");
    }

    @Override
    public String getName() {
        return "Model";
    }

    @Override
    protected void doReload() {
        configuration.load();
    }

    public static String getLinkeosName() {
        return configuration.linkeosName;
    }

    public static BigDecimal getLinkeosTaxesRate() {
        return configuration.linkeosTaxesRate;
    }

    public static String getLinkeosTaxIdentification() {
        return configuration.linkeosTaxesIdentification;
    }

    public static String getLinkeosInvoiceTemplate() {
        return configuration.linkeosInvoiceTemplate;
    }

    public static String getLinkeosStreet() {
        return configuration.linkeosStreet;
    }

    public static String getLinkeosExtras() {
        return configuration.linkeosExtras;
    }

    public static String getLinkeosCity() {
        return configuration.linkeosCity;
    }

    public static String getLinkeosCountry() {
        return configuration.linkeosCountry;
    }

    public static String getLinkeosLegalIdentification() {
        return configuration.linkeosLegalIdentification;
    }

}
