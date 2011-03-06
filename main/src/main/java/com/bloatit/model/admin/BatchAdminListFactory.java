package com.bloatit.model.admin;

import java.math.BigDecimal;

import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoBatch.BatchState;
import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.data.queries.DaoBatchQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Batch;
import com.bloatit.model.lists.BatchList;

public class BatchAdminListFactory extends IdentifiableAdminListFactory<DaoBatch, Batch> {

    public BatchAdminListFactory() {
        super(new DaoBatchQuery());
    }

    @Override
    protected DaoBatchQuery getfactory() {
        return (DaoBatchQuery) super.getfactory();
    }

    @Override
    public PageIterable<Batch> list() {
        return new BatchList(getfactory().createCollection());
    }

    public void amount(Comparator cmp, BigDecimal value) {
        getfactory().amount(cmp, value);
    }

    public void stateEquals(BatchState state) {
        getfactory().stateEquals(state);
    }

    public void withRelease() {
        getfactory().withRelease();
    }

    public void withoutRelease() {
        getfactory().withoutRelease();
    }

}
