package com.bloatit.web.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.model.data.util.SessionManager;

public class TestQueryAnnotation {

    static public class DemandLoader extends Loader<Demand> {
        @Override
        public Demand convert(String data) {
            return DemandManager.getDemandById(Integer.valueOf(data));
        }
    }

    @RequestParam(defaultValue = "12", message = @tr("T'es un boulet !"))
    Integer value;
    @RequestParam(message = @tr("error 2"))
    String other;
    @RequestParam(message = @tr("error 3"))
    BigDecimal money;
    @RequestParam(loader = DemandLoader.class, message = @tr("Id demand not found."))
    Demand demand;

    protected TestQueryAnnotation() {
        super();
        Map<String, String> plop = new HashMap<String, String>();
        plop.put("value", "39");
        plop.put("other", "This is a string");
        plop.put("money", "0.8");
        plop.put("demand", "419");

        SessionManager.beginWorkUnit();

        RequestParamSetter processor = new RequestParamSetter(plop);
        RequestParamSetter.Messages errors = processor.setValues(this);

        System.out.println(value);
        System.out.println(other);
        System.out.println(money);
        System.out.println(demand);

        SessionManager.endWorkUnitAndFlush();

        for (Message error : errors) {
            System.out.println("error " + error.getMessage());
        }

    }

    public static void main(String[] args) {
        new TestQueryAnnotation();
    }

}
