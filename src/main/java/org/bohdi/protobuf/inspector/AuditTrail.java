package org.bohdi.protobuf.inspector;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Used by ProtobufInspector. Is an audit trail of protobuf messages and expectations.
// This can help in debugging application or ProtobufInspector issues.

class AuditTrail {
    int numberOfErrors;
    int numberOfTests;
    final List<String> trace;

    AuditTrail() {
        numberOfErrors = 0;
        numberOfTests = 0;
        trace = new ArrayList<>();
    }

    AuditTrail success(String s) {
        trace.add("success: " + s);
        numberOfTests++;
        return this;
    }

    AuditTrail fail(String s) {
        trace.add("fail: " + s);
        numberOfTests++;
        numberOfErrors++;
        return this;
    }

    AuditTrail comment(String s) {
        List<String> newTrace = new ArrayList<>(trace);
        trace.add("comment: " + s);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrail that = (AuditTrail) o;
        return numberOfErrors == that.numberOfErrors && numberOfTests == that.numberOfTests && trace.equals(that.trace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfErrors, numberOfTests, trace);
    }

    @Override
    public String toString() {
        return String.format("tests: %d, errors: %d, trace: %n%s",
                numberOfTests,
                numberOfErrors,
                String.join("\n", trace) + "\n");
    }




}
