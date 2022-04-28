package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetContaBancariaReq implements CatalystSerializable {
    public int idBanco;
    public int nib;

    public GetContaBancariaReq(int idBanco, int nib) {
        this.idBanco = idBanco;
        this.nib = nib;
    }

    public GetContaBancariaReq() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(idBanco);
        bufferOutput.writeInt(nib);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        idBanco = bufferInput.readInt();
        nib = bufferInput.readInt();
    }
}
