package Utils;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;

/* Criamos esta classe porque este triplo de atributos representa uma referencia para 1 objeto distribuido.
O recetor consegue fazer invocações a esse objeto */
/* Referencia para 1 objeto distribuido */
public class ObjRef implements CatalystSerializable {
    public Address address; /* endereço a qual vai estar conectado*/
    public int id;          /* id do objeto nos objs do ServidorLivraria */
    public String cls;      /* tipo do objeto */

    public ObjRef(Address address, int id, String cls) {
        this.address = address;
        this.id = id;
        this.cls = cls;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(id);
        serializer.writeObject(address, bufferOutput);
        bufferOutput.writeString(cls);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        id = bufferInput.readInt();
        serializer.readObject(bufferInput);
        cls = bufferInput.readString();
    }
}
