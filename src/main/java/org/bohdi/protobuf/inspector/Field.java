package org.bohdi.protobuf.inspector;

import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;


public class Field<T> implements Expectation<T> {
    private final Function function;
    private final Object value;

    public Field(Function<T> function, Object value) {
        this.function = function;
        this.value = value;
    }

    public ProtobufInspector check(ProtobufInspector<T> protobufInspector, InspectorAssert inspectorAssert, T message) {
        //System.err.println("Field.check() " + value + ", {" + TextFormat.shortDebugString((Message) message) + "}");
        return protobufInspector.expect(value, function);
        //return protobufInspector.xxexpectField(path, value);
    }

    public boolean filter(ProtobufInspector<T> protobufInspector, T message) {
        return false;
        //return protobufInspector.filter(value, function);
        //return protobufInspector.filterField(message, path, value);
    }

    public String toString() {
        return String.format("Field(%s, %s", "comment", value);
    }

}
