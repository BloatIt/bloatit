package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.FileMetadata;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestFileMetadataList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestFileMetadata extends RestElement<FileMetadata>{ 
    private FileMetadata model;

    protected RestFileMetadata(FileMetadata model){
        this.model=model;
    }

    @REST(name = "filemetadatas", method = RequestMethod.GET)
    public static RestFileMetadata getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "filemetadatas", method = RequestMethod.GET)
    public static RestFileMetadataList getAll(){
        //TODO auto generated code
        return null;
    }

    FileMetadata getModel(){
        return model;
    }
}
