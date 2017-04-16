package org.bohdi.protobuf.inspector;


import com.google.protobuf.Message;

class ExpectationHelper {
    static final Field<Car.Sedan> isHonda = new Field<Car.Sedan>(m->m.getMake(), "Honda");
    static final Field<Car.Sedan> isToyota = new Field<Car.Sedan>(m->m.getMake(), "Toyota");
    static final Field<Car.Sedan> is1999 = new Field<Car.Sedan>(m->m.getYear(), 1999);
    static final Field<Car.Sedan> is2001 = new Field<Car.Sedan>(m->m.getYear(), 2001);
    static final Expectation isHonda1999 = new IsSedan(isHonda, is1999);
    static final Expectation isHonda2001 = new IsSedan(isHonda, is2001);
    static final Expectation isToyota1999 = new IsSedan(isToyota, is1999);

    static class IsSedan implements Expectation<Car.Sedan> {
        private final Field make;
        private final Field year;

        public IsSedan(Field make, Field year) {
            this.make = make;
            this.year = year;
        }
        public ProtobufInspector<Car.Sedan> check(ProtobufInspector<Car.Sedan> protobufInspector, InspectorAssert inspectorAssert, Car.Sedan message) {
            return protobufInspector
                    .filterType(Car.Sedan.class)
                    .expectType(Car.Sedan.class)
                    .expect(isHonda)
                    .expect(isHonda)
                    .expect(is2001);
        }

        public boolean filter(ProtobufInspector<Car.Sedan> protobufInspector, Car.Sedan message) {
            return true;
            //return protobufInspector.filter(make, m->m.getMake()) && protobufInspector.filter(message, "year", year);
        }

        public String toString() {
            return String.format("IsSedan(%s, %s", make, year);
        }
    }

}
