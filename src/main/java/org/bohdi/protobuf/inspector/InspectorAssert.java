package org.bohdi.protobuf.inspector;


public interface InspectorAssert {
    void assertEquals(String name, String comment, Object expected, Object actual);
    void assertNotNull(String comment, Object actual);
    void assertFalse(String comment, boolean actual);
    void assertTrue(String comment, boolean actual);
}
