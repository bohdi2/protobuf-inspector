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

    public boolean test(ProtobufInspector<MessageT> protobufInspector, MessageT protobufMessage) {
        boolean result = fieldPredicate.test(fieldExtractor.apply(protobufMessage));
        if (result) {
            protobufInspector.recoredSuccess(comment);
        }
        else {
            protobufInspector.recordFailure(comment);
        }
        return result;
    }


}
