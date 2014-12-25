package org.asuki.bv;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.asuki.bv.annotation.ForbiddenValues;
import org.asuki.bv.annotation.NameDifferent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ValueHolder {

    @Getter
    @Setter
    @ForbiddenValues({ "password", "1234" })
    private String value;

    private String name;

    public ValueHolder(@NotNull String name) {
        this.name = name;
    }

    @NameDifferent
    @Size(min = 3)
    public String updateName(@NotNull @Size(min = 1) String firstName,
            @NotNull @Size(min = 1) String lastName) {

        name = firstName + " " + lastName;
        return name;
    }
}
