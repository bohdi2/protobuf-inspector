package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.bohdi.protobuf.inspector.AddressBookProtos.*;
import static org.bohdi.protobuf.inspector.CompositeFieldExample.*;
import static org.bohdi.protobuf.inspector.ProtobufHelper.*;

public class ProtobufInspectorTest {

    @Test
    public void test_Simple_Lookups_With_Multiple_Messages() {
        List<Message> addressBooks = new ArrayList<>();
        addressBooks.add(createAddressBookWithJoeAndSue());
        addressBooks.add(createAddressBookWithFrank());

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(addressBooks);
        inspector
                .filterByMessageType(AddressBook.class)
                .expectMessageOfType(AddressBook.class)
                .expectEquals(AddressBook::getName, "Joe and Sue's Address Book")
                .nextMessage()
                .expectMessageOfType(AddressBook.class)
                .expectEquals(AddressBook::getName, "Frank's Address Book")
                .expectNoMoreMessages();
    }


    @Test
    public void test_Predicate_Lookups_With_Multiple_Messages() {
        List<Message> addressBooks = new ArrayList<>();
        addressBooks.add(createAddressBookWithJoeAndSue());
        addressBooks.add(createAddressBookWithFrank());

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(addressBooks);
        inspector
                .filterByMessageType(AddressBook.class)
                .expectMessageOfType(AddressBook.class)
                .expect(AddressBook::getName, v->v.equals("Joe and Sue's Address Book"))
                .nextMessage()
                .expectMessageOfType(AddressBook.class)
                .expect(AddressBook::getName, v->v.equals("Frank's Address Book"))
                .expectNoMoreMessages();
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
                .filterByMessageType(org.bohdi.protobuf.inspector.Car.Sedan.class)
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
                .filterByMessageType(org.bohdi.protobuf.inspector.Car.Sedan.class)
                .expectMessageCount(3);

    }

    @Test
    public void test_expectEquals() {
        List<Message> addressBooks = new ArrayList<>();
        addressBooks.add(createAddressBookWithJoeAndSue());

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(addressBooks);
        inspector
                .filterByMessageType(AddressBook.class)
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
                .expectNoMoreMessages();
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
                .filterByMessageType(Car.Sedan.class)

                // but just 3 cars
                .expectMessageCount(3)
                .filterByEquals(Car.Sedan::getMake, "Honda")

                // and just 2 Hondas
                .expectMessageCount(2)
                .expectMessageOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getYear, 1999)

                .nextMessage()
                .expectMessageOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getYear, 2001)

                .expectNoMoreMessages();

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
                .filterByMessageType(Car.Sedan.class)
                .filter(is1999)

                .expectMessageCount(2)
                .expectMessageOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Honda")
                .nextMessage()

                .expectMessageOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Toyota")
                .expectNoMoreMessages()
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

                .filterByMessageType(Car.Sedan.class)
                .expectMessageCount(3)

                .filterByEquals(Car.Sedan::getMake, "Honda")
                .filterByEquals(Car.Sedan::getYear, 2001)
                .expectMessageCount(1)

                .expectMessageOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Honda")
                .expectEquals(Car.Sedan::getYear, 2001)

                .expectNoMoreMessages();
    }

    @Test
    public void test_with_multiple_predicates() {
        List<Message> list = new ArrayList<>();
        list.add(createAddressBookWithJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);

        inspector
                .expectMessageCount(4)

                .filterByMessageType(Car.Sedan.class)
                .expectMessageCount(3)

                .multiFilter(isHonda, is2001)
                .expectMessageCount(1)
                .expectMessageOfType(Car.Sedan.class)
                .expectEquals(Car.Sedan::getMake, "Honda")
                .expectEquals(Car.Sedan::getYear, 2001)

                .expectNoMoreMessages();
    }

    @Test
    public void test_each_car() {
        List<Message> list = new ArrayList<>();
        list.add(createCar("Honda", 1999));
        list.add(createCar("Toyota", 2001));

        ProtobufInspector<Car.Sedan> carInspector = new ProtobufInspector<>(list)
                .filterByMessageType(Car.Sedan.class)
                .expectMessageCount(2);


        carInspector
                .filterByMessageType(Car.Sedan.class)
                .filter(isHonda)
                .expectEquals(Car.Sedan::getYear, 1999)
                .expectNoMoreMessages();

        carInspector
                .filterByMessageType(Car.Sedan.class)
                .filter(isToyota)
                .expectEquals(Car.Sedan::getYear, 2001)
                .expectNoMoreMessages();
    }


    // ToDo:
    @Test
    public void test_expectField_With_Bad_Value() {
        List<Message> list = new ArrayList<>();
        list.add(createCar("Honda", 2007));

        ProtobufInspector<Message> inspector = new ProtobufInspector<>(list);
//        inspector
//                .filterByMessageType(Car.Sedan.class)
//                .expectEquals(Car.Sedan::getMake, "Hondooo");

        assertThatExceptionOfType(AssertionError.class).isThrownBy(
                () ->         inspector
                        .filterByMessageType(Car.Sedan.class)
                        .expectEquals(Car.Sedan::getMake, "Hondooo")
        );
    }



}
