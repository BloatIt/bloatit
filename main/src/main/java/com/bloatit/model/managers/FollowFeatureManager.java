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
package com.bloatit.model.managers;

import com.bloatit.data.DaoFollowFeature;
import com.bloatit.data.DaoRelease;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.FollowFeature;
import com.bloatit.model.lists.ListBinder;

/**
 *
 */
public class FollowFeatureManager {
    public static FollowFeature getById(final Integer id) {
        return FollowFeature.create(DBRequests.getById(DaoFollowFeature.class, id));
    }

    public static PageIterable<FollowFeature> getAll() {
        return  new ListBinder<FollowFeature, DaoFollowFeature>(DBRequests.getAll(DaoFollowFeature.class));
    }

    public static int getReleaseCount() {
        return DBRequests.count(DaoRelease.class);
    }

}
