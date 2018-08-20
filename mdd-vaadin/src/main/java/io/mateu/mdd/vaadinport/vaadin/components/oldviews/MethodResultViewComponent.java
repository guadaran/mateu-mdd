package io.mateu.mdd.vaadinport.vaadin.components.oldviews;

import com.vaadin.data.HasValue;
import com.vaadin.data.Validator;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import io.mateu.mdd.core.MDD;
import io.mateu.mdd.core.annotations.Action;
import io.mateu.mdd.core.annotations.Ignored;
import io.mateu.mdd.core.interfaces.AbstractStylist;
import io.mateu.mdd.core.reflection.FieldInterfaced;
import io.mateu.mdd.core.reflection.ReflectionHelper;
import io.mateu.mdd.core.util.Helper;
import io.mateu.mdd.core.data.MDDBinder;
import io.mateu.mdd.core.data.Pair;
import io.mateu.mdd.vaadinport.vaadin.MyUI;
import io.mateu.mdd.vaadinport.vaadin.components.fieldBuilders.JPAOneToManyFieldBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Query;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MethodResultViewComponent extends AbstractViewComponent {

    private final Method method;
    private final Object result;
    private Map<HasValue, List<Validator>> validators = new HashMap<>();

    private MDDBinder binder;

    private AbstractStylist stylist;

    public List<Object> getMergeables() {
        return binder.getMergeables();
    }

    public MethodResultViewComponent(Method method, Object result) {

        this.result = result;
        this.method = method;

        try {
            build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String t = Helper.capitalize(method.getName());
        if (method.isAnnotationPresent(Action.class)) t = method.getAnnotation(Action.class).value();
        return t;
    }

    @Override
    public MethodResultViewComponent build() throws IllegalAccessException, InstantiationException {

        super.build();

        addStyleName("editorviewcomponent");

        if (result == null) {

            addComponent(new Label("Empty result", ContentMode.HTML));

        } else {

            Class c = result.getClass();

            if (int.class.equals(c)
                    || Integer.class.equals(c)
                    || long.class.equals(c)
                    || Long.class.equals(c)
                    || double.class.equals(c)
                    || Double.class.equals(c)
                    || String.class.equals(c)
                    || boolean.class.equals(c)
                    || Boolean.class.equals(c)
                    || float.class.equals(c)
                    || Float.class.equals(c)
                    ) {

                addComponent(new Label("" + result, ContentMode.HTML));

            } else if (Collection.class.isAssignableFrom(c)) {

                Collection col = (Collection) result;

                if (col.size() == 0) {

                    addComponent(new Label("Empty list", ContentMode.HTML));

                } else {

                    Grid g = new Grid();

                    ListViewComponent.buildColumns(g, ListViewComponent.getColumnFields(col.iterator().next().getClass()), false, false);

                    //g.setSelectionMode(Grid.SelectionMode.MULTI);

                    // añadimos columna para que no haga feo
                    if (g.getColumns().size() == 1) ((Grid.Column)g.getColumns().get(0)).setExpandRatio(1);
                    else g.addColumn((d) -> null).setWidthUndefined().setCaption("");

                    addComponent(g);

                    g.setWidth("100%");
                    g.setHeightByRows(col.size());

                    g.setDataProvider(new ListDataProvider((Collection) result));

                }


            } else if (Query.class.equals(c)) {

                addComponent(new JasperReportComponent((Query) result));

            } else {

                binder = new MDDBinder(result.getClass());
                //binder = new Binder(modelType, true);

                Pair<Component, AbstractStylist> r = FormLayoutBuilder.get().build(binder, result.getClass(), result, validators, getAllFields());

                stylist = r.getValue();
                addComponent(r.getKey());

            }

        }


        return this;
    }

    private List<FieldInterfaced> getAllFields() {
        List<FieldInterfaced> allFields = ReflectionHelper.getAllFields(method);

        allFields = allFields.stream().filter((f) ->
                !(f.isAnnotationPresent(Ignored.class) || (f.isAnnotationPresent(Id.class) && f.isAnnotationPresent(GeneratedValue.class)))
        ).collect(Collectors.toList());

        return allFields;
    }

    public AbstractStylist getStylist() {
        return stylist;
    }

    public void setStylist(AbstractStylist stylist) {
        this.stylist = stylist;
    }


}
