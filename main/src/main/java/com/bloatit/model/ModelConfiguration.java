package com.bloatit.model;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;

public class ModelConfiguration {
    public static final ModelConfiguration configuration = new ModelConfiguration();

    private final PropertiesRetriever properties;

    private final int kudosableDefaultTurnValid;
    private final int kudosableDefaultTurnRejected;
    private final int kudosableDefaultTurnHidden;
    private final int kudosableDefaultTurnPending;
    private final int kudosableCommentTurnValid;
    private final int kudosableCommentTurnRejected;
    private final int kudosableCommentTurnHidden;
    private final int kudosableCommentTurnPending;
    private final int kudosableDemandTurnValid;
    private final int kudosableDemandTurnRejected;
    private final int kudosableDemandTurnHidden;
    private final int kudosableDemandTurnPending;
    private final int kudosableOfferTurnValid;
    private final int kudosableOfferTurnRejected;
    private final int kudosableOfferTurnHidden;
    private final int kudosableOfferTurnPending;
    private final int kudosableTranslationTurnValid;
    private final int kudosableTranslationTurnRejected;
    private final int kudosableTranslationTurnHidden;
    private final int kudosableTranslationTurnPending;
    private final int kudosableMinInfluenceToUnkudos;
    private final int kudosableMinInfluenceToKudos;

    private ModelConfiguration() {
        properties = ConfigurationManager.loadProperties("model.properties");

        //
        // Kudosable configuration
        //
        kudosableDefaultTurnValid = properties.getInt("kudosable.default.turn_valid", 100);
        kudosableDefaultTurnRejected = properties.getInt("kudosable.default.turn_rejected", -100);
        kudosableDefaultTurnHidden = properties.getInt("kudosable.default.turn_hidden", -10);
        kudosableDefaultTurnPending = properties.getInt("kudosable.default.turn_pending", 10);

        // Comment demand offer translation
        kudosableCommentTurnValid = properties.getInt("kudosable.comment.turn_valid", kudosableDefaultTurnValid);
        kudosableCommentTurnRejected = properties.getInt("kudosable.comment.turn_rejected", kudosableDefaultTurnRejected);
        kudosableCommentTurnHidden = properties.getInt("kudosable.comment.turn_hidden", kudosableDefaultTurnHidden);
        kudosableCommentTurnPending = properties.getInt("kudosable.comment.turn_pending", kudosableDefaultTurnPending);

        kudosableDemandTurnValid = properties.getInt("kudosable.demand.turn_valid", kudosableDefaultTurnValid);
        kudosableDemandTurnRejected = properties.getInt("kudosable.demand.turn_rejected", kudosableDefaultTurnRejected);
        kudosableDemandTurnHidden = properties.getInt("kudosable.demand.turn_hidden", kudosableDefaultTurnHidden);
        kudosableDemandTurnPending = properties.getInt("kudosable.demand.turn_pending", kudosableDefaultTurnPending);

        kudosableOfferTurnValid = properties.getInt("kudosable.offer.turn_valid", kudosableDefaultTurnValid);
        kudosableOfferTurnRejected = properties.getInt("kudosable.offer.turn_rejected", kudosableDefaultTurnRejected);
        kudosableOfferTurnHidden = properties.getInt("kudosable.offer.turn_hidden", kudosableDefaultTurnHidden);
        kudosableOfferTurnPending = properties.getInt("kudosable.offer.turn_pending", kudosableDefaultTurnPending);

        kudosableTranslationTurnValid = properties.getInt("kudosable.tranlation.turn_valid", kudosableDefaultTurnValid);
        kudosableTranslationTurnRejected = properties.getInt("kudosable.tranlation.turn_rejected", kudosableDefaultTurnRejected);
        kudosableTranslationTurnHidden = properties.getInt("kudosable.tranlation.turn_hidden", kudosableDefaultTurnHidden);
        kudosableTranslationTurnPending = properties.getInt("kudosable.tranlation.turn_pending", kudosableDefaultTurnPending);

        kudosableMinInfluenceToUnkudos = properties.getInt("kudosable.min_influence_unkudos", 1);
        kudosableMinInfluenceToKudos = properties.getInt("kudosable.min_influence_kudos", 0);

    }

    /**
     * Make sure the configuration file is loaded.
     */
    public static void loadConfiguration() {
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

    public static int getKudosableDemandTurnValid() {
        return configuration.kudosableDemandTurnValid;
    }

    public static int getKudosableDemandTurnRejected() {
        return configuration.kudosableDemandTurnRejected;
    }

    public static int getKudosableDemandTurnHidden() {
        return configuration.kudosableDemandTurnHidden;
    }

    public static int getKudosableDemandTurnPending() {
        return configuration.kudosableDemandTurnPending;
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

    public static int getKudosableMinInfluenceToUnkudos() {
        return configuration.kudosableMinInfluenceToUnkudos;
    }

    public static int getKudosableMinInfluenceToKudos() {
        return configuration.kudosableMinInfluenceToKudos;
    }

}
