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
package com.bloatit.data.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.DataTestUnit;

public class DaoIdentifiableListFactoryTest extends DataTestUnit {

    @Test
    public void testDaoIdentifiableListFactory() {

        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);

        final DaoIdentifiableQuery<DaoIdentifiable> factory = new DaoIdentifiableQuery<DaoIdentifiable>();
        factory.idEquals(feature.getId());
        assertEquals(feature, factory.uniqueResult());
    }

    @Test
    public void testCreateCollection() {
        final DaoIdentifiableQuery<DaoIdentifiable> factory = new DaoIdentifiableQuery<DaoIdentifiable>();

        assertTrue(factory.createCollection().size() != 0);
    }

}
