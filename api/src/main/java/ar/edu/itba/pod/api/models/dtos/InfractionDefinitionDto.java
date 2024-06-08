package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class InfractionDefinitionDto implements DataSerializable {
    private String definition;

    public InfractionDefinitionDto() {
    }

    public InfractionDefinitionDto(String definition) {
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
