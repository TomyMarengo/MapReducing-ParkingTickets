package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Infraction implements DataSerializable {
    public static final Integer FIELD_COUNT = 2;
    private String code;
    private String definition;

    public Infraction() {
    }

    public Infraction(String code, String definition) {
        this.code = code;
        this.definition = definition;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(code);
        objectDataOutput.writeUTF(definition);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        code = objectDataInput.readUTF();
        definition = objectDataInput.readUTF();
    }

    @Override
    public String toString() {
        return "Infraction{" +
                "code=" + code +
                ", definition='" + definition + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
