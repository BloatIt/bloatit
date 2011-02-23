package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Translation;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestTranslation;

@XmlRootElement
public class RestTranslationList extends RestListBinder<RestTranslation, Translation> {
    public RestTranslationList(PageIterable<Translation> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "translations")
    @XmlElement(name = "translation")
    public RestTranslationList getTranslations() {
        return this;
    }
}

