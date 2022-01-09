package org.bohdi.protobuf.inspector;


import java.util.ArrayList;
import java.util.List;

// Used by ProtobufInspector. Is an audit trail of protobuf messages and expectations.
// This can help in debugging application or ProtobufInspector issues.

class AuditTrail {
    final int numberOfErrors;
    final int numberOfTests;
    final List<String> trace;

    AuditTrail() {
        numberOfErrors = 0;
        numberOfTests = 0;
        trace = new ArrayList<>();
    }

    AuditTrail(int numberOfErrors, int numberOfTests, List<String> trace) {
        this.numberOfErrors = numberOfErrors;
        this.numberOfTests = numberOfTests;
        this.trace = new ArrayList<>(trace);
    }

    AuditTrail success(String s) {
        List<String> newTrace = new ArrayList<>(trace);
        newTrace.add("success: " + s);
        return new AuditTrail(numberOfErrors, numberOfTests +1, newTrace);
    }

    AuditTrail fail(String s) {
        List<String> newTrace = new ArrayList<>(trace);
        newTrace.add("fail: " + s);
        return new AuditTrail(numberOfErrors +1, numberOfTests +1, newTrace);
    }

    AuditTrail comment(String s) {
        List<String> newTrace = new ArrayList<>(trace);
        newTrace.add("comment: " + s);
        return new AuditTrail(numberOfErrors, numberOfTests, newTrace);
    }

    @Override
    public String toString() {
        //new Exception("CJH").printStackTrace(System.err);
        return String.format("tests: %d, errors: %d, trace: %n%s",
                numberOfTests,
                numberOfErrors,
                String.join("\n", trace) + "\n");
    }




}
