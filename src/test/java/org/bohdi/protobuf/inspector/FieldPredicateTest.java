package org.bohdi.protobuf.inspector;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bohdi.protobuf.inspector.ExpectationHelper.isHonda1999;
import static org.bohdi.protobuf.inspector.ProtobufHelper.createCar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldPredicateTest {
    @Test
    public void test_Field() {

        List<Car.Sedan> list = new ArrayList<>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> pi = new ProtobufInspector<>(list);

        FieldPredicate<Car.Sedan, String> fs = new FieldPredicate<>("Make == Honda", Car.Sedan::getMake, v->v.equals("Honda"));
        FieldPredicate<Car.Sedan, Integer> fi = new FieldPredicate<>("Year == 1999", Car.Sedan::getYear, v->v == 1999);

        assertTrue("Honda", pi.test(fs));
        assertTrue("1999", pi.test(fi));

        assertEquals(list("success: Make == Honda", "success: Year == 1999"), pi.getAuditTrail().trace);
    }

    @Test
    public void test_isSedan() {

        List<Car.Sedan> list = new ArrayList<>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> pi = new ProtobufInspector<>(list);

        assertTrue("Honda", pi.xtest(isHonda1999));
        assertTrue("1999", pi.xtest(isHonda1999));
    }

    @SafeVarargs
    final <T> List<T> list(T... ts) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result, ts);
        return result;
    }
}
