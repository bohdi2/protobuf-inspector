package org.bohdi.protobuf.inspector;


import java.util.ArrayList;
import java.util.List;

// Used by ProtobufInspector. Is an audit trail of protobuf messages and expectations.
// This can be helpful debugging application or ProtobufInspector issues.

class Audit {
    final int errors;
    final int tests;
    final List<String> trace;

    Audit() {
        errors = 0;
        tests = 0;
        trace = new ArrayList<String>();
    }

    Audit(int errors, int asserts, List<String> trace) {
        this.errors = errors;
        this.tests = asserts;
        this.trace = new ArrayList<String>(trace);
    }

    Audit success(String s) {
        List newTrace = new ArrayList<String>(trace);
        newTrace.add("success: " + s);
        return new Audit(errors, tests +1, newTrace);
    }

    Audit fail(String s) {
        List newTrace = new ArrayList<String>(trace);
        newTrace.add("fail: " + s);
        return new Audit(errors+1, tests +1, newTrace);
    }

    Audit comment(String s) {
        List newTrace = new ArrayList<String>(trace);
        newTrace.add("comment: " + s);
        return new Audit(errors, tests, newTrace);
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
