package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.FileMetadata;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestFileMetadata;

@XmlRootElement
public class RestFileMetadataList extends RestListBinder<RestFileMetadata, FileMetadata> {
    public RestFileMetadataList(PageIterable<FileMetadata> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "filemetadatas")
    @XmlElement(name = "filemetadata")
    public RestFileMetadataList getFileMetadatas() {
        return this;
    }
}

