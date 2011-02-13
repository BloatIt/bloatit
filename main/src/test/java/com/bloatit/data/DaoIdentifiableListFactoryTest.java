package com.bloatit.data;

import java.util.Locale;

public class DaoIdentifiableListFactoryTest extends DataTestUnit {

    public void testDaoIdentifiableListFactory() {

        final DaoDemand demand = DaoDemand.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);

        DaoIdentifiableListFactory<DaoIdentifiable> factory = new DaoIdentifiableListFactory<DaoIdentifiable>();
        factory.idEquals(demand.getId());
        assertEquals(demand, factory.uniqueResult());
    }

    public void testCreateCollection() {
        DaoIdentifiableListFactory<DaoIdentifiable> factory = new DaoIdentifiableListFactory<DaoIdentifiable>();

        assertTrue(factory.createCollection().size() != 0);
    }

}
