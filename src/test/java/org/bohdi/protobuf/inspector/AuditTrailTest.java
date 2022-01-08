package org.bohdi.protobuf.inspector;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuditTrailTest {

    @Test
    public void testSimple() {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail = auditTrail.success("One");
        auditTrail = auditTrail.success("Two");

        assertEquals("tests", 2, auditTrail.numberOfTests);
        assertEquals("errors", 0, auditTrail.numberOfErrors);

        assertTrue(auditTrail.toString().contains("One"));
        assertTrue(auditTrail.toString().contains("Two"));
    }


}
