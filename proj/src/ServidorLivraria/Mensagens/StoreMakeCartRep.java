package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreMakeCartRep implements CatalystSerializable {
    public int cartid;

    public StoreMakeCartRep() {
    }
    public StoreMakeCartRep(int cartid) {
        this.cartid = cartid;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(cartid);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        cartid = bufferInput.readInt();
    }
}
