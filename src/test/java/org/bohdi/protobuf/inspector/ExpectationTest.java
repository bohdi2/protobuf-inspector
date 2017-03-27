package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/26/17.
 */
public class ExpectationTest {

    @Test
    public void test_multiple_filter() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                .expect(new IsSedan("Honda", 1999))
                .nextMessage()
                .expect(new IsSedan("Honda", 2001))
                .nextMessage()
                .expect(new IsSedan("Toyota", 1999))
                .expectEnd();
    }

    @Test
    public void test_Expect_Each_Message() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                .map(new IsSedan("Honda", 1999),
                     new IsSedan("Honda", 2001),
                     new IsSedan("Toyota", 1999))
                .expectEnd();
    }


    Car.Sedan createCar(String make, int year) {

        return Car.Sedan.newBuilder()
                .setMake(make)
                .setYear(year)
                .build();
    }

    class IsSedan implements Expectation {
        private final String make;
        private final int year;

        public IsSedan(String make, int year) {
            this.make = make;
            this.year = year;
        }
        public ProtobufInspector apply(ProtobufInspector protobufInspector) {
            return protobufInspector
                    .expectType(Car.Sedan.class)
                    .expectField("make", make)
                    .expectField("year", year);
        }
    }
}
