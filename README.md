ProtobufInspector is meant to be used by test cases to dig into a
Protobuf Message object and validate the contents.
Years ago I used it in an acceptence framework which sent and
received complicated protobuf messages to a server.

I recommend using Google Truth because it has an extension 
for [Protobufs](https://truth.dev/protobufs) which is widely used. 

The current code could be used as a starting off point for inspecting other
formats of messages like Simple Binary Encoding.

# Examples

## Protobuf Definitions

    message Person {
      optional string name = 1;
      optional int32 id = 2;  // Unique ID number for this person.
      optional string email = 3;
    
      enum PhoneType {
        MOBILE = 0;
        HOME = 1;
        WORK = 2;
      }
    
      message PhoneNumber {
        optional string number = 1;
        optional PhoneType type = 2;
      }
    
      repeated PhoneNumber phones = 4;
    }
    
    message AddressBook {
      optional string name = 1;
      repeated Person people = 2;
    }

## Sample Tests

Helper functions to create sample protobuf data exist but are not shown.

### Test One
Basic example. Given a list of different kinds of Protobufs, 
extract a specific car.

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


Example of using filtered inspectors. Given a list of Car 
protobufs extract each car and check the year.

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


