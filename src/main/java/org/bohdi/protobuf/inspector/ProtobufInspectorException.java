package org.bohdi.protobuf.inspector;


public class ProtobufInspectorException extends RuntimeException {
    public ProtobufInspectorException(AuditTrail audit) {
        super(audit.toString());
    }
}
