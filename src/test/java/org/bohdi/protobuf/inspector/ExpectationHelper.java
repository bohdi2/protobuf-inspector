package org.bohdi.protobuf.inspector;


class ExpectationHelper {

    static final FieldPredicate<Car.Sedan, String> isHonda = new FieldPredicate<>("Make", Car.Sedan::getMake, v->v.equals("Honda"));
    static final FieldPredicate<Car.Sedan, String> isToyota = new FieldPredicate<>("Make", Car.Sedan::getMake, v->v.equals("Toyota"));
    static final FieldPredicate<Car.Sedan, Integer> is1999 = new FieldPredicate<>("Year", Car.Sedan::getYear, v->v == 1999);
    static final FieldPredicate<Car.Sedan, Integer> is2001 = new FieldPredicate<>("Year", Car.Sedan::getYear, v->v == 2001);
    static final IsSedan isHonda1999 = new IsSedan(isHonda, is1999);
    static final IsSedan isHonda2001 = new IsSedan(isHonda, is2001);
    static final IsSedan isToyota1999 = new IsSedan(isToyota, is1999);

    static class IsSedan implements PiPredicate<Car.Sedan> {
        private final FieldPredicate makePredicate;
        private final FieldPredicate yearPredicate;

        public IsSedan(FieldPredicate makePredicate, FieldPredicate yearPredicate) {
            this.makePredicate = makePredicate;
            this.yearPredicate = yearPredicate;
        }
        public boolean test(ProtobufInspector<Car.Sedan> auditor, AuditTrail auditTrail, Car.Sedan protobuf) {
            auditor.comment("IsSedan");
            boolean result = makePredicate.test(auditor, auditTrail, protobuf) && yearPredicate.test(auditor, auditTrail, protobuf);
            if (result) {
                auditTrail.success("IsSedan: Good");
            }
            else {
                auditTrail.fail("IsSedan: No good");
            }
            return result;
        }

        public String toString() {
            return String.format("IsSedan(%s, %s", makePredicate, yearPredicate);
        }
    }

}
