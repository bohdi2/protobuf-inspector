package org.bohdi.protobuf.inspector;


public class ProtobufInspectorException extends RuntimeException {
    public ProtobufInspectorException(Audit audit) {
        super(audit.toString());
    }
}
