package com.bloatit.model;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.MasterConfiguration;

public class ModelConfiguration extends MasterConfiguration {
    public static ModelConfiguration configuration = new ModelConfiguration();

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
    private int kudosableMinInfluenceToUnkudos;
    private int kudosableMinInfluenceToKudos;

    private ModelConfiguration() {
        super();
        load();
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

    public static int getKudosableMinInfluenceToUnkudos() {
        return configuration.kudosableMinInfluenceToUnkudos;
    }

    public static int getKudosableMinInfluenceToKudos() {
        return configuration.kudosableMinInfluenceToKudos;
    }
    
    protected void load(){
        properties = ConfigurationManager.loadProperties("model.properties");

        //
        // Kudosable configuration
        //
        kudosableDefaultTurnValid = properties.getInt("kudosable.default.turn_valid", 100);
        kudosableDefaultTurnRejected = properties.getInt("kudosable.default.turn_rejected", -100);
        kudosableDefaultTurnHidden = properties.getInt("kudosable.default.turn_hidden", -10);
        kudosableDefaultTurnPending = properties.getInt("kudosable.default.turn_pending", 10);

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

        kudosableMinInfluenceToUnkudos = properties.getInt("kudosable.min_influence_unkudos", 1);
        kudosableMinInfluenceToKudos = properties.getInt("kudosable.min_influence_kudos", 0);
    }

    @Override
    public String getName() {
        return "Model";
    }

    @Override
    protected void doReload() {
        configuration.load();
    }
}
