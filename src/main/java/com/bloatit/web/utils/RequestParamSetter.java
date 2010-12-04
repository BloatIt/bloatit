package com.bloatit.web.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import com.bloatit.common.FatalErrorException;
import com.bloatit.web.utils.Message.What;

/**
 * This class uses RequestParam annotation to set the parameters using a map of
 * String/String name value.
 */
public class RequestParamSetter {
    private Map<String, String> parameters;

    /**
     * Read a request and construct a RequestParamResult. (The actual work is done in
     * setValues.)
     * 
     * @param parameters is a map of name->value. It represent a request.
     */
    public RequestParamSetter(Map<String, String> parameters) {
        super();
        this.parameters = parameters;
    }

    /**
     * Set the value of the parameters annotated with RequestParam, using the parameters
     * name->value map and the annotations informations.
     * 
     * @param instance is the object that has attributes to fill.
     * @return Some messages on the filling process. The messages is basically a list of
     * strings with some level.
     * @see Messages
     */
    public Messages setValues(Object instance) {
        Class<?> aClass = instance.getClass();
        Messages messages = new Messages();
        for (Field f : aClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null) {
                try {
                    FieldParser fieldParser = new FieldParser(f, param);
                    fieldParser.performChangeInInstance(instance);
                } catch (ParamNotFoundException e) {
                    messages.addError(param, What.NOT_FOUND, e.getMessage());
                } catch (ConversionErrorException e) {
                    messages.addError(param, What.CONVERSION_ERROR, e.getMessage());
                }
            }
        }
        return messages;
    }

    /**
     * Store some error messages that could append during the reflexive procedure
     * "setValues".
     */
    public static class Messages extends ArrayList<Message>{
        private static final long serialVersionUID = -7080211414458545384L;

        private Messages() {
            super();
        }

        public boolean hasMessage(Message.Level level) {
            for (Message error : this) {
                if (error.getLevel() == level) {
                    return true;
                }
            }
            return false;
        }

        private void addError(RequestParam param, Message.What what, String error) {
            this.add(new Message(param.level(), what, error));
        }

    }

    /**
     * This exception is thrown when a parameter is not found in the map.
     */
    public static class ParamNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ParamNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown when a parameter is found, but cannot be converted to the
     * right type.
     */
    public static class ConversionErrorException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ConversionErrorException(String message) {
            super(message);
        }
    }

    /**
     * The fieldParser class perform the reflexive work for a field. It is an internal
     * class and should never be used outside of the RequestParamResult class.
     */
    public class FieldParser {
        private Field f;
        private RequestParam param;
        private String value;
        private String error;

        private FieldParser(Field f, RequestParam param) throws ConversionErrorException, ParamNotFoundException {
            super();
            this.f = f;
            this.param = param;
            this.error = param.message().value();
            this.value = getValue();
        }

        /**
         * Convert the value string to the right type and set the attribute of instance with this value.
         * @throws ConversionErrorException if the value is not convertible in the right type.
         */
        private void performChangeInInstance(Object instance) throws ConversionErrorException {
            try {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Constructor<? extends Loader<?>> constructor = findLoaderType().getConstructor();
                Loader<?> newInstance = constructor.newInstance();
                Object convert = newInstance.convert(value);

                f.set(instance, convert);
            } catch (Exception e) {
                throw new ConversionErrorException(error);
            }
        }

        /**
         * Find the right class to convert a String into some type.
         * @return a Loader class corresponding to the type
         */
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

        /**
         * Find the name of a parameter using annotation, and then find the value string the parameters map.
         * @return the value of the current field
         * @throws ParamNotFoundException if the value is not found in the parameters map.
         */
        private String getValue() throws ParamNotFoundException {
            String name;
            String value;
            if (param.name().isEmpty()) {
                name = f.getName();
            } else {
                name = param.name();
            }
            // TODO : translate the error !
            error = error.replaceAll("\\%param", name);
            value = parameters.get(name);

            if (value == null && (value = param.defaultValue()).isEmpty()) {
                value = null;
                error = error.replaceAll("\\%value", "null");
                throw new ParamNotFoundException(error);
            }
            error = error.replaceAll("\\%value", value);
            return value;
        }
    } // End of FieldParser
}
