package org.bohdi.protobuf.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class ProtobufInspector<MessageT> {
    private final List<MessageT> protobufs;  // "protobufs" and "messages" are used interchangeably

    public ProtobufInspector(List<MessageT> protobufs) {
        this.protobufs = new ArrayList<>(protobufs);
    }

    // Test that there are 'n' protobuf messages.
    public ProtobufInspector<MessageT> expectMessageCount(int expectedSize) {

        assertEquals(String.format("expectMessages(%d)", expectedSize),
                toClassString(),
                expectedSize,
                protobufs.size());

        return this;
    }


    // Test that there is another protobuffer in the list of messages and if there
    // is then create a new ProtobufInspector for it. This is how tests for different
    // messages are tied together.

    public ProtobufInspector<MessageT> nextProtobuf() {
        assertTrue("nextMessage ", protobufs.size() > 1);
        return new ProtobufInspector<>(tail(protobufs));
    }

    // assert there are no more messages in ProtobufInspector
    public ProtobufInspector<MessageT> expectNoMoreProtobufs() {
        assertEquals(protobufs.toString(), "bad", protobufs.size(), 1);
        return this;
    }



    // Return new ProtobufInspector containing only messages of type clazz
    public <C extends MessageT> ProtobufInspector<C> filterByProtobufType(Class<C> clazz) {

        List<C> found = new ArrayList<>();

        for (MessageT protobuf : protobufs) {
            if (clazz.isInstance(protobuf))
                found.add((C) protobuf);
        }

        return new ProtobufInspector<>(found);
    }



    // assert that current message is of type clazz
    public ProtobufInspector<MessageT> expectProtobufOfType(Class clazz) {
        String name = String.format("expectType(%s)", clazz.getSimpleName());

        assertEquals(name,
                     toClassString(),
                     clazz,
                     protobufs.get(0).getClass());
        return this;
    }




    public <FieldT> ProtobufInspector<MessageT> filter(Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> p) {
        return filter(new FieldPredicate<>("Foo", fieldExtractor, p));
    }

    public <FieldT> ProtobufInspector<MessageT> filterEquals(Function<MessageT, FieldT> fieldExtractor, FieldT expectedValue) {
        return filter(fieldExtractor, v->v.equals(expectedValue));
    }

    public ProtobufInspector<MessageT> filter(Predicate<MessageT> p) {
        List<MessageT> found = new ArrayList<>();

        for (MessageT protobuf : protobufs) {
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


    public boolean testField(Predicate<MessageT> p) {
        return p.test(protobufs.get(0));
    }


    public <FieldT> ProtobufInspector<MessageT> expectEquals(Function<MessageT, FieldT> fieldExtractor, FieldT expected) {
        return expect(fieldExtractor, v->v.equals(expected));
    }

    public <FieldT> ProtobufInspector<MessageT> expect(Function<MessageT, FieldT> fieldExtractor, Predicate<FieldT> p) {
        return expect(new FieldPredicate<>("Foo2", fieldExtractor, p));
    }

    private ProtobufInspector<MessageT> expect(Predicate<MessageT> p) {
        assertTrue("expect", p.test(protobufs.get(0)));
        return this;

    }

    private ProtobufInspector<MessageT> expect(Predicate<MessageT>... predicates) {
        ProtobufInspector<MessageT> pi = this;

        for (Predicate<MessageT> predicate : predicates) {
            pi = pi.expect(predicate);
        }
        return pi;
    }



    private <E> List<E> tail(List<E> ss) {
        return ss.subList(1, ss.size());
    }



    public ProtobufInspector<MessageT> dumpCurrentProtobuf(String comment) {
        if (protobufs.isEmpty())
            System.err.format("Message[%s]: Empty%n", comment);
        else
            System.err.format("Message[%s]: %s%n", comment, protobufs.get(0));

        return this;
    }

    public ProtobufInspector<MessageT> dumpAll(String comment) {
        int index = 0;
        for (MessageT protobuf : protobufs) {
            System.err.format("Message[%d](%s): %s%n", index++, comment, protobuf);
        }

        return this;
    }



    // Get the class name of each Protobuf in the inspector.
    private String toClassString() {
        List<String> classes = new ArrayList<>(protobufs.size());
        for (MessageT m : protobufs)
            classes.add(m.getClass().getSimpleName());

        return "[" + String.join(", ", classes) + "]";
    }


    public void assertEquals(String name, String comment, Object expected, Object actual) {

        //System.err.format("assertEquals(name=%s, comment=%s, expected=%s, actual=%s%n", name, comment, expected, actual);

        // Are the classes the same?
        if (expected.getClass().equals(actual.getClass())) {
            // Yes, are the values the same?
            if (expected.equals(actual)) {
                //audit = audit.success(comment + "(" + expected + ") ok ");
                //auditTrail.success(String.format("%s // %s", name, comment));
            }
            else {
                //audit = audit.fail(badComment + " expected2: " + expected + ", actual: " + actual);
                //auditTrail.fail(String.format(name + " <%s> != <%s>", expected, actual));
                throw new ProtobufInspectorException(null);
            }
        }
        else {
            //auditTrail.fail(String.format(name + "class %s != %s", expected.getClass(), actual.getClass()));
            throw new ProtobufInspectorException(null);
        }
    }


    public void assertNotNull(String comment, Object actual) {
        if (null != actual)
         ;   //auditTrail.success(comment);
        else {
            //auditTrail.fail(comment);
            throw new ProtobufInspectorException(null);
        }
    }

    public void assertFalse(String comment, boolean actual) {
        if (!actual)
            ;//auditTrail.success(comment);
        else {
            //auditTrail.fail(comment);
            throw new ProtobufInspectorException(null);
        }
    }

    public void assertTrue(String comment, boolean actual) {
        if (actual)
           ; //auditTrail.success(comment);
        else {
            //auditTrail.fail(comment);
            throw new ProtobufInspectorException(comment);
        }
    }

}
