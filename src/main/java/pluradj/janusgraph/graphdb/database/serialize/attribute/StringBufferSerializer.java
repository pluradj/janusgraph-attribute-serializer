package pluradj.janusgraph.graphdb.database.serialize.attribute;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;
import org.janusgraph.graphdb.database.serialize.attribute.StringSerializer;

public class StringBufferSerializer implements AttributeSerializer<StringBuffer> {
    // leverage JanusGraph's StringSerializer to integrate with its attribute serialization flow
    private StringSerializer serializer;

    public StringBufferSerializer() {
        System.out.println("*** StringBufferSerializer constructor");
        serializer = new StringSerializer();
    }

    @Override
    public StringBuffer read(ScanBuffer buffer) {
        System.out.println("*** StringBufferSerializer read");
        String str = serializer.read(buffer);
        return new StringBuffer(str);
    }

    @Override
    public void write(WriteBuffer buffer, StringBuffer attribute) {
        System.out.println("*** StringBufferSerializer write");
        serializer.write(buffer, attribute.toString());
    }
}
