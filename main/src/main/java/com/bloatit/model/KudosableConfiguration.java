package com.bloatit.model;

import com.bloatit.framework.utils.ConfigurationManager;
import com.bloatit.framework.utils.ConfigurationManager.PropertiesRetriever;

public class KudosableConfiguration {

    private static final PropertiesRetriever conf = ConfigurationManager.loadProperties("kudosable");

    private static final int DEFAULT_TURN_VALID = conf.getInt("kudosable.default.turn_valid", 100);
    private static final int DEFAULT_TURN_REJECTED = conf.getInt("kudosable.default.turn_rejected", -100);
    private static final int DEFAULT_TURN_HIDDEN = conf.getInt("kudosable.default.turn_hidden", -10);
    private static final int DEFAULT_TURN_PENDING = conf.getInt("kudosable.default.turn_pending", 10);

    // Comment demandImplementation offer translation
    private static final int COMMENT_TURN_VALID = conf.getInt("kudosable.comment.turn_valid", getDefaultTurnValid());
    private static final int COMMENT_TURN_REJECTED = conf.getInt("kudosable.comment.turn_rejected", getDefaultTurnRejected());
    private static final int COMMENT_TURN_HIDDEN = conf.getInt("kudosable.comment.turn_hidden", getDefaultTurnHidden());
    private static final int COMMENT_TURN_PENDING = conf.getInt("kudosable.comment.turn_pending", getDefaultTurnPending());

    private static final int DEMAND_TURN_VALID = conf.getInt("kudosable.demand.turn_valid", getDefaultTurnValid());
    private static final int DEMAND_TURN_REJECTED = conf.getInt("kudosable.demand.turn_rejected", getDefaultTurnRejected());
    private static final int DEMAND_TURN_HIDDEN = conf.getInt("kudosable.demand.turn_hidden", getDefaultTurnHidden());
    private static final int DEMAND_TURN_PENDING = conf.getInt("kudosable.demand.turn_pending", getDefaultTurnPending());

    private static final int OFFER_TURN_VALID = conf.getInt("kudosable.offer.turn_valid", getDefaultTurnValid());
    private static final int OFFER_TURN_REJECTED = conf.getInt("kudosable.offer.turn_rejected", getDefaultTurnRejected());
    private static final int OFFER_TURN_HIDDEN = conf.getInt("kudosable.offer.turn_hidden", getDefaultTurnHidden());
    private static final int OFFER_TURN_PENDING = conf.getInt("kudosable.offer.turn_pending", getDefaultTurnPending());

    private static final int TRANSLATION_TURN_VALID = conf.getInt("kudosable.tranlation.turn_valid", getDefaultTurnValid());
    private static final int TRANSLATION_TURN_REJECTED = conf.getInt("kudosable.tranlation.turn_rejected", getDefaultTurnRejected());
    private static final int TRANSLATION_TURN_HIDDEN = conf.getInt("kudosable.tranlation.turn_hidden", getDefaultTurnHidden());
    private static final int TRANSLATION_TURN_PENDING = conf.getInt("kudosable.tranlation.turn_pending", getDefaultTurnPending());



    private static final int MIN_INFLUENCE_TO_UNKUDOS = conf.getInt("kudosable.min_influence_unkudo", 2);
    private static final int MIN_INFLUENCE_TO_KUDOS = conf.getInt("kudosable.min_influence_kudo", 0);
    /**
     * @return the defaultTurnValid
     */
    public static final int getDefaultTurnValid() {
        return DEFAULT_TURN_VALID;
    }
    /**
     * @return the defaultTurnRejected
     */
    public static final int getDefaultTurnRejected() {
        return DEFAULT_TURN_REJECTED;
    }
    /**
     * @return the defaultTurnHidden
     */
    public static final int getDefaultTurnHidden() {
        return DEFAULT_TURN_HIDDEN;
    }
    /**
     * @return the defaultTurnPending
     */
    public static final int getDefaultTurnPending() {
        return DEFAULT_TURN_PENDING;
    }
    /**
     * @return the commentTurnValid
     */
    public static final int getCommentTurnValid() {
        return COMMENT_TURN_VALID;
    }
    /**
     * @return the commentTurnRejected
     */
    public static final int getCommentTurnRejected() {
        return COMMENT_TURN_REJECTED;
    }
    /**
     * @return the commentTurnHidden
     */
    public static final int getCommentTurnHidden() {
        return COMMENT_TURN_HIDDEN;
    }
    /**
     * @return the commentTurnPending
     */
    public static final int getCommentTurnPending() {
        return COMMENT_TURN_PENDING;
    }
    /**
     * @return the demandTurnValid
     */
    public static final int getDemandTurnValid() {
        return DEMAND_TURN_VALID;
    }
    /**
     * @return the demandTurnRejected
     */
    public static final int getDemandTurnRejected() {
        return DEMAND_TURN_REJECTED;
    }
    /**
     * @return the demandTurnHidden
     */
    public static final int getDemandTurnHidden() {
        return DEMAND_TURN_HIDDEN;
    }
    /**
     * @return the demandTurnPending
     */
    public static final int getDemandTurnPending() {
        return DEMAND_TURN_PENDING;
    }
    /**
     * @return the offerTurnValid
     */
    public static final int getOfferTurnValid() {
        return OFFER_TURN_VALID;
    }
    /**
     * @return the offerTurnRejected
     */
    public static final int getOfferTurnRejected() {
        return OFFER_TURN_REJECTED;
    }
    /**
     * @return the offerTurnHidden
     */
    public static final int getOfferTurnHidden() {
        return OFFER_TURN_HIDDEN;
    }
    /**
     * @return the offerTurnPending
     */
    public static final int getOfferTurnPending() {
        return OFFER_TURN_PENDING;
    }
    /**
     * @return the translationTurnValid
     */
    public static final int getTranslationTurnValid() {
        return TRANSLATION_TURN_VALID;
    }
    /**
     * @return the translationTurnRejected
     */
    public static final int getTranslationTurnRejected() {
        return TRANSLATION_TURN_REJECTED;
    }
    /**
     * @return the translationTurnHidden
     */
    public static final int getTranslationTurnHidden() {
        return TRANSLATION_TURN_HIDDEN;
    }
    /**
     * @return the translationTurnPending
     */
    public static final int getTranslationTurnPending() {
        return TRANSLATION_TURN_PENDING;
    }
    /**
     * @return the minInfluenceToUnkudos
     */
    public static final int getMinInfluenceToUnkudos() {
        return MIN_INFLUENCE_TO_UNKUDOS;
    }
    /**
     * @return the minInfluenceToKudos
     */
    public static final int getMinInfluenceToKudos() {
        return MIN_INFLUENCE_TO_KUDOS;
    }


}
