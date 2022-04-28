package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetBookTitleRep implements CatalystSerializable {
    public String title;

    public GetBookTitleRep(String title) {
        this.title = title;
    }

    public GetBookTitleRep() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeString(title);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        title = bufferInput.readString();
    }
}
