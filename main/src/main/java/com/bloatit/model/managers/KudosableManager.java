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
package com.bloatit.model.managers;

import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoTranslation;
import com.bloatit.model.Comment;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;
import com.bloatit.model.demand.Demand;

public final class KudosableManager {

    private KudosableManager() {
        // Desactivate default ctor
    }

    public static Kudosable<?> getById(final Integer id) {

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
