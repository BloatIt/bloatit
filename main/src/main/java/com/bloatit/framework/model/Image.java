package com.bloatit.framework.model;

public interface Image {

    /**
     * Gives the identifier of the image. - If the image is local, its
     * identifier is its unique name on the bloatit server. - If the image is
     * distant, its identifier is its complet URI.
     * <p>
     * Should <i>always</i> be used <i>after</i> a call to isLocal.
     * </p>
     * 
     * @return the identifier of the image
     */
    public abstract String getIdentifier();

    public abstract boolean isNull();

}
