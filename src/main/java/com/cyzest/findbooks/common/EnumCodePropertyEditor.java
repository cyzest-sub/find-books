package com.cyzest.findbooks.common;

import java.beans.PropertyEditorSupport;

public class EnumCodePropertyEditor<T extends Enum<T> & EnumCode> extends PropertyEditorSupport {

    private final Class<T> typeParameterClass;

    public EnumCodePropertyEditor(Class<T> typeParameterClass) {
        super();
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {

        if (text == null) {
            throw new NullPointerException("text is null");
        }

        T[] constants = this.typeParameterClass.getEnumConstants();

        for (T constant : constants) {
            if (constant.getCode().equals(text)) {
                setValue(constant);
                return;
            }
        }

        throw new IllegalArgumentException("No enum constant " + this.typeParameterClass.getCanonicalName() + "." + text);
    }

}
