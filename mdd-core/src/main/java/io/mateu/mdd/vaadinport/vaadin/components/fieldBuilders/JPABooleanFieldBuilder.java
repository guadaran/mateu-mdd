package io.mateu.mdd.vaadinport.vaadin.components.fieldBuilders;

import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import io.mateu.mdd.core.annotations.Help;
import io.mateu.mdd.core.interfaces.AbstractStylist;
import io.mateu.mdd.core.reflection.FieldInterfaced;
import io.mateu.mdd.core.util.Helper;
import io.mateu.mdd.core.data.MDDBinder;

import java.util.List;
import java.util.Map;

public class JPABooleanFieldBuilder extends AbstractFieldBuilder {


    public boolean isSupported(FieldInterfaced field) {
        return Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType());
    }

    public void build(FieldInterfaced field, Object object, Layout container, MDDBinder binder, Map<HasValue, List<Validator>> validators, AbstractStylist stylist, Map<FieldInterfaced, Component> allFieldContainers, boolean forSearchFilter) {


        if (forSearchFilter) {

            //todo: contemplar los casos si, no, indiferente

        } else {

            CheckBox cb;
            container.addComponent(cb = new CheckBox());

            if (allFieldContainers.size() == 0) cb.focus();

            cb.setCaption(Helper.capitalize(field.getName()));

            if (field.isAnnotationPresent(Help.class) && !Strings.isNullOrEmpty(field.getAnnotation(Help.class).value())) cb.setDescription(field.getAnnotation(Help.class).value());


            bind(binder, cb, field);

        }

    }

    protected void bind(MDDBinder binder, CheckBox cb, FieldInterfaced field) {
        binder.bind(cb, field.getName());
    }
}