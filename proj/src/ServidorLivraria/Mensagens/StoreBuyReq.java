package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreBuyReq implements CatalystSerializable {
    public int idCart;
    public int nib;

    public StoreBuyReq(int idCart, int nib) {
        this.idCart=idCart;
        this.nib=nib;
    }

    public StoreBuyReq(){}

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(idCart);
        bufferOutput.writeInt(nib);

    }
    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        this.idCart = bufferInput.readInt();
        this.nib = bufferInput.readInt();
    }
}
