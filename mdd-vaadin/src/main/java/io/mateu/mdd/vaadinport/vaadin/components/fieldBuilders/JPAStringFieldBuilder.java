package io.mateu.mdd.vaadinport.vaadin.components.fieldBuilders;

import com.google.common.base.Strings;
import com.vaadin.data.*;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.mateu.mdd.core.annotations.FullWidth;
import io.mateu.mdd.core.annotations.Help;
import io.mateu.mdd.core.annotations.UseRadioButtons;
import io.mateu.mdd.core.data.MDDBinder;
import io.mateu.mdd.core.MDD;
import io.mateu.mdd.core.annotations.*;
import io.mateu.mdd.core.data.MDDBinder;
import io.mateu.mdd.core.dataProviders.JPQLListDataProvider;
import io.mateu.mdd.core.interfaces.AbstractStylist;
import io.mateu.mdd.core.reflection.FieldInterfaced;
import io.mateu.mdd.core.reflection.ReflectionHelper;
import io.mateu.mdd.core.util.Helper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JPAStringFieldBuilder extends AbstractFieldBuilder {


    public boolean isSupported(FieldInterfaced field) {
        return String.class.equals(field.getType());
    }

    public void build(FieldInterfaced field, Object object, Layout container, MDDBinder binder, Map<HasValue, List<Validator>> validators, AbstractStylist stylist, Map<FieldInterfaced, Component> allFieldContainers, boolean forSearchFilter) {

        Method mdp = ReflectionHelper.getMethod(field.getDeclaringClass(), ReflectionHelper.getGetter(field.getName()) + "DataProvider");

        if (field.isAnnotationPresent(ValueClass.class) || field.isAnnotationPresent(DataProvider.class) || mdp != null) {

            Component tf = null;
            HasValue hv = null;

            if (field.isAnnotationPresent(UseRadioButtons.class)) {

                RadioButtonGroup rbg;
                container.addComponent(tf = rbg = new RadioButtonGroup());

                hv = rbg;

                //AbstractBackendDataProvider
                //FetchItemsCallback
                //newItemProvider



                if (mdp != null) {

                    try {
                        ((HasDataProvider)tf).setDataProvider((com.vaadin.data.provider.DataProvider) mdp.invoke(object));
                    } catch (Exception e) {
                        MDD.alert(e);
                    }

                } else if (field.isAnnotationPresent(ValueClass.class)) {

                    ValueClass a = field.getAnnotation(ValueClass.class);

                    ((HasDataProvider)tf).setDataProvider(new JPQLListDataProvider(a.value()));

                } else if (field.isAnnotationPresent(DataProvider.class)) {

                    try {

                        DataProvider a = field.getAnnotation(DataProvider.class);

                        ((HasDataProvider)tf).setDataProvider(a.dataProvider().newInstance());

                        rbg.setItemCaptionGenerator(a.itemCaptionGenerator().newInstance());

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        Helper.notransact((em) -> rbg.setDataProvider(new JPQLListDataProvider(em, field)));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    FieldInterfaced fName = ReflectionHelper.getNameField(field.getType());
                    if (fName != null) rbg.setItemCaptionGenerator((i) -> {
                        try {
                            return "" + ReflectionHelper.getValue(fName, i);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return "Error";
                    });

                }

                if (!forSearchFilter) rbg.setRequiredIndicatorVisible(field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(NotEmpty.class));

            } else {

                ComboBox cb;
                container.addComponent(tf = cb = new ComboBox());
                cb.addStyleName("combo");

                if (allFieldContainers.size() == 0) cb.focus();

                hv = cb;


                //AbstractBackendDataProvider
                //FetchItemsCallback
                //newItemProvider


                if (mdp != null) {

                    try {
                        cb.setDataProvider((com.vaadin.data.provider.DataProvider) mdp.invoke(object), f -> new SerializablePredicate() {
                            @Override
                            public boolean test(Object o) {
                                String s = (String) f;
                                return  o != null && (Strings.isNullOrEmpty(s) || cb.getItemCaptionGenerator().apply(o).toLowerCase().contains(((String) s).toLowerCase()));
                            }
                        });
                    } catch (Exception e) {
                        MDD.alert(e);
                    }

                } else if (field.isAnnotationPresent(ValueClass.class)) {

                    ValueClass a = field.getAnnotation(ValueClass.class);

                    cb.setDataProvider(new JPQLListDataProvider(a.value()));

                } else if (field.isAnnotationPresent(DataProvider.class)) {

                    try {

                        DataProvider a = field.getAnnotation(DataProvider.class);

                        cb.setDataProvider(a.dataProvider().newInstance(), f -> new SerializablePredicate() {
                            @Override
                            public boolean test(Object o) {
                                String s = (String) f;
                                return  o != null && (Strings.isNullOrEmpty(s) || cb.getItemCaptionGenerator().apply(o).toLowerCase().contains(((String) s).toLowerCase()));
                            }
                        });

                        cb.setItemCaptionGenerator(a.itemCaptionGenerator().newInstance());

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        Helper.notransact((em) -> cb.setDataProvider(new JPQLListDataProvider(em, field), f -> new SerializablePredicate() {
                            @Override
                            public boolean test(Object o) {
                                String s = (String) f;
                                return  o != null && (Strings.isNullOrEmpty(s) || cb.getItemCaptionGenerator().apply(o).toLowerCase().contains(((String) s).toLowerCase()));
                            }
                        }));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    FieldInterfaced fName = ReflectionHelper.getNameField(field.getType());
                    if (fName != null) cb.setItemCaptionGenerator((i) -> {
                        try {
                            return "" + ReflectionHelper.getValue(fName, i);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return "Error";
                    });

                }

                if (!forSearchFilter) cb.setRequiredIndicatorVisible(field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(NotEmpty.class));

            }

            allFieldContainers.put(field, tf);

            if (container.getComponentCount() > 0) tf.setCaption(ReflectionHelper.getCaption(field));


            bind(binder, hv, field, forSearchFilter);

        } else {

            TextField tf;
            container.addComponent(tf = (field.isAnnotationPresent(Password.class)) ? new PasswordField() : new TextField());
            tf.setValueChangeMode(ValueChangeMode.BLUR);


            if (String.class.equals(field.getType())) {
                if (!forSearchFilter) {
                    if (field.isAnnotationPresent(FullWidth.class)) tf.setWidth("100%");
                    else tf.setWidth(370, Sizeable.Unit.PIXELS);
                }
            } else {
                tf.setWidth(100, Sizeable.Unit.PIXELS);
                tf.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
            }



            if (allFieldContainers.size() == 0) tf.focus();

            allFieldContainers.put(field, tf);

            if (container.getComponentCount() > 0) tf.setCaption(ReflectionHelper.getCaption(field));

            if (!forSearchFilter) {

                tf.setRequiredIndicatorVisible(field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(NotEmpty.class));

            }

            if (field.isAnnotationPresent(Help.class) && !Strings.isNullOrEmpty(field.getAnnotation(Help.class).value()))
                tf.setDescription(field.getAnnotation(Help.class).value());

            bind(binder, tf, field, forSearchFilter);

        }

    }


    protected void bind(MDDBinder binder, TextField tf, FieldInterfaced field, boolean forSearchFilter) {
        Binder.BindingBuilder aux = binder.forField(tf);

        if (field.isAnnotationPresent(ValueClass.class)) {
            aux.withConverter(new Converter() {
                @Override
                public Result convertToModel(Object o, ValueContext valueContext) {
                    return Result.ok(o);
                }

                @Override
                public Object convertToPresentation(Object o, ValueContext valueContext) {
                    return (o != null) ? o : "";
                }
            });
        } else {
            aux.withConverter(new Converter() {
                @Override
                public Result convertToModel(Object o, ValueContext valueContext) {
                    return Result.ok(o);
                }

                @Override
                public Object convertToPresentation(Object o, ValueContext valueContext) {
                    return (o != null) ? o : "";
                }
            });
        }

        if (!forSearchFilter && field.getDeclaringClass() != null) aux.withValidator(new BeanValidator(field.getDeclaringClass(), field.getName()));
        aux.bind(field.getName());
    }

    protected void bind(MDDBinder binder, HasValue tf, FieldInterfaced field, boolean forSearchFilter) {
        Binder.BindingBuilder aux = binder.forField(tf);

        aux.withConverter(new Converter() {
            @Override
            public Result convertToModel(Object o, ValueContext valueContext) {
                if (o != null && !"".equals(o)) {
                    return Result.ok((o.getClass().equals(field.getType()))?o:ReflectionHelper.getId(o));
                } else return Result.ok(null);
            }

            @Override
            public Object convertToPresentation(Object o, ValueContext valueContext) {
                if (o == null) return "";

                com.vaadin.data.provider.DataProvider dp = null;
                if (tf instanceof HasDataProvider) {
                    dp = ((HasDataProvider)tf).getDataProvider();
                } else if (tf instanceof ComboBox) {
                    dp = ((ComboBox)tf).getDataProvider();
                }
                if (dp != null) {
                    Optional optional = dp.fetch(new com.vaadin.data.provider.Query()).filter(x -> (x.getClass().equals(o.getClass()))?x.equals(o):ReflectionHelper.getId(x).equals(o)).findFirst();
                    if (optional.isPresent()) return optional.get();
                    else return "";
                } else return o;
            }
        });

        if (!forSearchFilter && field.getDeclaringClass() != null) aux.withValidator(new BeanValidator(field.getDeclaringClass(), field.getName()));
        aux.bind(field.getName());
    }
}
