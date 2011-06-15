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
package com.bloatit.framework.webprocessor.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.utils.parameters.SessionParameters;

public abstract class UrlComponent extends UrlNode {
    private boolean isRegistered = false;
    private final List<UrlNode> nodes = new ArrayList<UrlNode>();
    
    protected abstract void doRegister();

    @Override
    public abstract UrlComponent clone();

    protected UrlComponent() {
        super();
    }

    @Override
    public final void constructUrl(final StringBuilder sb) {
        registerIfNotAlreadyDone();
        for (final UrlNode node : this) {
            node.constructUrl(sb);
        }
    }

    @Override
    public final void getParametersAsStrings(final Parameters parameters) {
        registerIfNotAlreadyDone();
        for (final UrlNode node : this) {
            node.getParametersAsStrings(parameters);
        }
    }

    protected final void register(final UrlNode node) {
        if (node == null) {
            throw new BadProgrammerException("Node musn't be null !");
        }
        nodes.add(node);
    }
    
    private final void registerIfNotAlreadyDone() {
        if (!isRegistered) {
            doRegister();
            isRegistered = true;
        }
    }

    @Override
    protected final void parseParameters(final Parameters params) {
        if (params == null) {
            return;
        }
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.parseParameters(params);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.framework.webprocessor.url.UrlNode#parseSessionParameters(com
     * .bloatit.framework.utils.SessionParameters)
     */
    @Override
    protected void parseSessionParameters(final SessionParameters params) {
        if (params == null) {
            return;
        }
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.parseSessionParameters(params);
        }
    }

    @Override
    @Deprecated
    public void addParameter(final String name, final String value) {
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.addParameter(name, value);
        }
    }

    @Override
    public final Iterator<UrlNode> iterator() {
        registerIfNotAlreadyDone();
        return nodes.iterator();
    }

    @Override
    public final Messages getMessages() {
        registerIfNotAlreadyDone();
        final Messages messages = new Messages();
        for (final UrlNode node : nodes) {
            messages.addAll(node.getMessages());
        }
        return messages;
    }

}
