package pluradj.janusgraph.graphdb.database.serialize.attribute;

import static org.junit.Assert.*;
import org.junit.Test;

import org.janusgraph.graphdb.database.serialize.DataOutput;
import org.janusgraph.graphdb.database.serialize.StandardSerializer;

import org.janusgraph.diskstorage.ReadBuffer;

import java.util.ArrayList;

public class ArrayListSerializerTest {

    private StandardSerializer getStandardSerializer() {
        // register the custom attribute serializer with the JanusGraph standard serializer
        StandardSerializer serialize = new StandardSerializer();
        serialize.registerClass(2, ArrayList.class, new ArrayListSerializer());
        assertTrue(serialize.validDataType(ArrayList.class));
        return serialize;
    }

    @Test
    public void writeReadObjectNotNull() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        ArrayList<String> list = new ArrayList<String>();
        list.add("one");
        DataOutput out = serialize.getDataOutput(128);
        out.writeObjectNotNull(list);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        ArrayList<String> read = (ArrayList<String>) serialize.readObjectNotNull(b, ArrayList.class);

        // make sure they are equal
        assertEquals(list.size(), read.size());
        assertEquals(list.get(0), read.get(0));
    }

    @Test
    public void writeReadClassAndObject() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        ArrayList<String> list = new ArrayList<String>();
        list.add("one");
        DataOutput out = serialize.getDataOutput(128);
        out.writeClassAndObject(list);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        ArrayList<String> read = (ArrayList<String>) serialize.readClassAndObject(b);

        // make sure they are equal
        assertEquals(list.size(), read.size());
        assertEquals(list.get(0), read.get(0));
    }

    @Test
    public void writeReadObject() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        ArrayList<String> list = new ArrayList<String>();
        list.add("one");
        DataOutput out = serialize.getDataOutput(128);
        out.writeObject(list, ArrayList.class);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        ArrayList<String> read = (ArrayList<String>) serialize.readObject(b, ArrayList.class);

        // make sure they are equal
        assertEquals(list.size(), read.size());
        assertEquals(list.get(0), read.get(0));
    }

}
