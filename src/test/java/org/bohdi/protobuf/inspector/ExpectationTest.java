package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.bohdi.protobuf.inspector.ExpectationHelper.*;
import static org.bohdi.protobuf.inspector.ProtobufHelper.*;


public class ExpectationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_Multiple_Inspectors_Per_Message() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                .expect(isHonda, is1999)
                .nextMessage()
                .expect(isHonda, is2001)
                .nextMessage()
                .expect(isToyota, is1999)
                .expectEnd();
    }

    //@Test
    public void test_Multiple_Inspectors_Per_Message_Error() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: check expectField(year, 1999) <1999> != <2001>");

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                .expect(isHonda, is1999)
                .nextMessage()
                .expect(isHonda, isHonda1999) // Will fail
                .nextMessage()
                .expect(isToyota, is1999)
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
                //.map(isHonda1999, isHonda2001, isToyota1999)
                //.expectEnd();
        ;
    }

    @Test
    public void test_Expect_Fields() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                .map(new Field<Car.Sedan>(m -> m.getMake(), "Honda"),
                     new Field<Car.Sedan>(m -> m.getMake(), "Honda"),
                     new Field<Car.Sedan>(m -> m.getMake(), "Toyota"))
                .expectEnd();
    }

    @Test
    public void test_Filter_Fields() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                //.filter(isHonda)
                //.expectMessages(2);
        ;
    }

    @Test
    public void test_Filter_Fields2() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                //.filter(isHonda1999)
                //.expectMessages(1);
        ;
    }

}
