package com.bloatit.web.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;

public class LazyLoaders {

    public static abstract class LazyComponent<T> {
        private T value = null;
        private final String name;

        public LazyComponent(String name) {
            this.name = name;
        }

        public final T getValue(Map<String, String> env) {
            if (value != null) {
                return value;
            }
            String stringValue = env.get(name);
            if (stringValue != null) {
                value = convert(stringValue);
                return value;
            }
            return getDefault();
        }

        public abstract T convert(String stringValue);

        public abstract T getDefault();

    }

    public static final class LazyMap extends LazyComponent<Map<String, String>> {
        public LazyMap(String name) {
            super(name);
        }

        @Override
        public Map<String, String> convert(String stringValue) {
            HashMap<String, String> map = new HashMap<String, String>();
            String[] namedValues = stringValue.split(";");
            for (String namedValue : namedValues) {
                String[] aValue = namedValue.split("=");
                if (aValue.length == 2) {
                    map.put(aValue[0].trim(), aValue[1].trim());
                } else {
                    Log.web().error("Malformed cookie value: " + namedValue);
                }
            }
            return map;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, String> getDefault() {
            return Collections.EMPTY_MAP;
        }
    }

    public static final class LazyInt extends LazyComponent<Integer> {

        public LazyInt(String name) {
            super(name);
        }

        @Override
        public Integer convert(String stringValue) {
            try {
                return Integer.valueOf(stringValue);
            } catch (Exception e) {
                Log.web().error("Malformed integer: " + stringValue);
            }
            return null;
        }

        @Override
        public Integer getDefault() {
            return 0;
        }

    }

    public static final class LazyStringList extends LazyComponent<List<String>> {

        private final String separator;

        public LazyStringList(String name, String separator) {
            super(name);
            this.separator = separator;
        }

        @Override
        public List<String> convert(String stringValue) {
            return Arrays.asList(stringValue.split(separator));
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> getDefault() {
            return Collections.EMPTY_LIST;
        }
    }

    public static final class LazyString extends LazyComponent<String> {
        public LazyString(String name) {
            super(name);
        }

        @Override
        public String convert(String stringValue) {
            return stringValue;
        }

        @Override
        public String getDefault() {
            return "";
        }
    }

}
