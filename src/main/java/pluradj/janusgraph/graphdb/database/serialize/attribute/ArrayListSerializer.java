package pluradj.janusgraph.graphdb.database.serialize.attribute;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;
import org.janusgraph.graphdb.database.serialize.attribute.ByteArraySerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

public class ArrayListSerializer implements AttributeSerializer<ArrayList> {
    // use Java object serialization to convert ArrayList into byte[]
    // then leverage JanusGraph's ByteArraySerializer to integrate with its attribute serialization flow
    private ByteArraySerializer serializer;

    public ArrayListSerializer() {
        System.out.println("*** ArrayListSerializer constructor");
        serializer = new ByteArraySerializer();
    }

    @Override
    public ArrayList read(ScanBuffer buffer) {
        System.out.println("*** ArrayListSerializer read");
        ArrayList attribute = null;
        byte[] data = serializer.read(buffer);
        // http://docs.oracle.com/javase/8/docs/technotes/guides/language/try-with-resources.html
        try (
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
        ) {
            attribute = (ArrayList) ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return attribute;
    }

    @Override
    public void write(WriteBuffer buffer, ArrayList attribute) {
        System.out.println("*** ArrayListSerializer write");
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
        ) {
            oos.writeObject(attribute);
            byte[] data = baos.toByteArray();
            serializer.write(buffer, data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
