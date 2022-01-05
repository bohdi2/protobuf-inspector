ProtobufInspector is used by test cases to dig into a
protobuf Message object and validate the contents.

I used it in an acceptence framework which sent and
received complicated protobuf messages to a server and
I needed to test the messages received.

The protobuf plugin seems to be out of data and this
does not build.