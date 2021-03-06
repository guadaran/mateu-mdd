package io.mateu.mdd.tester.model.entities.relations;

import io.mateu.mdd.core.annotations.UseCheckboxes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class ManyToManyASideEntity {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    private String name;


    @ManyToMany(mappedBy = "as")
    @UseCheckboxes
    private Set<ManyToManyBSideEntity> bs = new HashSet<>();




    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj != null && obj instanceof  ManyToManyASideEntity && id == ((ManyToManyASideEntity)obj).id);
    }
}
