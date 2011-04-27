package com.bloatit.model;

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
    private int kudosableMinInfluenceToUnkudos;
    private int kudosableMinInfluenceToKudos;
    private String bloatitLibravatarURI;
    private int recentActivityDays;

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

    protected static int getKudosableDefaultTurnValid() {
        return configuration.kudosableDefaultTurnValid;
    }

    protected static int getKudosableDefaultTurnRejected() {
        return configuration.kudosableDefaultTurnRejected;
    }

    protected static int getKudosableDefaultTurnHidden() {
        return configuration.kudosableDefaultTurnHidden;
    }

    protected static int getKudosableDefaultTurnPending() {
        return configuration.kudosableDefaultTurnPending;
    }

    protected static int getKudosableCommentTurnValid() {
        return configuration.kudosableCommentTurnValid;
    }

    protected static int getKudosableCommentTurnRejected() {
        return configuration.kudosableCommentTurnRejected;
    }

    protected static int getKudosableCommentTurnHidden() {
        return configuration.kudosableCommentTurnHidden;
    }

    protected static int getKudosableCommentTurnPending() {
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

    protected static int getKudosableOfferTurnValid() {
        return configuration.kudosableOfferTurnValid;
    }

    protected static int getKudosableOfferTurnRejected() {
        return configuration.kudosableOfferTurnRejected;
    }

    protected static int getKudosableOfferTurnHidden() {
        return configuration.kudosableOfferTurnHidden;
    }

    protected static int getKudosableOfferTurnPending() {
        return configuration.kudosableOfferTurnPending;
    }

    protected static int getKudosableTranslationTurnValid() {
        return configuration.kudosableTranslationTurnValid;
    }

    protected static int getKudosableTranslationTurnRejected() {
        return configuration.kudosableTranslationTurnRejected;
    }

    protected static int getKudosableTranslationTurnHidden() {
        return configuration.kudosableTranslationTurnHidden;
    }

    protected static int getKudosableTranslationTurnPending() {
        return configuration.kudosableTranslationTurnPending;
    }

    protected static int getKudosableMinInfluenceToUnkudos() {
        return configuration.kudosableMinInfluenceToUnkudos;
    }

    protected static int getKudosableMinInfluenceToKudos() {
        return configuration.kudosableMinInfluenceToKudos;
    }

    public static String getLibravatarURI() {
        return configuration.bloatitLibravatarURI;
    }
    
    // Others
    
    protected static int getRecentActivityDays() {
        return configuration.recentActivityDays;
    }

    private void load() {
        properties = ConfigurationManager.loadProperties("model.properties");
        
        recentActivityDays = properties.getInt("recent.activity.days");

        // Kudosable configuration
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

        bloatitLibravatarURI = properties.getString("bloatit.libravatar.uri");
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
