package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetBookIsbnRep implements CatalystSerializable {
    public int isbn;

    public GetBookIsbnRep(int isbn) {
        this.isbn = isbn;
    }

    public GetBookIsbnRep() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {

        bufferOutput.writeInt(isbn);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        isbn = bufferInput.readInt();
    }
}
