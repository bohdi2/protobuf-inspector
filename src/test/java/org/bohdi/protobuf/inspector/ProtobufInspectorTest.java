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
                .expectEquals(f->f.getName(), "Joe and Sue's Address Book")
                .nextMessage()
                .expectType(AddressBookProtos.AddressBook.class)
                .expectEquals(f->f.getName(), "Frank's Address Book")
                .expectEnd();
    }


    @Test
    public void test_Predicate_Lookups_With_Multiple_Messages() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createFrank());

        ProtobufInspector<Message> inspector = new ProtobufInspector(list);
        inspector
                .filterType(AddressBookProtos.AddressBook.class)
                .expectType(AddressBookProtos.AddressBook.class)
                .expect(f->f.getName(), v->v.equals("Joe and Sue's Address Book"))
                .nextMessage()
                .expectType(AddressBookProtos.AddressBook.class)
                .expect(f->f.getName(), v->v.equals("Frank's Address Book"))
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
        inspector.expectMessages(4);
    }

    @Test
    public void test_FilterType_Size_0() {
        List<Message> list = new ArrayList<Message>();

        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector
                .expectMessages(0)
                .filterType(org.bohdi.protobuf.inspector.Car.Sedan.class)
                .expectMessages(0);    }

    @Test
    public void test_FilterType() {
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
    public void test_expectEquals() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());

        ProtobufInspector<Message> inspector = new ProtobufInspector(list);
        inspector
                .filterType(AddressBookProtos.AddressBook.class)
                .expectEquals(f->f.getName(), "Joe and Sue's Address Book")
                .expectEquals(m -> m.getPeople(0).getName(), "Joe")
                .expectEquals(m -> m.getPeople(0).getId(), 567)
                .expectEquals(m -> m.getPeople(0).getPhones(0).getNumber(), "123456")
                .expectEquals(m -> m.getPeople(0).getPhones(0).getType(), AddressBookProtos.Person.PhoneType.HOME)

                .expectEquals(m -> m.getPeople(1).getName(), "Sue")
                .expectEquals(m -> m.getPeople(1).getId(), 890)
                .expectEquals(m -> m.getPeople(1).getPhones(0).getNumber(), "123")
                .expectEquals(m -> m.getPeople(1).getPhones(0).getType(), AddressBookProtos.Person.PhoneType.MOBILE)
                .expectEquals(m -> m.getPeople(1).getPhones(1).getNumber(), "456")
                .expectEquals(m -> m.getPeople(1).getPhones(1).getType(), AddressBookProtos.Person.PhoneType.WORK)
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

                // but just 3 cars
                .expectMessages(3)
                .filterEquals(m->m.getMake(), "Honda")

                // and just 2 Hondas
                .expectMessages(2)
                .expectType(Car.Sedan.class)
                .expectEquals(m->m.getYear(), 1999)

                .nextMessage()
                .expectType(Car.Sedan.class)
                .expectEquals(m->m.getYear(), 2001)

                .expectEnd();

    }

    @Test
    public void test_filter_predicates() {
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
                .filter(is1999)

                .expectMessages(2)
                .expectType(Car.Sedan.class)
                .expectEquals(m->m.getMake(), "Honda")
                .nextMessage()

                .expectType(Car.Sedan.class)
                .expectEquals(m->m.getMake(), "Toyota")
                .expectEnd()
        ;
    }



    @Test
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
                .filterEquals(m->m.getMake(), "Honda")
                .filterEquals(m->m.getYear(), 2001)

                .expectMessages(1)
                .expectType(Car.Sedan.class)
                .expectEquals(m->m.getMake(), "Honda")
                .expectEquals(m -> m.getYear(), 2001)
                .expectEnd();
    }

    @Test
    public void test_filter_with_multiple_predicates() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);

        inspector
                .expectMessages(4)
                .filterType(Car.Sedan.class)
                .filter(isHonda, is2001)

                .expectMessages(1)
                .expectType(Car.Sedan.class)
                .expectEquals(m->m.getMake(), "Honda")
                .expectEquals(m -> m.getYear(), 2001)
                .expectEnd();
    }


    //@Test
    public void test_expectField_With_Bad_Value() {
        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 2007));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: xxx2 <Hondo> != <Honda>");

        ProtobufInspector<Car.Sedan> inspector = new ProtobufInspector(list);
        inspector
                .expectEquals(m->m.getMake(), "Hondo");
    }



}
