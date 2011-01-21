/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.managers;

import com.bloatit.framework.Comment;
import com.bloatit.framework.Kudosable;
import com.bloatit.framework.Offer;
import com.bloatit.framework.Translation;
import com.bloatit.framework.demand.Demand;
import com.bloatit.model.data.DBRequests;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoOffer;
import com.bloatit.model.data.DaoTranslation;

public final class KudosableManager {

    private KudosableManager() {
        // Desactivate default ctor
    }

    public static Kudosable getById(final Integer id) {

        // Try with comment
        final DaoComment comment = DBRequests.getById(DaoComment.class, id);
        if (comment != null) {
            return Comment.create(comment);
        }

        // Try with demand
        final DaoDemand demand = DBRequests.getById(DaoDemand.class, id);
        if (demand != null) {
            return Demand.create(demand);
        }

        // Try with offer
        final DaoOffer offer = DBRequests.getById(DaoOffer.class, id);
        if (offer != null) {
            return Offer.create(offer);
        }

        // Try with translation
        final DaoTranslation translation = DBRequests.getById(DaoTranslation.class, id);
        if (translation != null) {
            return Translation.create(translation);
        }

        return null;

    }

}
