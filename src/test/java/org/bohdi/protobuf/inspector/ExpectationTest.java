package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.bohdi.protobuf.inspector.ExpectationHelper.*;
import static org.bohdi.protobuf.inspector.ProtobufHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ExpectationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_x1() {

        List<Car.Sedan> list = new ArrayList<Car.Sedan>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> inspector = new ProtobufInspector<>(list);

        //assertTrue("Honda", inspector.test(m->m.getMake().equals("Honda")));
        //assertTrue("1999", inspector.test(m->m.getYear() == 1999));

        inspector
                .expectMessages(3)
                //.expect(isHonda, is1999)
                .nextMessage()
                //.expect(isHonda, is2001)
                .nextMessage()
                //.expect(isToyota, is1999)
                .expectEnd();
    }

    @Test
    public void test_x() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        //assertTrue(isHonda.check(inspector))

        inspector
                .expectMessages(3)
                //.expect(isHonda, is1999)
                .nextMessage()
                //.expect(isHonda, is2001)
                .nextMessage()
                //.expect(isToyota, is1999)
                .expectEnd();
    }

    @Test
    public void test_Multiple_Inspectors_Per_Message() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                //.expect(isHonda, is1999)
                .nextMessage()
                //.expect(isHonda, is2001)
                .nextMessage()
                //.expect(isToyota, is1999)
                .expectEnd();
    }


    //@Test
    public void test_bad_expectation() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: xxx3 <Toyota> != <Honda>");

        new ProtobufInspector<Message>(list)
                .expectMessages(1)
                //.expect(isToyota)
        ;

    }

    //@Test
    public void test_Multiple_Inspectors_Per_Message_Error() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: xxx3 <2001> != <1999>");

        new ProtobufInspector<Message>(list)
                .expectMessages(1)
                //.expect(isHonda, is2001) // Will fail
        ;

    }

    //@Test
    public void test_Multiple_Inspectors_Per_Message_Error2() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: xxx3 <2001> != <1999>");

        new ProtobufInspector<Message>(list)
                //.expect(isHonda2001)
        ;

    }

    //@Test
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

    //@Test
    public void test_Expect_Fields() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> inspector = new ProtobufInspector(list);

//        inspector
//                .expectMessages(3)
//                .map(new Field<Car.Sedan>(m -> m.getMake(), "Honda"),
//                     new Field<Car.Sedan>(m -> m.getMake(), "Honda"),
//                     new Field<Car.Sedan>(m -> m.getMake(), "Toyota"))
//                .expectEnd();
    }

    //@Test
    public void test_Filter_Fields() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                //.filter(isHonda)
                .expectMessages(2)
        ;

    }

    //@Test
    public void test_Filter_Fields2() {

        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        System.err.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

        ProtobufInspector<Message> inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(3)
                //.filter(isHonda1999)
                .expectMessages(1);
        ;
    }

}
