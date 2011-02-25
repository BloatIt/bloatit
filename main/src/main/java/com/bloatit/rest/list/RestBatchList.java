/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

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
@XmlRootElement(name = "batchs")
public class RestBatchList extends RestListBinder<RestBatch, Batch> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestBatchList() {
        super();
    }

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
    @XmlIDREF
    public List<RestBatch> getBatchs() {
        List<RestBatch> batchs = new ArrayList<RestBatch>();
        for (RestBatch batch : this) {
            batchs.add(batch);
        }
        return batchs;
    }
}
