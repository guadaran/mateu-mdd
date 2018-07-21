package io.mateu.mdd.tester.model.entities.basic;

import com.vaadin.icons.VaadinIcons;
import io.mateu.mdd.core.annotations.Action;
import io.mateu.mdd.core.annotations.Parameter;
import io.mateu.mdd.core.annotations.SearchFilter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter@Setter
public class ActionsDemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @SearchFilter
    private String stringField = "zzzz";

    private int intField;


    @Action(name = "Action on all", icon = VaadinIcons.AIRPLANE)
    public static void action1() {
        System.out.println("action 1");
    }

    @Action(name = "Action on all w/params")
    public static void action3(@Parameter(name = "Name") String name, @Parameter(name = "Ageº") int age) {
        System.out.println("action 3 " + name + "/" + age);
    }

    @Action(name = "Action on all w/params+result")
    public static String action5(@Parameter(name = "Name") String name, @Parameter(name = "Ageº") int age) {
        String msg = "action 5 " + name + "/" + age;
        System.out.println(msg);
        return msg;
    }

    @Action(name = "Action on all w/injected params")
    public static void action4(EntityManager em, Set<ActionsDemoEntity> selection) {
        System.out.println("action 4");
    }

    @Action(name = "Action on item", icon = VaadinIcons.ALARM, confirmationMessage = "Are you sure you want to do it?")
    public void action2() {
        System.out.println("action 2");
        setStringField("" + getStringField() + " - " + new Date());
    }


    @Action(name = "Action on item w/params")
    public void action6(String name) {
        System.out.println("action 6 " + name);
        setStringField("" + getStringField() + "/6 - " + new Date());
    }


    @Action(name = "Action on item w/params + result")
    public String action7(String name) {
        String msg = "action 7 " + name;
        System.out.println(msg);
        return msg;
    }

    @Action(name = "Open custom component")
    public void action8() {

    }
}