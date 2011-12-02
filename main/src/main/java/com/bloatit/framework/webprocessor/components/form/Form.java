package com.bloatit.framework.webprocessor.components.form;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.LengthConstraint;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.generator.ParameterDescription;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlParameter;

public class Form {

    public static class FieldData {
        private FormField formField;
        private FormComment comment;
        private UrlParameter<?, ?> targetParam;
        private UrlParameter<?, ?> sessionParam;
        private MinConstraint min;
        private MaxConstraint max;
        private LengthConstraint length;
        private boolean isOptional = false;
        private Class<?> clazz;

        public FieldData() {
            super();
        }

        public boolean isFinished() {
            return formField != null && targetParam != null;
        }

        public final void setFormField(FormField formField) {
            this.formField = formField;
        }

        public final void setComment(FormComment comment) {
            this.comment = comment;
        }

        public final void setTargetParam(UrlParameter<?, ?> param) {
            this.targetParam = param;
        }

        public final void setMin(MinConstraint min) {
            this.min = min;
        }

        public final void setMax(MaxConstraint max) {
            this.max = max;
        }

        public final void setLength(LengthConstraint length) {
            this.length = length;
        }

        public final void setOptional(boolean isOptional) {
            this.isOptional = isOptional;
        }

        public String getLabel() {
            return formField.label().value();
        }

        public boolean isAutocomplete() {
            return formField.autocomplete();
        }

        public UrlParameter<?, ?> getTargetParameter() {
            return targetParam;
        }

        public String getComment() {
            if (comment == null) {
                return null;
            }
            return comment.value().value();
        }

        public final Integer getMin() {
            if (min == null) {
                return null;
            }
            return min.min();
        }

        public final Integer getMax() {
            if (max == null) {
                return null;
            }
            return max.max();
        }

        public final Integer getLength() {
            if (length == null) {
                return null;
            }
            return length.length();
        }

        public final boolean isOptional() {
            return isOptional;
        }

        public UrlParameter<?, ?> getSessionParam() {
            return sessionParam;
        }

        public void setSessionParam(UrlParameter<?, ?> sessionParam) {
            this.sessionParam = sessionParam;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

    }

    private Map<String, FieldData> fields = new HashMap<String, FieldData>();

    // TODO translate
    @SuppressWarnings("unchecked")
    public Form(Class<? extends Action> target, Url actionUrl) {

        while (!Action.class.equals(target) && target != null) {
            for (Field f : target.getDeclaredFields()) {
                FieldData data = new FieldData();
                String name = null;
                for (Annotation annotation : f.getAnnotations()) {
                    if (annotation.annotationType().equals(RequestParam.class)) {
                        name = ParameterDescription.computeName(((RequestParam) annotation).name(),
                                                                f.getName(),
                                                                ((RequestParam) annotation).role(),
                                                                target.getSimpleName());
                    } else if (annotation.annotationType().equals(FormField.class)) {
                        data.setFormField((FormField) annotation);
                    } else if (annotation.annotationType().equals(FormComment.class)) {
                        data.setComment((FormComment) annotation);
                    } else if (annotation.annotationType().equals(LengthConstraint.class)) {
                        data.setLength((LengthConstraint) annotation);
                    } else if (annotation.annotationType().equals(MaxConstraint.class)) {
                        data.setMax((MaxConstraint) annotation);
                    } else if (annotation.annotationType().equals(MinConstraint.class)) {
                        data.setMin((MinConstraint) annotation);
                    } else if (annotation.annotationType().equals(Optional.class)) {
                        data.setOptional(true);
                    }
                }

                data.setTargetParam(actionUrl.getParameter(name));
                data.setSessionParam(Context.getSession().getParameters().pick(name));
                data.setClazz(f.getType());

                if (data.isFinished() && name != null) {
                    fields.put(name, data);
                }
            }

            target = ((Class<? extends Action>) target.getSuperclass());
        }
    }

    public HtmlFormField add(HtmlBranch form, HtmlFormField b) {
        FieldData data = fields.get(b.getName());

        if (data == null) {
            throw new BadProgrammerException("The parameter '" + b.getName() + "' is not found.");
        }
        form.add(b);
        if (data.getSessionParam() != null) {
            b.addErrorMessages(data.getSessionParam().getMessages());
            b.setDefaultValue(data.getSessionParam().getSuggestedValue());
        } else {
            b.setDefaultValue(data.getTargetParameter().getSuggestedValue());
        }
        b.setLabel(Context.tr(data.getLabel()));
        if (data.getComment() != null) {
            b.setComment(Context.tr(data.getComment()));
        }
        if (!data.isOptional()) {
            b.addAttribute("required", "required");
        }
        if (data.getMax() != null) {
            b.addAttribute("max", data.getMax().toString());
            if (data.getMax() > 0 && data.getClazz().equals(String.class)) {
                b.addAttribute("maxlength", data.getMax().toString());
            }
        }
        if (data.getMin() != null) {
            b.addAttribute("min", data.getMin().toString());
        }
        if (data.getLength() != null) {
            b.addAttribute("size", data.getLength().toString());
        }

        return b;
    }

    public boolean suggestedValueChanged(HtmlFormField b) {
        return suggestedValueChanged(b.getName());
    }

    public boolean suggestedValueChanged(String name) {
        FieldData data = fields.get(name);
        return data != null && data.getSessionParam() != null
                && !data.getSessionParam().getSuggestedValue().equals(data.getTargetParameter().getSuggestedValue());
    }

}
