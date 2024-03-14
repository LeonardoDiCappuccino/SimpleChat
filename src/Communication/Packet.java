package Communication;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Packet implements Serializable {

    private final ProtocolHeader head;

    private final List<Serializable> objectList = new LinkedList<>();

    public Packet(ProtocolHeader head, Serializable... objects) {
        this.head = head;
        objectList.addAll(List.of(objects));
    }

    public Packet append(Serializable object) {
        objectList.add(object);
        return this;
    }

    public ProtocolHeader getHead() {
        return head;
    }

    public boolean isHead(ProtocolHeader compHead) {
        return head.equals(compHead);
    }

    public List<Serializable> getObjectList() {
        return objectList;
    }
}
