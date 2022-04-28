package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetBookPrecoReq implements CatalystSerializable {
    public int idBook;

    public GetBookPrecoReq(int idBook) {
        this.idBook = idBook;
    }

    public GetBookPrecoReq() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {

        bufferOutput.writeInt(idBook);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        idBook = bufferInput.readInt();
    }
}
