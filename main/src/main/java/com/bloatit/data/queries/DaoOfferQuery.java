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

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoOffer;
import com.bloatit.data.SessionManager;

/**
 * A Factory to create query on the DB and return list of DaoDemand.
 */
public class DaoOfferQuery extends DaoKudosableQuery<DaoOffer> {

    /**
     * Instantiates a new dao demand list factory.
     */
    public DaoOfferQuery() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoOffer.class));
    }

    /**
     * Add a WHERE close restricting the <code>amount</code> value of the
     * returning offers. For example if you want your query to return only the
     * offers that have less than 42 €, you can call:
     *
     * <pre>
     * DaoOfferListFactory factory = new DaoOfferListFactory();
     * factory.amount(Comparator.LESS, 42);
     * PageIterable&lt;DaoOffer&gt; offers = factory.createCollection();
     * </pre>
     *
     * @param cmp the cmp.
     * @param value the value
     */
    public void amount(final Comparator cmp, final BigDecimal value) {
        add(createNbCriterion(cmp, "amount", value));
    }

    public void withBatches() {
        add(Restrictions.sizeGe("batches", 1));
    }

    public void withoutBatches() {
        add(Restrictions.sizeEq("batches", 1));
    }

    public void hasExpired() {
        add(Restrictions.lt("expirationDate", new Date()));
    }

}
