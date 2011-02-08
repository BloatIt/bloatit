package com.bloatit.framework.webserver.mime.filenaming;

import java.util.UUID;

/**
 * A File name generator that generates a random name based on UUID and adds to
 * it the extension of the original filename
 */
public class UUIDFileNameGenerator implements FileNamingGenerator {
    private static final char SEPARATOR = '.';

    @Override
    public String generateName(String fileName) {
        final UUID uuid = UUID.randomUUID();
        int separatorIndex = fileName.lastIndexOf(SEPARATOR);
        if (separatorIndex == -1) {
            return uuid.toString();
        }

        return uuid.toString() + fileName.substring(separatorIndex, fileName.length());
    }
}
