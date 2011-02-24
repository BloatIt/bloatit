package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.FileMetadata;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestFileMetadata;

/**
 * <p>
 * Wraps a list of FileMetadata into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of FileMetadata<br />
 * Example: 
 * 
 * <pre>
 * {@code <FileMetadatas>}
 *     {@code <FileMetadata name=FileMetadata1 />}
 *     {@code <FileMetadata name=FileMetadata2 />}
 * {@code </FileMetadatas>}
 * </pre>
 * <p>
 */ 
@XmlRootElement (name = "filemetadatas")
public class RestFileMetadataList extends RestListBinder<RestFileMetadata, FileMetadata> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestFileMetadataList() {
        super();
    }

    /**
     * Creates a RestFileMetadataList from a {@codePageIterable<FileMetadata>}
     *
     * @param collection the list of elements from the model
     */
    public RestFileMetadataList(PageIterable<FileMetadata> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "filemetadata")
    @XmlIDREF
    public List<RestFileMetadata> getFileMetadatas() {
        List<RestFileMetadata> filemetadatas = new ArrayList<RestFileMetadata>();
        for (RestFileMetadata filemetadata : this) {
            filemetadatas.add(filemetadata);
        }
        return filemetadatas;
    }
}

