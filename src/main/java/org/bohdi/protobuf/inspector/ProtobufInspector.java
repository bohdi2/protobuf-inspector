package org.bohdi.protobuf.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
            if (clazz.isInstance(protobuf))
                found.add((C) protobuf);
        }

        return new ProtobufInspector<>(found);
    }



    // assert that current message is of type clazz
    public ProtobufInspector<MessageT> expectMessageOfType(Class<? extends MessageT> clazz) {
        assertThat(messages).first().isExactlyInstanceOf(clazz);
        return this;
    }




    public <FieldT> ProtobufInspector<MessageT> filter(Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> p) {
        return filter(new FieldPredicate<>(fieldExtractor, p));
    }

    public <FieldT> ProtobufInspector<MessageT> filterEquals(Function<MessageT, FieldT> fieldExtractor, FieldT expectedValue) {
        return filter(fieldExtractor, v->v.equals(expectedValue));
    }

    public ProtobufInspector<MessageT> filter(Predicate<MessageT> p) {
        List<MessageT> found = new ArrayList<>();

        for (MessageT protobuf : messages) {
            if (p.test(protobuf))
                found.add(protobuf);
        }

        return new ProtobufInspector<>(found);
    }

    @SafeVarargs
    public final ProtobufInspector<MessageT> filter(Predicate<MessageT>... predicates) {
        ProtobufInspector<MessageT> pi = this;

        for (Predicate<MessageT> predicate : predicates) {
            pi = pi.filter(predicate);
        }
        return pi;
    }


    public void testField(Predicate<MessageT> p) {
        assertThat(p).accepts(messages.get(0));
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
