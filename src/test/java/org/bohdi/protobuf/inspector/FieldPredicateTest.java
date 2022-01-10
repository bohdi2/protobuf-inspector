package org.bohdi.protobuf.inspector;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bohdi.protobuf.inspector.CompositeFieldExample.isHonda1999;
import static org.bohdi.protobuf.inspector.ProtobufHelper.createCar;

public class FieldPredicateTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_Field() {
        FieldPredicate<Car.Sedan, String> fs = new FieldPredicate<>(Car.Sedan::getMake, v->v.equals("Honda"));
        FieldPredicate<Car.Sedan, Integer> fi = new FieldPredicate<>(Car.Sedan::getYear, v->v == 1999);

        assertThat(fs).accepts(createCar("Honda", 1999));
        assertThat(fi).accepts(createCar("Honda", 1999));
    }


    @Test
    public void test_compoite_field_isSedan() {
        assertThat(isHonda1999).accepts(createCar("Honda", 1999));
        assertThat(isHonda1999).accepts(createCar("Honda", 1999));
    }

    @Test
    public void test_compoite_field_xxx() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("");

        assertThat(isHonda1999).accepts(createCar("Honda", 1950));
    }


    @SafeVarargs
    final <T> List<T> list(T... ts) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result, ts);
        return result;
    }
}
