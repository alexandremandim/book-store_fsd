package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetContaBancariaRep implements CatalystSerializable {
    public int idContaBancaria;

    public GetContaBancariaRep(int idContaBancaria) {
        this.idContaBancaria = idContaBancaria;
    }

    public GetContaBancariaRep() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(idContaBancaria);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        idContaBancaria = bufferInput.readInt();
    }
}
