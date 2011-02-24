package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Contribution;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestContribution;

/**
 * <p>
 * Wraps a list of Contribution into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Contribution<br />
 * Example:
 *
 * <pre>
 * {@code <Contributions>}
 *     {@code <Contribution name=Contribution1 />}
 *     {@code <Contribution name=Contribution2 />}
 * {@code </Contributions>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "contributions")
public class RestContributionList extends RestListBinder<RestContribution, Contribution> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestContributionList() {
        super();
    }

    /**
     * Creates a RestContributionList from a {@codePageIterable<Contribution>}
     *
     * @param collection the list of elements from the model
     */
    public RestContributionList(PageIterable<Contribution> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "contribution")
    @XmlIDREF
    public List<RestContribution> getContributions() {
        List<RestContribution> contributions = new ArrayList<RestContribution>();
        for (RestContribution contribution : this) {
            contributions.add(contribution);
        }
        return contributions;
    }
}
