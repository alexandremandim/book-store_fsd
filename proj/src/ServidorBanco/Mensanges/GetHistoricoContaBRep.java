package ServidorBanco.Mensanges;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

import java.util.ArrayList;
import java.util.List;

public class GetHistoricoContaBRep implements CatalystSerializable {
    public List<String> historico;

    public GetHistoricoContaBRep(List <String> historico) {
        this.historico = historico;
    }

    public GetHistoricoContaBRep() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        for(String pagamento: historico){
            bufferOutput.writeString(pagamento);
        }
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        historico = new ArrayList<>();
        while(bufferInput.hasRemaining()){
            String pagamento = bufferInput.readString();
            historico.add(pagamento);
        }
    }
}
