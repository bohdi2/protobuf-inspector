package org.bohdi.protobuf.inspector;

import java.util.function.Function;
import java.util.function.Predicate;

public class FieldPredicate<MessageT, FieldT> implements Predicate<MessageT> {
    private final Function<MessageT, FieldT> fieldExtractor;
    private final Predicate<FieldT> fieldPredicate;

    public FieldPredicate(Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> fieldPredicate) {
        this.fieldExtractor = fieldExtractor;
        this.fieldPredicate = fieldPredicate;
    }

    public boolean test(MessageT protobufMessage) {
        return fieldPredicate.test(fieldExtractor.apply(protobufMessage));
    }


}
