package ServidorLivraria.Mensagens;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreSearchReq implements CatalystSerializable {
    public String title;
    public int storeid; /* id do obj */

    public StoreSearchReq() {}

    public StoreSearchReq(String title, int storeid) {
        this.title = title;
        this.storeid = storeid;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeString(title);
        bufferOutput.writeInt(storeid);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        title = bufferInput.readString();
        storeid = bufferInput.readInt();
    }
}
