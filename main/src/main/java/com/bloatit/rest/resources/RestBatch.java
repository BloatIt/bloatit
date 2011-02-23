package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Batch;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestBatchList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestBatch extends RestElement<Batch>{ 
    private Batch model;

    protected RestBatch(Batch model){
        this.model=model;
    }

    @REST(name = "batchs", method = RequestMethod.GET)
    public static RestBatch getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "batchs", method = RequestMethod.GET)
    public static RestBatchList getAll(){
        //TODO auto generated code
        return null;
    }

    Batch getModel(){
        return model;
    }
}
