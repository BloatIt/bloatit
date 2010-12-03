package com.bloatit.web.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.common.FatalErrorException;
import com.bloatit.web.utils.Message.What;

public class RequestParamResult {
    private List<Message> errors = new ArrayList<Message>();
    private Map<String, String> parameters;

    public RequestParamResult(Map<String, String> parameters) {
        super();
        this.parameters = parameters;
    }

    public List<Message> getMessages() {
        return errors;
    }

    public boolean hasMessage(Message.Level level) {
        for (Message error : errors) {
            if (error.getLevel() == level) {
                return true;
            }
        }
        return false;
    }

    public void parse(Object instance) {
        Class<?> aClass = instance.getClass();
        for (Field f : aClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null) {
                try {
                    FieldParser fieldParser = new FieldParser(f, param);
                    fieldParser.performChangeInInstance(instance);
                } catch (ParamNotFoundException e) {
                    addError(param, What.NOT_FOUND, e.getMessage());
                } catch (ConversionErrorException e) {
                    addError(param, What.CONVERSION_ERROR, e.getMessage());
                }

            }
        }
    }

    public class FieldParser {
        private Field f;
        private RequestParam param;
        private String value;
        private String error;

        public FieldParser(Field f, RequestParam param) throws ConversionErrorException {
            super();
            this.f = f;
            this.param = param;
            this.error = param.message();
            setValue();

        }

        public void performChangeInInstance(Object instance) throws ParamNotFoundException {
            try {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Constructor<? extends Loader<?>> constructor = findLoaderType().getConstructor();
                Loader<?> newInstance = constructor.newInstance();
                Object convert = newInstance.convert(value);

                f.set(instance, convert);
            } catch (Exception e) {
                throw new ParamNotFoundException(error);
            }
        }

        private Class<? extends Loader<?>> findLoaderType() {
            Class<? extends Loader<?>> valueClass;
            if (param.loader().equals(Loader.DefaultConvertor.class)) {
                // default one.
                valueClass = Loader.getLoaderClass(f.getType());
            } else {
                valueClass = param.loader();
            }

            // if the loader is not found there is a fatal error !
            // It can only arrive if there is a big programming error.
            if (valueClass == null) {
                throw new FatalErrorException("StringConvertor class not found.", null);
            }

            return valueClass;
        }

        private void setValue() throws ConversionErrorException {
            String name;
            if (param.name().isEmpty()) {
                name = f.getName();
            } else {
                name = param.name();
            }
            error = error.replaceAll("\\%param", name);
            value = parameters.get(name);

            if (value == null && (value = param.defaultValue()).isEmpty()) {
                value = null;
                error = error.replaceAll("\\%value", "null");
                throw new ConversionErrorException(error);
            }
            error = error.replaceAll("\\%value", value);
        }
    }

    private void addError(RequestParam param, Message.What what, String error) {
        errors.add(new Message(param.level(), what, error));
    }

    public static class ParamNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ParamNotFoundException(String message) {
            super(message);
        }
    }

    public static class ConversionErrorException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ConversionErrorException(String message) {
            super(message);
        }
    }
}
