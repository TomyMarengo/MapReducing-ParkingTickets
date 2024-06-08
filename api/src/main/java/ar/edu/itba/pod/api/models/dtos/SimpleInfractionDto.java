package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class SimpleInfractionDto implements DataSerializable {
    private String definition;

    public SimpleInfractionDto() {
    }

    public SimpleInfractionDto(String definition) {
        this.definition = definition;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(definition);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        definition = in.readUTF();
    }

    public String getDefinition() {
        return definition;
    }
}
