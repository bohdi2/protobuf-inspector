package org.bohdi.protobuf.inspector;


import java.util.ArrayList;
import java.util.List;


public class ProtobufInspector<T> implements InspectorAssert {
    private final List<T> protobufs;
    private Audit audit;

    public ProtobufInspector(List<T> protobufs) {
        this(new Audit(), protobufs);
    }

    public ProtobufInspector(Audit audit, List<T> protobufs) {
        //assert messages.size() > 0 : "Empty messages";
        this.protobufs = new ArrayList<T>(protobufs);
        this.audit=audit;
    }


    public ProtobufInspector<T> expectMessages(int n) {

        assertEquals(String.format("expectMessages(%d)", n),
                     toClassString(),
                     n,
                     protobufs.size());
        return this;
    }




    public ProtobufInspector<T> nextMessage() {
        assertTrue("nextMessage ", protobufs.size() > 1);
        return new ProtobufInspector<T>(audit, tail(protobufs));
    }

    // assert there are no more messages in ProtobufInspector
    public ProtobufInspector<T> expectEnd() {
        assertEquals(protobufs.toString(), "bad", protobufs.size(), 1);
        return this;
    }

    // Return new ProtobufInspector containing only messages of type clazz
    public <C extends T> ProtobufInspector<C> filterType(Class<C> clazz) {

        List<C> found = new ArrayList<C>();

        for (T protobuf : protobufs) {
            if (clazz.isInstance(protobuf))
                found.add((C) protobuf);
        }

        audit = audit.comment(String.format("filterType(%s) removed %d messages", clazz, protobufs.size() - found.size()));
        return new ProtobufInspector<C>(audit, found);
    }



    // assert that current message is of type clazz
    public ProtobufInspector<T> expectType(Class clazz) {
        String name = String.format("expectType(%s)", clazz.getSimpleName());

        assertEquals(name,
                     toClassString(),
                     clazz,
                     protobufs.get(0).getClass());
        return this;
    }


    // Return new ProtobufInspector containing only messages that satisfy the expectations
    //public ProtobufInspector<T> filter(Expectation expectation) {
    //    List<T> found = new ArrayList<T>();

    //    for (T protobuf : protobufs) {
    //        if (expectation.filter(this, protobuf))
    //            found.add(protobuf);
    //    }

    //    audit = audit.comment(String.format("filter(%s) removed %d messages", expectation, protobufs.size() - found.size()));

    //    return new ProtobufInspector<T>(audit, found);
    //}


    public ProtobufInspector<T> filter(Object expected, Function<T> function) {
        assertEquals("xxx", "Comment", expected, function.op(protobufs.get(0)));

        List<T> found = new ArrayList<T>();

        for (T protobuf : protobufs) {
            if (function.op(protobuf).equals(expected))
                found.add(protobuf);
        }

        audit = audit.comment(String.format("filter(%s) removed %d messages", function, protobufs.size() - found.size()));

        return new ProtobufInspector<T>(audit, found);
    }

    public ProtobufInspector<T> expect(Expectation... expectations) {
        T protobuf = protobufs.get(0);
        ProtobufInspector<T> pi = this;

        for (Expectation expectation : expectations) {
            pi = expectation.check(pi, null, protobuf); // This will throw if test fails
        }
        return pi;
    }

    public ProtobufInspector<T> expect(Integer expected, Function<T> function) {
        assertEquals("xxx1", "Comment", expected, function.op(protobufs.get(0)));
        return this;
    }

    public ProtobufInspector<T> expect(String expected, Function<T> function) {
        assertEquals("xxx2", "Comment", expected, function.op(protobufs.get(0)));
        return this;
    }

    public ProtobufInspector<T> expect(Object expected, Function<T> function) {
        //System.err.println("pi.expect()");
        assertEquals("xxx3", "Comment", expected, function.op(protobufs.get(0)));
        //System.err.println("pi.expect() done");
        return this;
    }

//    public ProtobufInspector<T> map(Expectation... expectations) {
//        return map(Arrays.asList(expectations));
//    }
//
//    public ProtobufInspector<T> map(List<Expectation> list) {
//        if (list.isEmpty())
//            return this;
//
//        int len = list.size();
//        ProtobufInspector<T> pi = list.get(0).check(this);
//
//        for (int i = 1; i<len; i++) {
//            pi = pi.nextMessage();
//            pi = list.get(i).check(pi);
//        }
//        return pi;
//    }


    public ProtobufInspector<T> expectString(String s, Function<T> function) {
        assertEquals("xxx4", "Comment", s, function.op(protobufs.get(0)).toString());
        return this;
    }




    private <E> List<E> tail(List<E> ss) {
        return ss.subList(1, ss.size());
    }

    // Add a comment to audit trail
    public ProtobufInspector<T> comment(String s) {
        audit = audit.comment(s);
        return this;
    }

    public ProtobufInspector<T> dump(String comment) {
        if (protobufs.isEmpty())
            System.err.format("Message[%s]: Empty%n", comment);
        else
            System.err.format("Message[%s]: %s%n", comment, protobufs.get(0));

        return this;
    }

    public ProtobufInspector<T> dumpAll(String comment) {
        int index = 0;
        for (T protobuf : protobufs) {
            System.err.format("Message[%d](%s): %s%n", index++, comment, protobuf);
        }

        return this;
    }




    private String toClassString() {
        List<String> classes = new ArrayList<String>(protobufs.size());
        for (T m : protobufs)
            classes.add(m.getClass().getSimpleName());

        return "[" + Utils.join(classes, ", ") + "]";
    }


    public void assertEquals(String name, String comment, Object expected, Object actual) {

        //System.err.format("assertEquals(name=%s, comment=%s, expected=%s, actual=%s%n", name, comment, expected, actual);

        // Are the classes the same?
        if (expected.getClass().equals(actual.getClass())) {
            // Yes, are the values the same?
            if (expected.equals(actual)) {
                //audit = audit.success(comment + "(" + expected + ") ok ");
                audit = audit.success(String.format("%s // %s", name, comment));
            }
            else {
                //audit = audit.fail(badComment + " expected2: " + expected + ", actual: " + actual);
                audit = audit.fail(String.format(name + " <%s> != <%s>", expected, actual));
                throw new ProtobufInspectorException(audit);
            }
        }
        else {
            audit = audit.fail(String.format(name + "class %s != %s", expected.getClass(), actual.getClass()));
            throw new ProtobufInspectorException(audit);
        }
    }


    public void assertNotNull(String comment, Object actual) {
        if (null != actual)
            audit = audit.success(comment);
        else {
            audit = audit.fail(comment);
            throw new ProtobufInspectorException(audit);
        }
    }

    public void assertFalse(String comment, boolean actual) {
        if (!actual)
            audit = audit.success(comment);
        else {
            audit = audit.fail(comment);
            throw new ProtobufInspectorException(audit);
        }
    }

    public void assertTrue(String comment, boolean actual) {
        if (actual)
            audit = audit.success(comment);
        else {
            audit = audit.fail(comment);
            throw new ProtobufInspectorException(audit);
        }
    }

}
