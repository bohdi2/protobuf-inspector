package org.bohdi.protobuf.inspector;


import java.util.function.Predicate;

class CompositeFieldExample {

    static final FieldPredicate<Car.Sedan, String> isHonda = new FieldPredicate<>( Car.Sedan::getMake, v->v.equals("Honda"));
    static final FieldPredicate<Car.Sedan, String> isToyota = new FieldPredicate<>( Car.Sedan::getMake, v->v.equals("Toyota"));
    static final FieldPredicate<Car.Sedan, Integer> is1999 = new FieldPredicate<>(Car.Sedan::getYear, v->v == 1999);
    static final FieldPredicate<Car.Sedan, Integer> is2001 = new FieldPredicate<>(Car.Sedan::getYear, v->v == 2001);
    static final IsSedan isHonda1999 = new IsSedan(isHonda, is1999);

    // A composite predicate. Checks multiple fields.

    static class IsSedan implements Predicate<Car.Sedan> {
        private final FieldPredicate<Car.Sedan, String> makePredicate;
        private final FieldPredicate<Car.Sedan, Integer> yearPredicate;

        public IsSedan(FieldPredicate<Car.Sedan, String> makePredicate, FieldPredicate<Car.Sedan, Integer> yearPredicate) {
            this.makePredicate = makePredicate;
            this.yearPredicate = yearPredicate;
        }
        public boolean test(Car.Sedan protobuf) {
            return makePredicate.test(protobuf) && yearPredicate.test(protobuf);
        }

        public String toString() {
            return String.format("IsSedan(%s, %s", makePredicate, yearPredicate);
        }
    }

}
