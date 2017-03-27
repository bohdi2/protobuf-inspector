package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;


public class Field implements Expectation {
    private final String path;
    private final Object value;

    public Field(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public ProtobufInspector apply(ProtobufInspector protobufInspector) {
        return protobufInspector.expectField(path, value);
    }

    public boolean filter(ProtobufInspector protobufInspector, Message message) {
        return protobufInspector.filterField(message, path, value);
    }

    public String toString() {
        return String.format("Field(%s, %s", path, value);
    }

}
