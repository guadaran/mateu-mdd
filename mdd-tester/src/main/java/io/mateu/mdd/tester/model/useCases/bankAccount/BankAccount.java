package io.mateu.mdd.tester.model.useCases.bankAccount;

import io.mateu.mdd.core.annotations.Output;
import io.mateu.mdd.core.model.authentication.Audit;
import io.mateu.mdd.core.model.authentication.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter@Setter
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    @Output
    private Audit audit;

    private String name;

    @ManyToOne
    @NotNull
    private User owner;

    @Output
    private double balance;

}
