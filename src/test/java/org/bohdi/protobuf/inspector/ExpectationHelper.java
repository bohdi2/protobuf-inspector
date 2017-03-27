package org.bohdi.protobuf.inspector;


import com.google.protobuf.Message;

class ExpectationHelper {
    static final Field isHonda = new Field("make", "Honda");
    static final Field isToyota = new Field("make", "Toyota");
    static final Field is1999 = new Field("year", 1999);
    static final Field is2001 = new Field("year", 2001);
    static final Expectation isHonda1999 = new IsSedan("Honda", 1999);
    static final Expectation isHonda2001 = new IsSedan("Honda", 2001);
    static final Expectation isTotota1999 = new IsSedan("Toyota", 1999);

    static class IsSedan implements Expectation {
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

        public boolean filter(ProtobufInspector protobufInspector, Message message) {
            return protobufInspector.filterField(message, "make", make) && protobufInspector.filterField(message, "year", year);
        }

        public String toString() {
            return String.format("IsSedan(%s, %d", make, year);
        }
    }

}
