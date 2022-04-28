package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetHistoricoContaReq implements CatalystSerializable {
    public int nib;

    public GetHistoricoContaReq(int nib) {
        this.nib = nib;
    }

    public GetHistoricoContaReq() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(nib);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        nib = bufferInput.readInt();
    }
}
