package com.bloatit.web.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.web.utils.QueryParam.FromString;

public class TestQueryAnnotation {

    static public class DemandLoader implements FromString<Demand> {
        @Override
        public Demand convert(String data) {
            return DemandManager.getDemandById(Integer.valueOf(data));
        }
    }

    @QueryParam(error = "T'es un boulet ! ", defaultValue = "12")
    Integer value;
    @QueryParam(error = "error 2")
    String other;
    @QueryParam(error = "error 3")
    BigDecimal money;
    @QueryParam(loader = DemandLoader.class, error = "Id demand not found.")
    Demand demand;

    protected TestQueryAnnotation() {
        super();
        Map<String, String> plop = new HashMap<String, String>();
        plop.put("value", "39");
        plop.put("other", "This is a string");
        plop.put("money", "0.8");
        plop.put("demand", "419");

        SessionManager.beginWorkUnit();
        
        QueryParamProcessor processor = new QueryParamProcessor();
        processor.run(this, plop);

        System.out.println(value);
        System.out.println(other);
        System.out.println(money);
        System.out.println(demand);
        
        SessionManager.endWorkUnitAndFlush();

        for (String error : processor.getErrors()) {
            System.out.println("error " + error);
        }

    }

    public static void main(String[] args) {
        new TestQueryAnnotation();
    }

}
