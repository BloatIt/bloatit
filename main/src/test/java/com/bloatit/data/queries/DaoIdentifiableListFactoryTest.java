package com.bloatit.data.queries;

import java.util.Locale;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.DataTestUnit;

public class DaoIdentifiableListFactoryTest extends DataTestUnit {

    public void testDaoIdentifiableListFactory() {

        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);

        DaoIdentifiableQuery<DaoIdentifiable> factory = new DaoIdentifiableQuery<DaoIdentifiable>();
        factory.idEquals(demand.getId());
        assertEquals(demand, factory.uniqueResult());
    }

    public void testCreateCollection() {
        DaoIdentifiableQuery<DaoIdentifiable> factory = new DaoIdentifiableQuery<DaoIdentifiable>();

        assertTrue(factory.createCollection().size() != 0);
    }

}
