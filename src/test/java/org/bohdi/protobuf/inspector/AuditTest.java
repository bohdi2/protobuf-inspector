package org.bohdi.protobuf.inspector;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuditTest {

    @Test
    public void testSimple() {
        Audit audit = new Audit();
        audit = audit.success("One");
        audit = audit.success("Two");

        assertEquals("tests", 2, audit.tests);
        assertEquals("errors", 0, audit.errors);

        assertTrue(audit.toString().contains("One"));
        assertTrue(audit.toString().contains("Two"));
    }


}
