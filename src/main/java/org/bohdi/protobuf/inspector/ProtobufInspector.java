package org.bohdi.protobuf.inspector;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtobufInspector {
    private final Pattern indexPattern; // Finds embedded array indexes: [n]
    private final List<Message> protobufs;
    private Audit audit;

    public ProtobufInspector(List<Message> protobufs) {
        this(new Audit(), protobufs);
    }

    public ProtobufInspector(Audit audit, List<Message> protobufs) {
        //assert messages.size() > 0 : "Empty messages";
        this.protobufs = new ArrayList<Message>(protobufs);
        indexPattern = Pattern.compile("(.*?)\\[(\\d+)\\](.*)");
        this.audit=audit;
    }


    public ProtobufInspector expectMessages(int n) {

        assertEquals(String.format("expectMessages(%d)", n),
                     toClassString(),
                     n,
                     protobufs.size());
        return this;
    }




    public ProtobufInspector nextMessage() {
        assertTrue("nextMessage ", protobufs.size() > 1);
        //if (messages.size() <= 1) {
        //    audit = audit.fail("nextMessage() failed. No more messages.");
        //    audit = audit.success("nextMessage() failed. No more messages.");
        //    throw new ProtobufInspectorException(audit);
        //}
        //else {
        //    audit = audit.success("nextMessage() ok.");
        //}

        return new ProtobufInspector(audit, tail(protobufs));
    }

    // Return new ProtobufInspector containing only messages of type clazz
    public ProtobufInspector filterType(Class clazz) {

        List<Message> found = new ArrayList<Message>();

        for (Message protobuf : protobufs) {
            if (clazz.isInstance(protobuf))
                found.add(protobuf);
        }

        audit = audit.comment(String.format("filterType(%s) removed %d messages", clazz, protobufs.size() - found.size()));
        return new ProtobufInspector(audit, found);
    }

    // Return new ProtobufInspector containing only messages that contain the path and expected values
    public ProtobufInspector filterField(String path, Object expectedValue) {
        List<Message> found = new ArrayList<Message>();

        for (Message protobuf : protobufs) {
            Object o = extractField(path, protobuf);
            if (expectedValue.getClass().equals(o.getClass()) && expectedValue.equals(o))
                found.add(protobuf);
        }

        audit = audit.comment(String.format("filterField(%s, %s) removed %d messages", path, expectedValue, protobufs.size() - found.size()));

        return new ProtobufInspector(audit, found);
    }


    // assert that current message is of type clazz
    public ProtobufInspector expectType(Class clazz) {
        String name = String.format("expectType(%s)", clazz.getSimpleName());

        assertEquals(name,
                     toClassString(),
                     clazz,
                     protobufs.get(0).getClass());
        return this;
    }

    // assert that current message contains path and value
    public ProtobufInspector expectField(String path, Object expectedValue) {
        String name = String.format("check expectField(%s, %s)", path, expectedValue);

        Object o = extractField(path, protobufs.get(0));

        assertEquals(name, "",  expectedValue, o);
        return this;
    }


    private Object extractField(String path, Message proto) {
        return extractField("", Arrays.asList(path.split("\\.")), proto);
    }

    private Object extractField(String comment, List<String> path, Message proto) {
        assert !path.isEmpty();

        //System.err.format("extractField(%s, %d, proto)%n", comment, path.size());

        String fieldName = path.get(0);

        if (path.size() == 1) {
            Matcher m = indexPattern.matcher(fieldName);

            if (m.matches()) {
                // ....fieldname[n]
                //System.err.println("...field[]: " + fieldName);

                Descriptors.FieldDescriptor field = proto.getDescriptorForType().findFieldByName(m.group(1));
                assertNotNull("Check that terminal field \"" + fieldName + "[]\" exists ", field);

                Object value = proto.getRepeatedField(field, Integer.parseInt(m.group(2)));
                if (null != value && value instanceof Descriptors.EnumValueDescriptor) {
                    return ((Descriptors.EnumValueDescriptor) value).getFullName();
                }
                return value;
            }
            else {
                // ...fieldName
                //System.err.println("...field: " + fieldName);

                Descriptors.FieldDescriptor field = proto.getDescriptorForType().findFieldByName(fieldName);
                //System.err.println("{D = " + field + "}");
                assertNotNull("Check that terminal field \"" + fieldName + "\" exists ", field);


                Object value = proto.getField(field);

                if (null != value && value instanceof Descriptors.EnumValueDescriptor) {
                    //System.err.println("ENUM: " + ((Descriptors.EnumValueDescriptor) value).getFullName().getClass());
                    return ((Descriptors.EnumValueDescriptor) value).getFullName();
                }
                //System.err.println("VALUE: " + value);

                return value;
            }
        }
        else {
            Matcher m = indexPattern.matcher(fieldName);

            if (m.matches()) {
                //System.err.println("...field[]...: " + fieldName);
                // ...fieldName[n]...
                Descriptors.FieldDescriptor field = proto.getDescriptorForType().findFieldByName(m.group(1));
                assertNotNull("Check that interior field \"" + fieldName + "[]\" exists", field);

                return extractField(comment + "  ", tail(path), (Message) proto.getRepeatedField(field, Integer.parseInt(m.group(2))));

            }
            else {
                // ...fieldName...
                //System.err.println("...field...: " + fieldName);
                Descriptors.FieldDescriptor field = proto.getDescriptorForType().findFieldByName(fieldName);
                assertNotNull("Check that interior field \"" + fieldName + "\" exists", field);

                return extractField(comment + "  ", tail(path), (Message) proto.getField(field));
            }
        }

    }

    private <T> List<T> tail(List<T> ss) {
        return ss.subList(1, ss.size());
    }

    // Add a comment to audit trail
    public ProtobufInspector comment(String s) {
        audit = audit.comment(s);
        return this;
    }

    public ProtobufInspector dump(String comment) {
        if (protobufs.isEmpty())
            System.err.format("Message[%s]: Empty%n", comment);
        else
            System.err.format("Message[%s]: %s%n", comment, protobufs.get(0));

        return this;
    }

    public ProtobufInspector dumpAll(String comment) {
        int index = 0;
        for (Message protobuf : protobufs) {
            System.err.format("Message[%d](%s): %s%n", index++, comment, protobuf);
        }

        return this;
    }

    // assert there are no more messages in ProtobufInspector
    public ProtobufInspector expectEnd() {
        assertEquals(protobufs.toString(), "bad", protobufs.size(), 1);
        return this;
    }

    public ProtobufInspector xclear() {
        protobufs.clear();
        return this;
    }

    private String toClassString() {
        List<String> classes = new ArrayList<String>(protobufs.size());
        for (Message m : protobufs)
            classes.add(m.getClass().getSimpleName());

        return "[" + Utils.join(classes, ", ") + "]";
    }

    private void assertEquals(String nameTemplate, String comment, int expected, int actual) {
        String name = String.format(nameTemplate, expected);

        if (expected == actual)
            audit = audit.success(String.format("%s // %s", name, comment));
        else {
            audit = audit.fail(String.format("<%s> != <%s> // %s", name, actual, comment));
            throw new ProtobufInspectorException(audit);
            //throw new ProtobufInspectorException(audit);
        }
    }

    private void assertEquals(String name, String comment, Object expected, Object actual) {

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
                //throw new ProtobufInspectorException(audit);
            }
        }
        else {
            audit = audit.fail(String.format(name + "class %s != %s", expected.getClass(), actual.getClass()));
            throw new ProtobufInspectorException(audit);
            //throw new ProtobufInspectorException(audit);
        }
    }


    private void assertNotNull(String comment, Object actual) {
        if (null != actual)
            audit = audit.success(comment);
        else {
            audit = audit.fail(comment);
            throw new ProtobufInspectorException(audit);
            //throw new ProtobufInspectorException(audit);
        }
    }

    private void assertFalse(String comment, boolean actual) {
        if (!actual)
            audit = audit.success(comment);
        else {
            audit = audit.fail(comment);
            throw new ProtobufInspectorException(audit);
            //throw new ProtobufInspectorException(audit);
        }
    }

    private void assertTrue(String comment, boolean actual) {
        if (actual)
            audit = audit.success(comment);
        else {
            audit = audit.fail(comment);
            throw new ProtobufInspectorException(audit);
            //throw new ProtobufInspectorException(audit);
        }
    }

}
