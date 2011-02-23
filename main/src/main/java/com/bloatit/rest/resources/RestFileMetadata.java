package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.FileMetadata;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestFileMetadataList;

@XmlRootElement
public class RestFileMetadata extends RestElement<FileMetadata> {
    private final FileMetadata model;

    protected RestFileMetadata(final FileMetadata model) {
        this.model = model;
    }

    @REST(name = "filemetadatas", method = RequestMethod.GET)
    public static RestFileMetadata getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "filemetadatas", method = RequestMethod.GET)
    public static RestFileMetadataList getAll() {
        // TODO auto generated code
        return null;
    }

    FileMetadata getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
