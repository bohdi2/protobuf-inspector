package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;


public class ProtobufInspectorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_Simple_Lookups_With_One_Message() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());

        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector
                .expectType(AddressBookProtos.AddressBook.class)
                .expectField("name", "Joe and Sue's Address Book")
                .expectField("people[0].name", "Joe")
                .expectField("people[0].phones[0].number", "123456")
                .expectField("people[0].phones[0].type", "Person.PhoneType.HOME")
                .expectField("people[1].name", "Sue")
                .expectField("people[1].phones[0].number", "123")
                .expectField("people[1].phones[0].type", "Person.PhoneType.MOBILE")
                .expectField("people[1].phones[1].number", "456")
                .expectField("people[1].phones[1].type", "Person.PhoneType.WORK")
                .expectEnd();
    }

    //@Test
    public void test_Simple_Lookups_With_Multiple_Messages() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createFrank());

        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector
                .expectType(AddressBookProtos.AddressBook.class)
                .expectField("name", "Joe and Sue's Address Book")
                .nextMessage()
                .expectType(AddressBookProtos.AddressBook.class)
                .expectField("name", "Frank's Address Book")
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
    public void test_filter() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));


        // We can look at the a set of messages in different ways.
        // By using a filter we can create subsets of messages.

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                // 3 messages in total
                .expectMessages(4)
                .filterType(Car.Sedan.class)
                .expectField("make", "Honda")

                // 2 request messages for securityId 123
                .expectMessages(3)
                .filterField("make", "Honda")
                .expectMessages(2)
                .expectType(Car.Sedan.class)
                .expectField("year", 1999)

                .nextMessage()
                .expectType(Car.Sedan.class)
                .expectField("year", 2001)

                .expectEnd();

        // Look at same set of messages again, just filter on a different securityId

        inspector
                // 4 messages in total
                .expectMessages(4)
                .filterType(Car.Sedan.class)
                .filterField("year", 1999)

                // Only one request message for securityId 124
                .expectMessages(2)
                .expectType(Car.Sedan.class)
                .expectField("make", "Honda")
                .nextMessage()
                .expectType(Car.Sedan.class)
                .expectField("make", "Toyota")
                 .expectEnd();
    }



    @Test
    public void test_multiple_filter() {
        List<Message> list = new ArrayList<Message>();
        list.add(createJoeAndSue());
        list.add(createCar("Honda", 1999));
        list.add(createCar("Honda", 2001));
        list.add(createCar("Toyota", 1999));

        ProtobufInspector inspector = new ProtobufInspector(list);

        inspector
                .expectMessages(4)

                .filterType(Car.Sedan.class)
                .filterField("make", "Honda")
                .filterField("year", 2001)

                .expectMessages(1)
                .expectType(Car.Sedan.class)
                .expectField("make", "Honda")
                .expectField("year", 2001)
                .expectEnd();
    }

    @Test
    public void test_expectField_With_Bad_Key() {
        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 2007));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: Check that terminal field \"makex\" exists");

        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector
                .expectField("makex", "Honda");
    }

    @Test
    public void test_expectField_With_Bad_Value() {
        List<Message> list = new ArrayList<Message>();
        list.add(createCar("Honda", 2007));

        thrown.expect(ProtobufInspectorException.class);
        thrown.expectMessage("fail: check expectField(make, Hondo) <Hondo> != <Honda>");

        ProtobufInspector inspector = new ProtobufInspector(list);
        inspector
                .expectField("make", "Hondo");
    }

    // We just need any protobuf that has nested and repeating fields.

    AddressBookProtos.AddressBook createJoeAndSue() {
        AddressBookProtos.AddressBook.Builder builder = AddressBookProtos.AddressBook.newBuilder();
        builder
                .setName("Joe and Sue's Address Book")

                .addPeople(0, AddressBookProtos.Person.newBuilder()
                        .setName("Joe")
                        .addPhones(0, AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("123456")
                                .setType(AddressBookProtos.Person.PhoneType.HOME)))

                .addPeople(1, AddressBookProtos.Person.newBuilder()
                        .setName("Sue")
                        .addPhones(0, AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("123")
                                .setType(AddressBookProtos.Person.PhoneType.MOBILE))
                        .addPhones(1, AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("456")
                                .setType(AddressBookProtos.Person.PhoneType.WORK)))
        ;


        return builder.build();
    }

    AddressBookProtos.AddressBook createFrank() {
        AddressBookProtos.AddressBook.Builder builder = AddressBookProtos.AddressBook.newBuilder();
        builder
                .setName("Frank's Address Book")

                .addPeople(0, AddressBookProtos.Person.newBuilder()
                        .setName("Joe")
                        .addPhones(0, AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("123456")
                                .setType(AddressBookProtos.Person.PhoneType.HOME)))
        ;


        return builder.build();
    }

    Car.Sedan createCar(String make, int year) {

        return Car.Sedan.newBuilder()
                .setMake(make)
                .setYear(year)
                .build();
    }


}
