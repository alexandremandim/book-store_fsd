package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreSearchRep implements CatalystSerializable {
    public int idBook;

    public StoreSearchRep() {}

    public StoreSearchRep(int idBook) {
        this.idBook = idBook;
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
