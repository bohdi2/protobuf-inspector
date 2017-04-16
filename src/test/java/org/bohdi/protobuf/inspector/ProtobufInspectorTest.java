package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.bohdi.protobuf.inspector.ExpectationHelper.*;
import static org.bohdi.protobuf.inspector.ProtobufHelper.*;

public class ProtobufInspectorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @Test
    public void test_Simple_Lookups_With_Multiple_Messages() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createFrank());

        ProtobufInspector<Message> inspector = new ProtobufInspector(list);
        inspector
                .filterType(AddressBookProtos.AddressBook.class)
                .expectType(AddressBookProtos.AddressBook.class)
                .expect("Joe and Sue's Address Book", m -> m.getName())
                .nextMessage()
                .expectType(AddressBookProtos.AddressBook.class)
                .expect("Frank's Address Book", m -> m.getName())
                .expectEnd();
    }

    @Test
    public void test_Expect_Size_0() {
        List<Message> list = new ArrayList<Message>();
        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector.expectMessages(0);
    }

    @Test
    public void test_Expect_Size_Not_0() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector
                .expectMessages(4)
                .filterType(org.bohdi.protobuf.inspector.Car.Sedan.class)
                .expectMessages(3);

    }





    @Test
    public void test_expect() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());

        ProtobufInspector<Message> inspector = new ProtobufInspector(list);
        inspector
                .filterType(AddressBookProtos.AddressBook.class)
                .expectString("Joe and Sue's Address Book", m -> m.getName())

                .expectString("Joe", m -> m.getPeople(0).getName())
                .expect(567, m -> m.getPeople(0).getId())
                .expect("123456", m -> m.getPeople(0).getPhones(0).getNumber())
                .expect(AddressBookProtos.Person.PhoneType.HOME, m -> m.getPeople(0).getPhones(0).getType())

                .expectString("Sue", m -> m.getPeople(1).getName())
                .expect(890, m -> m.getPeople(1).getId())
                .expect("123", m -> m.getPeople(1).getPhones(0).getNumber())
                .expectString("MOBILE", m -> m.getPeople(1).getPhones(0).getType())
                .expect("456", m -> m.getPeople(1).getPhones(1).getNumber())
                .expectString("WORK", m -> m.getPeople(1).getPhones(1).getType())
                .expectEnd();
    }





    @Test
    public void test_filter() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));


        // We can look at the a set of messages in different ways.
        // By using a filter we can create subsets of messages.

        ProtobufInspector<Message> inspector = new ProtobufInspector(list);

        inspector
                // 4 messages in total
                .expectMessages(4)
                .filterType(Car.Sedan.class)
                //.xxexpectField("make", "Honda")

                // but just 3 cars
                .expectMessages(3)
                .filter("Honda", m->m.getMake())

                // and just 2 Hondas
                .expectMessages(2)
                .expectType(Car.Sedan.class)
                //.xxexpectField("year", 1999)

                .nextMessage()
                .expectType(Car.Sedan.class)
                //.xxexpectField("year", 2001)

                .expectEnd();

        // Look at same set of messages again, just filter on a different securityId

        inspector
                // 4 messages in total
                .expectMessages(4)
                .filterType(Car.Sedan.class)
                //.filter(is1999)

                // Only one request message for securityId 124
                //.expectMessages(2)
                //.expectType(Car.Sedan.class)
                //.expect(isHonda)
                //.nextMessage()
                //.expectType(Car.Sedan.class)
                //.expect(isToyota)
                //.expectEnd()
        ;
    }



    //@Test
    public void test_multiple_filter() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Car.Sedan> inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(4)

                .filterType(Car.Sedan.class)
                //.filterField("make", "Honda")
                //.filterField("year", 2001)

                .expectMessages(1)
                .expectType(Car.Sedan.class)
                .expect("Honda", m->m.getMake())
                .expect(2001, m -> m.getYear())
                .expectEnd();
    }



    //@Test
    public void test_expectField_With_Bad_Value() {
        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 2007));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: check expectField(make, Hondo) <Hondo> != <Honda>");

        ProtobufInspector<Car.Sedan> inspector = new ProtobufInspector(list);
        inspector
                .expect("Hondo", m->m.getMake());
    }



}
