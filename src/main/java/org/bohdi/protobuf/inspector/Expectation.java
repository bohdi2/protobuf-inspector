package org.bohdi.protobuf.inspector;


import com.google.protobuf.Message;

public interface Expectation {
    ProtobufInspector apply(ProtobufInspector protobufInspector);
    boolean filter(ProtobufInspector protobufInspector, Message message);
}
