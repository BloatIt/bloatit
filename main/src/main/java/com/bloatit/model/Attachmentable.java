/*
 * 
 */
package com.bloatit.model;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;

public interface Attachmentable {
    /**
     * Gets the files associated with this user content.
     * 
     * @return the files
     */
    PageIterable<FileMetadata> getFiles();

    /**
     * Associate a file with this user content.
     * 
     * @param file the file to add.
     * @throws UnauthorizedOperationException
     */
    void addFile(FileMetadata file) throws UnauthorizedOperationException;

    boolean canAddFile();
}
