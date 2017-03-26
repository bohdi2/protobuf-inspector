package org.bohdi.protobuf.inspector;

/**
 * Created by chris on 3/26/17.
 */
public class ProtobufInspectorException extends RuntimeException {
    public ProtobufInspectorException(Audit audit) {
        super(audit.toString());
    }
}
