package org.bohdi.protobuf.inspector;


import java.util.ArrayList;
import java.util.List;

// Used by ProtobufInspector. Is an audit trail of protobuf messages and expectations.
// This can help in debugging application or ProtobufInspector issues.

class AuditTrail {
    final int errors;
    final int tests;
    final List<String> trace;

    AuditTrail() {
        errors = 0;
        tests = 0;
        trace = new ArrayList<String>();
    }

    AuditTrail(int errors, int asserts, List<String> trace) {
        this.errors = errors;
        this.tests = asserts;
        this.trace = new ArrayList<String>(trace);
    }

    AuditTrail success(String s) {
        List newTrace = new ArrayList<String>(trace);
        newTrace.add("success: " + s);
        return new AuditTrail(errors, tests +1, newTrace);
    }

    AuditTrail fail(String s) {
        List newTrace = new ArrayList<String>(trace);
        newTrace.add("fail: " + s);
        return new AuditTrail(errors+1, tests +1, newTrace);
    }

    AuditTrail comment(String s) {
        List newTrace = new ArrayList<String>(trace);
        newTrace.add("comment: " + s);
        return new AuditTrail(errors, tests, newTrace);
    }

    @Override
    public String toString() {
        //new Exception("CJH").printStackTrace(System.err);
        return String.format("tests: %d, errors: %d, trace: %n%s",
                             tests,
                             errors,
                             String.join("\n", trace) + "\n");
    }




}
