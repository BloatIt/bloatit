package com.bloatit.framework.xcgiserver.postparsing;

/**
 * <p>
 * Simple class to describe a post parameter
 * </p>
 * <p>
 * Post parameters are described as name -> value
 * </p>
 */
public class PostParameter {
    private final String name;
    private final String value;

    public PostParameter(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
