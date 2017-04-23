package org.bohdi.protobuf.inspector;

import java.util.function.Function;

public class StringField<T> {//implements Expectation<T> {

    private final Function<T, String> function;
    private final String value;

    public StringField(Function<T, String> function, String value) {
        this.function = function;
        this.value = value;
    }

    public ProtobufInspector<T> check(ProtobufInspector<T> protobufInspector, T message) {
        return protobufInspector;
        //System.err.println("Field.check() " + value + ", {" + TextFormat.shortDebugString((Message) message) + "}");
        //return protobufInspector.expect(value, function);
        //return protobufInspector.xxexpectField(path, value);
    }

    public boolean filter(ProtobufInspector<T> protobufInspector, T message) {
        return false;
        //System.err.println("Field.filter(protobuf, message)");
        //return protobufInspector.filter(value, function, message);
        //return protobufInspector.filterField(message, path, value);
    }

    //public String toString() {
    //    return String.format("Field(%s, %s", "comment", value);
    //}


}
