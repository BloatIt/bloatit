package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import com.bloatit.common.FatalErrorException;
import com.bloatit.web.utils.Loader;
import com.bloatit.web.utils.Loaders;
import com.bloatit.web.utils.Message;
import com.bloatit.web.utils.Message.What;
import com.bloatit.web.utils.PageComponent;
import com.bloatit.web.utils.RequestParam;

public class UrlReader {

    private final Url url;

    /**
     * TODO
     */
    public UrlReader(Url url) {
        super();
        this.url = url;
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
    public Messages setValues(Object instance) {
        Class<?> aClass = instance.getClass();
        Messages messages = new Messages();
        for (Field f : aClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null) {
                try {
                    FieldParser fieldParser = new FieldParser(f, param, url.getParameters());
                    fieldParser.performChangeInInstance(instance);
                } catch (ParamNotFoundException e) {
                    messages.addError(param, What.NOT_FOUND, e.getMessage());
                } catch (ConversionErrorException e) {
                    messages.addError(param, What.CONVERSION_ERROR, e.getMessage());
                }
            } else if (f.getAnnotation(PageComponent.class) != null) {
                try {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    messages.addAll(setValues(f.get(instance)));
                } catch (IllegalArgumentException e) {
                    throw new FatalErrorException("Cannot parse the pageComponent", e);
                } catch (IllegalAccessException e) {
                    throw new FatalErrorException("Cannot parse the pageComponent", e);
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
     * This exception is thrown when a parameter is found, but cannot be
     * converted to the
     * right type.
     */
    public static class ConversionErrorException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ConversionErrorException(String message) {
            super(message);
        }
    }

    /**
     * The fieldParser class perform the reflexive work for a field. It is an
     * internal
     * class and should never be used outside of the RequestParamResult class.
     */
    public static class FieldParser {
        private Field f;
        private RequestParam param;
        private String value;
        private String name;
        private String error;

        public FieldParser(Field f, RequestParam param, Map<String, String> parameters) throws ParamNotFoundException {
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
        private void performChangeInInstance(Object instance) throws ConversionErrorException {
            try {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Constructor<? extends Loader<?>> constructor = findLoaderType().getConstructor();
                Loader<?> newInstance = constructor.newInstance();
                Object convert = newInstance.fromString(value);

                f.set(instance, convert);
            } catch (Exception e) {
                throw new ConversionErrorException(error);
            }
        }

        /**
         * Find the right class to convert a String into some type.
         * 
         * @return a Loader class corresponding to the type
         */
        private Class<? extends Loader<?>> findLoaderType() {
            Class<? extends Loader<?>> valueClass;
            if (param.loader().equals(Loaders.DefaultConvertor.class)) {
                // default one.
                valueClass = Loaders.getLoaderClass(f.getType());
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
         * Find the name (and set it) of a parameter using annotation, and then
         * find the
         * value string the parameters map.
         * 
         * @return the value of the current field
         * @throws ParamNotFoundException if the value is not found in the
         *         parameters map.
         */
        private String findValueAndSetName(Map<String, String> parameters) throws ParamNotFoundException {
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
