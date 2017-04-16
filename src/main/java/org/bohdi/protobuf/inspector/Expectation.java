package org.bohdi.protobuf.inspector;


import com.google.protobuf.Message;

// An accessor and expected result
// An expectation expects to find a specific value in a message.

public interface Expectation<T> {
    // Assert that the expectation is true
    ProtobufInspector check(ProtobufInspector<T> protobufInspector, InspectorAssert inspectorAssert, T message);

    // Test if the expectation is true
    boolean filter(ProtobufInspector<T> protobufInspector, T message);
}
