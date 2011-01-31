package com.bloatit.framework.scgiserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;

class LazyLoaders {

    public abstract static class LazyComponent<T> {
        private T value = null;
        private final String name;

        public LazyComponent(final String name) {
            this.name = name;
        }

        public final T getValue(final Map<String, String> env) {
            if (value != null) {
                return value;
            }
            final String stringValue = env.get(name);
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
        public LazyMap(final String name) {
            super(name);
        }

        @Override
        public Map<String, String> convert(final String stringValue) {
            final HashMap<String, String> map = new HashMap<String, String>();
            final String[] namedValues = stringValue.split(";");
            for (final String namedValue : namedValues) {
                final String[] aValue = namedValue.split("=");
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

        public LazyInt(final String name) {
            super(name);
        }

        @Override
        public Integer convert(final String stringValue) {
            try {
                return Integer.valueOf(stringValue);
            } catch (final Exception e) {
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

        public LazyStringList(final String name, final String separator) {
            super(name);
            this.separator = separator;
        }

        @Override
        public List<String> convert(final String stringValue) {
            return Arrays.asList(stringValue.split(separator));
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> getDefault() {
            return Collections.EMPTY_LIST;
        }
    }

    public static final class LazyString extends LazyComponent<String> {
        public LazyString(final String name) {
            super(name);
        }

        @Override
        public String convert(final String stringValue) {
            return stringValue;
        }

        @Override
        public String getDefault() {
            return "";
        }
    }

}
