package com.bloatit.data.queries;

import java.util.Locale;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.DataTestUnit;

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
