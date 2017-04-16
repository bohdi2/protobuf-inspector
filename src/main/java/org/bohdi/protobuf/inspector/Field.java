package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;


public class Field<T> implements Expectation {
    private final Function function;
    private final Object value;

    public Field(Function<T> function, Object value) {
        this.function = function;
        this.value = value;
    }

    public ProtobufInspector check(ProtobufInspector protobufInspector) {
        return protobufInspector.expect(value, function);
        //return protobufInspector.xxexpectField(path, value);
    }

    public boolean filter(ProtobufInspector protobufInspector, Message message) {
        return false;
        //return protobufInspector.filter(value, function);
        //return protobufInspector.filterField(message, path, value);
    }

    public String toString() {
        return String.format("Field(%s, %s", "comment", value);
    }

}
