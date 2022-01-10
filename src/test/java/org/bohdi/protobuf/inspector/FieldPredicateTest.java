package org.bohdi.protobuf.inspector;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bohdi.protobuf.inspector.CompositeFieldExample.isHonda1999;
import static org.bohdi.protobuf.inspector.ProtobufHelper.createCar;

public class FieldPredicateTest {

    @Test
    public void test_Field() {
        FieldPredicate<Car.Sedan, String> isHonda = new FieldPredicate<>(Car.Sedan::getMake, v->v.equals("Honda"));
        FieldPredicate<Car.Sedan, Integer> is1999 = new FieldPredicate<>(Car.Sedan::getYear, v->v == 1999);

        assertThat(isHonda).accepts(createCar("Honda", 1999));
        assertThat(isHonda).rejects(createCar("Toyota", 1999));

        assertThat(is1999).accepts(createCar("Honda", 1999));
        assertThat(is1999).rejects(createCar("Honda", 1950));
    }


    @Test
    public void test_compoite_field_isSedan() {
        assertThat(isHonda1999).accepts(createCar("Honda", 1999));
        assertThat(isHonda1999).rejects(createCar("Toyota", 1999));
        assertThat(isHonda1999).rejects(createCar("Honda", 1950));
    }
}
