package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetSaldoRep implements CatalystSerializable {
    public float saldo;

    public GetSaldoRep(float saldo) {
        this.saldo = saldo;
    }

    public GetSaldoRep() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeFloat(saldo);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        saldo = bufferInput.readFloat();
    }
}
