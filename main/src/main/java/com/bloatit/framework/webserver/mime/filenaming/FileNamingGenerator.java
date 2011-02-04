package com.bloatit.framework.webserver.mime.filenaming;

/**
 * A simple interface to describe file name generators that will be used to save
 * uploaded files
 */
public interface FileNamingGenerator {
    /**
     * <p>
     * Generate a file name
     * </p>
     * 
     * @param fileName
     *            the fileName of the file to save
     * @return the file name
     */
    public String generateName(String fileName);
}
