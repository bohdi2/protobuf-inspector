package org.bohdi.protobuf.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProtobufInspector<MessageT> {
    private final List<MessageT> messages;

    public ProtobufInspector(List<MessageT> messages) {
        this.messages = new ArrayList<>(messages);
    }

    // Test that there are 'n' messages.
    public ProtobufInspector<MessageT> expectMessageCount(int expectedSize) {
        assertThat(messages).hasSize(expectedSize);
        return this;
    }


    // Test that there is another message in the list of messages and if there
    // is then create a new ProtobufInspector for it.

    public ProtobufInspector<MessageT> nextMessage() {
        assertThat(messages).hasSizeGreaterThan(1);
        return new ProtobufInspector<>(tail(messages));
    }

    // assert there are no more messages in ProtobufInspector
    public ProtobufInspector<MessageT> expectNoMoreMessages() {
        assertThat(messages).hasSize(1);
        return this;
    }



    // Return new ProtobufInspector containing only messages of type clazz
    public <C extends MessageT> ProtobufInspector<C> filterByMessageType(Class<C> clazz) {

        List<C> found = new ArrayList<>();

        for (MessageT protobuf : messages) {
            if (clazz.isInstance(protobuf)) {
                found.add(clazz.cast(protobuf));
            }
        }

        return new ProtobufInspector<>(found);
    }



    // assert that current message is of type clazz
    public ProtobufInspector<MessageT> expectMessageOfType(Class<? extends MessageT> clazz) {
        assertThat(messages).first().isExactlyInstanceOf(clazz);
        return this;
    }



    public <FieldT> ProtobufInspector<MessageT> filterByEquals(Function<MessageT, FieldT> fieldExtractor, FieldT expectedValue) {
        return filter(fieldExtractor, v->v.equals(expectedValue));
    }

    public <FieldT> ProtobufInspector<MessageT> filter(Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> p) {
        return filter(new FieldPredicate<>(fieldExtractor, p));
    }

    // Filter our messages that do not match the predicate
    public ProtobufInspector<MessageT> filter(Predicate<MessageT> p) {
        List<MessageT> ps = messages.stream().filter(p).collect(Collectors.toList());

        return new ProtobufInspector<>(ps);
    }

    @SafeVarargs
    public final ProtobufInspector<MessageT> multiFilter(Predicate<MessageT>... predicates) {
        ProtobufInspector<MessageT> filteredInspector = this;

        for (Predicate<MessageT> predicate : predicates) {
            filteredInspector = filteredInspector.filter(predicate);
        }
        return filteredInspector;
    }

    public <FieldT> ProtobufInspector<MessageT> expectEquals(Function<MessageT, FieldT> fieldExtractor, FieldT expected) {
        return expect(fieldExtractor, v->v.equals(expected));
    }

    public <FieldT> ProtobufInspector<MessageT> expect(Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> p) {
        assertThat(new FieldPredicate<>(fieldExtractor, p)).accepts(messages.get(0));
        return this;
    }

    private <E> List<E> tail(List<E> ss) {
        return ss.subList(1, ss.size());
    }


    // Display the protobuf being inspected. 

    public ProtobufInspector<MessageT> dumpCurrentProtobuf(String comment) {
        if (messages.isEmpty())
            System.err.format("Message[%s]: Empty%n", comment);
        else
            System.err.format("Message[%s]: %s%n", comment, messages.get(0));

        return this;
    }

    public ProtobufInspector<MessageT> dumpAll(String comment) {
        int index = 0;
        for (MessageT protobuf : messages) {
            System.err.format("Message[%d](%s): %s%n", index++, comment, protobuf);
        }

        return this;
    }

}
