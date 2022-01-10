package org.bohdi.protobuf.inspector;


public interface PiPredicate<MessageT> {

    boolean test(MessageT message);
}
