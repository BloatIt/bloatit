package com.bloatit.framework.webserver.mime.filenaming;

import java.util.UUID;

public class UUIDFileNameGenerator implements FileNamingGenerator{
    private static final char SEPARATOR = '.';

    @Override
    public String generateName(String fileName) {
        final UUID uuid = UUID.randomUUID();
        int separatorIndex = fileName.lastIndexOf(SEPARATOR);
        
        return uuid.toString() + fileName.substring(separatorIndex, fileName.length());
    }
    
}
