package org.bohdi.protobuf.inspector;


class ProtobufHelper {

    static AddressBookProtos.AddressBook createJoeAndSue() {
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

    static AddressBookProtos.AddressBook createFrank() {
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

    static Car.Sedan createCar(String make, int year) {

        return Car.Sedan.newBuilder()
                .setMake(make)
                .setYear(year)
                .build();
    }

}
