package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetSaldoReq implements CatalystSerializable {
    public int idConta;

    public GetSaldoReq(int idConta) {
        this.idConta = idConta;
    }

    public GetSaldoReq() {
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
