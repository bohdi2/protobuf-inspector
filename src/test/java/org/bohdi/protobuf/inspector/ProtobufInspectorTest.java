package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.bohdi.protobuf.inspector.AddressBookProtos.*;
import static org.bohdi.protobuf.inspector.CompositeFields.*;
import static org.bohdi.protobuf.inspector.ProtobufHelper.*;

public class ProtobufInspectorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_Simple_Lookups_With_Multiple_Messages() {
        List<Message> addressBooks = new ArrayList<>();
        addressBooks.add(createAddressBookWithJoeAndSue());
        addressBooks.add(createAddressBookWithFrank());

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(addressBooks);
        inspector
                .filterByProtobufType(AddressBook.class)
                .expectProtobufOfType(AddressBook.class)
                .expectEquals(AddressBook::getName, "Joe and Sue's Address Book")
                .nextProtobuf()
                .expectProtobufOfType(AddressBook.class)
                .expectEquals(AddressBook::getName, "Frank's Address Book")
                .expectNoMoreProtobufs();
    }


    @Test
    public void test_Predicate_Lookups_With_Multiple_Messages() {
        List<Message> addressBooks = new ArrayList<>();
        addressBooks.add(createAddressBookWithJoeAndSue());
        addressBooks.add(createAddressBookWithFrank());

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(addressBooks);
        inspector
                .filterByProtobufType(AddressBook.class)
                .expectProtobufOfType(AddressBook.class)
                .expect(AddressBook::getName, v->v.equals("Joe and Sue's Address Book"))
                .nextProtobuf()
                .expectProtobufOfType(AddressBook.class)
                .expect(AddressBook::getName, v->v.equals("Frank's Address Book"))
                .expectNoMoreProtobufs();
    }

    @Test
    public void test_Expect_Size_0() {
        List<Message> list = new ArrayList<>();
        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);
        inspector.expectMessageCount(0);
    }

    @Test
    public void test_Expect_Size_Not_0() {
        List<Message> protobufMessages = new ArrayList<>();
        protobufMessages.add(createAddressBookWithJoeAndSue());
        protobufMessages.add(createCar("Honda", 1999));
        protobufMessages.add(createCar("Honda", 2001));
        protobufMessages.add(createCar("Toyota", 1999));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(protobufMessages);
        inspector.expectMessageCount(4);
    }


    @Test
    public void test_flitering_an_empty_list_by_type_has_no_effect() {
        List<Message> list = new ArrayList<>();

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);
        inspector
                .expectMessageCount(0)
                .filterByProtobufType(org.bohdi.protobuf.inspector.Car.Sedan.class)
                .expectMessageCount(0);    }

    @Test
    public void test_filtering_by_type() {
        List<Message> protobufs = new ArrayList<>();
        protobufs.add(createAddressBookWithJoeAndSue());
        protobufs.add(createCar("Honda", 1999));
        protobufs.add(createCar("Honda", 2001));
        protobufs.add(createCar("Toyota", 1999));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(protobufs);
        inspector
                .expectMessageCount(4)
                .filterByProtobufType(org.bohdi.protobuf.inspector.Car.Sedan.class)
                .expectMessageCount(3);

    }

    @Test
    public void test_expectEquals() {
        List<Message> addressBooks = new ArrayList<>();
        addressBooks.add(createAddressBookWithJoeAndSue());

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(addressBooks);
        inspector
                .filterByProtobufType(AddressBook.class)
                .expectEquals(AddressBook::getName, "Joe and Sue's Address Book")
                .expectEquals(m -> m.getPeople(0).getName(), "Joe")
                .expectEquals(m -> m.getPeople(0).getId(), 567)
                .expectEquals(m -> m.getPeople(0).getPhones(0).getNumber(), "123456")
                .expectEquals(m -> m.getPeople(0).getPhones(0).getType(), Person.PhoneType.HOME)

                .expectEquals(m -> m.getPeople(1).getName(), "Sue")
                .expectEquals(m -> m.getPeople(1).getId(), 890)
                .expectEquals(m -> m.getPeople(1).getPhones(0).getNumber(), "123")
                .expectEquals(m -> m.getPeople(1).getPhones(0).getType(), Person.PhoneType.MOBILE)
                .expectEquals(m -> m.getPeople(1).getPhones(1).getNumber(), "456")
                .expectEquals(m -> m.getPeople(1).getPhones(1).getType(), Person.PhoneType.WORK)
                .expectNoMoreProtobufs();
    }





    @Test
    public void test_filter() {
        List<Message> list = new ArrayList<>();
        list.add(createAddressBookWithJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));


        // We can look at the a set of messages in different ways.
        // By using a filter we can create subsets of messages.

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);
        inspector
                // 4 messages in total
                .expectMessageCount(4)
                .filterByProtobufType(Car.Sedan.class)

                // but just 3 cars
                .expectMessageCount(3)
                .filterEquals(Car.Sedan::getMake, "Honda")

                // and just 2 Hondas
                .expectMessageCount(2)
                .expectProtobufOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getYear, 1999)

                .nextProtobuf()
                .expectProtobufOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getYear, 2001)

                .expectNoMoreProtobufs();

    }

    @Test
    public void test_filter_predicates() {
        List<Message> list = new ArrayList<>();
        list.add(createAddressBookWithJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));


        // We can look at the a set of messages in different ways.
        // By using a filter we can create subsets of messages.

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);

        inspector
                // 4 messages in total
                .expectMessageCount(4)
                .filterByProtobufType(Car.Sedan.class)
                .filter(is1999)

                .expectMessageCount(2)
                .expectProtobufOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Honda")
                .nextProtobuf()

                .expectProtobufOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Toyota")
                .expectNoMoreProtobufs()
        ;
    }



    @Test
    public void test_multiple_filter() {
        List<Message> list = new ArrayList<>();
        list.add(createAddressBookWithJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        // Had been <car.sedan> for T
        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);

        inspector
                .expectMessageCount(4)

                .filterByProtobufType(Car.Sedan.class)
                .filterEquals(Car.Sedan::getMake, "Honda")
                .filterEquals(Car.Sedan::getYear, 2001)

                .expectMessageCount(1)
                .expectProtobufOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Honda")
                .expectEquals(Car.Sedan::getYear, 2001)
                .expectNoMoreProtobufs();
    }

    @Test
    public void test_filter_with_multiple_predicates() {
        List<Message> list = new ArrayList<>();
        list.add(createAddressBookWithJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);

        inspector
                .expectMessageCount(4)
                .filterByProtobufType(Car.Sedan.class)
                .filter(isHonda, is2001)

                .expectMessageCount(1)
                .expectProtobufOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Honda")
                .expectEquals(Car.Sedan::getYear, 2001)
                .expectNoMoreProtobufs();
    }


    @Test
    public void test_auditTrail() {
        List<Message> list = new ArrayList<>();
        list.add(createCar("Honda", 2007));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);
        inspector
                .comment("Hello")
                .comment("GoodBye")
                .recordSuccess("good")
                .recordFailure("bad")
                .dumpAuditTrail();
    }


    // ToDo:
//    @Test
//    public void test_expectField_With_Bad_Value() {
//        List<Message> list = new ArrayList<>();
//        list.add(createCar("Honda", 2007));
//
//        thrown.expect(ProtobufInspectorException.class);
//        thrown.expectMessage("fail: xxx2 <Hondo> != <Honda>");
//
//        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);
//        inspector
//                .filterByProtobufType(Car.Sedan.class)
//                .expectEquals(Car.Sedan::getMake, "Hondo");
//    }



}
