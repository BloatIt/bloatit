package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Batch;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestBatch;

/**
 * <p>
 * Wraps a list of Batch into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Batch<br />
 * Example: 
 * 
 * <pre>
 * {@code <Batchs>}
 *     {@code <Batch name=Batch1 />}
 *     {@code <Batch name=Batch2 />}
 * {@code </Batchs>}
 * </pre>
 * <p>
 */ 
@XmlRootElement (name = "batchs")
public class RestBatchList extends RestListBinder<RestBatch, Batch> {
    /**
     * Creates a RestBatchList from a {@codePageIterable<Batch>}
     *
     * @param collection the list of elements from the model
     */
    public RestBatchList(PageIterable<Batch> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "batch")
    public List<RestBatch> getBatchs() {
        List<RestBatch> batchs = new ArrayList<RestBatch>();
        for (RestBatch batch : this) {
            batchs.add(batch);
        }
        return batchs;
    }
}

