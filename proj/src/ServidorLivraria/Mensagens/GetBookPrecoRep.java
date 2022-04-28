package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetBookPrecoRep implements CatalystSerializable {
    public float preco;

    public GetBookPrecoRep(float preco) {
        this.preco = preco;
    }

    public GetBookPrecoRep() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeFloat(preco);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        preco = bufferInput.readFloat();
    }
}
