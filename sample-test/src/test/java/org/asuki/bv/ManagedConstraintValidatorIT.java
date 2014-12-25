package org.asuki.bv;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.inject.Inject;
import javax.validation.Validator;

import org.asuki.bv.ValueHolder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ManagedConstraintValidatorIT {

    @Deployment
    public static Archive<?> createArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(ValueHolder.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private ValueHolder holder;

    @Inject
    private Validator validator;

    @Test
    public void testValidator() {

        holder.setValue("password");
        assertThat(validator.validate(holder).size(), is(1));

        holder.setValue("1234");
        assertThat(validator.validate(holder).size(), is(1));

        holder.setValue("other");
        assertThat(validator.validate(holder).size(), is(0));
    }
}
