package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetHistoricoContaBReq implements CatalystSerializable {
    public int idConta;

    public GetHistoricoContaBReq(int idConta) {
        this.idConta = idConta;
    }

    public GetHistoricoContaBReq() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(idConta);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        idConta = bufferInput.readInt();
    }
}
