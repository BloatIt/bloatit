//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.features.create;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.actions.WebProcess;
import com.bloatit.web.url.ChooseFeatureTypePageUrl;
import com.bloatit.web.url.CreateFeatureAndOfferPageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.CreateFeatureProcessUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("feature/create/process")
public class CreateFeatureProcess extends WebProcess {

    
    private FeatureCreationType type;

    public enum FeatureCreationType {
        CREATE,
        CREATE_AND_MAKE_OFFER,
    }
    
    public CreateFeatureProcess(final CreateFeatureProcessUrl url) {
        super(url);
        type = FeatureCreationType.CREATE;
    }

    @Override
    protected Url doProcess() {
        return new ChooseFeatureTypePageUrl(this);
    }
    
    @Override
    protected Url doProcessErrors() {
        return new IndexPageUrl();
    }

    @Override
    public void doLoad() {
    }

    public void setType(FeatureCreationType type) {
        this.type = type;
        
    }

    public Url getCreationPage() {
        switch(type) {
            case CREATE:
                return new CreateFeaturePageUrl(this);
            case CREATE_AND_MAKE_OFFER:
                return new CreateFeatureAndOfferPageUrl(this);
        }
        throw new BadProgrammerException("Unreachable code");
    }

    
    
}
