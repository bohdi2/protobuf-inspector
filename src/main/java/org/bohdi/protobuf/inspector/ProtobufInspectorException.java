package org.bohdi.protobuf.inspector;

// ProtorbufInspectorExceptions contain an audit trail.

public class ProtobufInspectorException extends RuntimeException {
//    public ProtobufInspectorException(AuditTrail audit) {
//        super(audit.toString());
//    }

    public ProtobufInspectorException(String audit) {
        super(audit);
    }
}
