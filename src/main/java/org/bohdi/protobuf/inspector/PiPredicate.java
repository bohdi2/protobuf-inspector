package org.bohdi.protobuf.inspector;


public interface PiPredicate<MessageT> {
    boolean test(ProtobufInspector<MessageT> pi, MessageT message);
}
