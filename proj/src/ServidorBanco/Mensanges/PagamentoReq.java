package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class PagamentoReq implements CatalystSerializable {
    public int idConta;
    public float valor;

    public PagamentoReq(int idConta, float valor) {
        this.idConta = idConta;
        this.valor = valor;
    }

    public PagamentoReq() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(idConta);
        bufferOutput.writeFloat(valor);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        idConta = bufferInput.readInt();
        valor = bufferInput.readFloat();
    }
}
