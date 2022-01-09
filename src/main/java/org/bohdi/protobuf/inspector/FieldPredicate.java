package org.bohdi.protobuf.inspector;

import java.util.function.Function;
import java.util.function.Predicate;

public class FieldPredicate<MessageT, FieldT> implements PiPredicate<MessageT> {
    private final String comment;
    private final Function<MessageT, FieldT> fieldExtractor;
    private final Predicate<FieldT> fieldPredicate;

    public FieldPredicate(String comment, Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> fieldPredicate) {
        this.comment = comment;
        this.fieldExtractor = fieldExtractor;
        this.fieldPredicate = fieldPredicate;
    }

    // protobufInspector is only ysed to record success/failure
    public boolean test(ProtobufInspector<MessageT> protobufInspector, AuditTrail auditTrail, MessageT protobufMessage) {
        boolean result = fieldPredicate.test(fieldExtractor.apply(protobufMessage));
        if (result) {
            //System.out.println("Embedded: " + protobufInspector.auditTrail);
            //System.out.println("Free:     " + auditTrail);
            //assert protobufInspector.auditTrail.equals(auditTrail);
            //protobufInspector.recordSuccess(comment);
            //auditTrail.success(comment);
            //System.out.println("Embedded: " + protobufInspector.auditTrail);
            //System.out.println("Free:     " + auditTrail);
        }
        else {
            //protobufInspector.recordFailure(comment);
        }
        return result;
    }


}
