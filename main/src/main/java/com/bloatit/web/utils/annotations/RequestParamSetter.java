package com.bloatit.web.utils.annotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam;

/**
 * This class uses RequestParam annotation to set the parameters using a map of
 * String/String name value.
 */
public class RequestParamSetter {
    private final Map<String, String> parameters;

    /**
     * Read a request and construct a RequestParamResult. (The actual work is
     * done in
     * setValues.)
     * 
     * @param parameters is a map of name->value. It represent a request.
     */
    public RequestParamSetter(final Map<String, String> parameters) {
        super();
        this.parameters = parameters;
    }

    /**
     * Set the value of the parameters annotated with RequestParam, using the
     * parameters
     * name->value map and the annotations informations.
     * 
     * @param instance is the object that has attributes to fill.
     * @return Some messages on the filling process. The messages is basically a
     *         list of
     *         strings with some level.
     * @see Messages
     */
    public Messages setValues(final Object instance) {
        final Class<?> aClass = instance.getClass();
        final Messages messages = new Messages();
        for (final Field f : aClass.getDeclaredFields()) {
            final RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null) {
                try {
                    final FieldParser fieldParser = new FieldParser(f, param, parameters);
                    fieldParser.performChangeInInstance(instance);
                } catch (final ParamNotFoundException e) {
                    messages.addError(param, What.NOT_FOUND, e.getMessage());
                } catch (final ConversionErrorException e) {
                    messages.addError(param, What.CONVERSION_ERROR, e.getMessage());
                }
            }
        }
        return messages;
    }

    /**
     * Store some error messages that could append during the reflexive
     * procedure
     * "setValues".
     */
    public static class Messages extends ArrayList<Message> {
        private static final long serialVersionUID = -7080211414458545384L;

        public Messages() {
            super();
        }

        public boolean hasMessage(final Message.Level level) {
            for (final Message error : this) {
                if (error.getLevel() == level) {
                    return true;
                }
            }
            return false;
        }

        private void addError(final RequestParam param, final Message.What what, final String error) {
            this.add(new Message(param.level(), what, error));
        }

    }

    /**
     * This exception is thrown when a parameter is not found in the map.
     */
    public static class ParamNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParamNotFoundException(final String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown when a parameter is found, but cannot be
     * converted to the
     * right type.
     */
    public static class ConversionErrorException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ConversionErrorException(final String message) {
            super(message);
        }
    }

    /**
     * The fieldParser class perform the reflexive work for a field. It is an
     * internal
     * class and should never be used outside of the RequestParamResult class.
     */
    public static class FieldParser {
        private final Field f;
        private final RequestParam param;
        private final String value;
        private String name;
        private String error;

        public FieldParser(final Field f, final RequestParam param, final Map<String, String> parameters) throws ParamNotFoundException {
            super();
            this.f = f;
            this.param = param;
            this.error = param.message().value();
            this.value = findValueAndSetName(parameters);
        }

        /**
         * Convert the value string to the right type and set the attribute of
         * instance
         * with this value.
         * 
         * @throws ConversionErrorException if the value is not convertible in
         *         the right
         *         type.
         */
        private void performChangeInInstance(final Object instance) throws ConversionErrorException {
            try {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                final Object convert = Loaders.fromStr(f.getType(), value);

                f.set(instance, convert);
            } catch (final Exception e) {
                e.printStackTrace();
                throw new ConversionErrorException(error);
            }
        }

        /**
         * Find the name (and set it) of a parameter using annotation, and then
         * find the
         * value string the parameters map.
         * 
         * @return the value of the current field
         * @throws ParamNotFoundException if the value is not found in the
         *         parameters map.
         */
        private String findValueAndSetName(final Map<String, String> parameters) throws ParamNotFoundException {
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

        public String getName() {
            return name;
        }

        public String getvalue() {
            return value;
        }
    } // End of FieldParser
}
