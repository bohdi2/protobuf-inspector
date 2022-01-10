package org.bohdi.protobuf.inspector;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bohdi.protobuf.inspector.CompositeFields.isHonda1999;
import static org.bohdi.protobuf.inspector.ProtobufHelper.createCar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldPredicateTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_Field() {

        List<Car.Sedan> list = new ArrayList<>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> pi = new ProtobufInspector<>(list);

        FieldPredicate<Car.Sedan, String> fs = new FieldPredicate<>(Car.Sedan::getMake, v->v.equals("Honda"));
        FieldPredicate<Car.Sedan, Integer> fi = new FieldPredicate<>(Car.Sedan::getYear, v->v == 1999);

        pi.testField(fs);
        pi.testField(fi);
    }

    @Test
    public void test_compoite_field_isSedan() {

        List<Car.Sedan> list = new ArrayList<>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> pi = new ProtobufInspector<>(list);

        pi.testField(isHonda1999);
        pi.testField(isHonda1999);
    }

    @Test
    public void test_compoite_field_xxx() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("");

        List<Car.Sedan> list = new ArrayList<>();
        list.add(createCar("Honda", 1950));

        ProtobufInspector<Car.Sedan> pi = new ProtobufInspector<>(list);

        pi.testField(isHonda1999);
    }


    @SafeVarargs
    final <T> List<T> list(T... ts) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result, ts);
        return result;
    }
}
