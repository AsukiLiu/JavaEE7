package org.asuki.bv;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

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
                .addPackages(true, ValueHolder.class.getPackage())
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

    @Test
    public void testMethodValidation() throws Exception {
        ValueHolder bean = new ValueHolder("name");

        Method method = ValueHolder.class.getMethod("updateName", String.class,
                String.class);
        Constructor<ValueHolder> constructor = ValueHolder.class
                .getConstructor(String.class);

        ExecutableValidator executableValidator = validator.forExecutables();

        Set<ConstraintViolation<ValueHolder>> parameterViolations = executableValidator
                .validateParameters(bean, method,
                        new Object[] { "same", "same" });
        assertThat(parameterViolations.size(), is(1));

        Set<ConstraintViolation<ValueHolder>> returnValueViolations = executableValidator
                .validateReturnValue(bean, method, "xx");
        assertThat(returnValueViolations.size(), is(1));

        Set<ConstraintViolation<ValueHolder>> constructorViolations = executableValidator
                .validateConstructorParameters(constructor,
                        new Object[] { null });
        assertThat(constructorViolations.size(), is(1));
    }

}
