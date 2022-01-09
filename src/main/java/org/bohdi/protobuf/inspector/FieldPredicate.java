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
    public boolean test(ProtobufInspector<MessageT> xxx, AuditTrail auditTrail, MessageT protobufMessage) {
        boolean result = fieldPredicate.test(fieldExtractor.apply(protobufMessage));
        if (result) {
            auditTrail.success(comment);
        }
        else {
            //protobufInspector.recordFailure(comment);
            auditTrail.fail(comment);
        }
        return result;
    }


}
