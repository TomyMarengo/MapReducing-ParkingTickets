package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class InfractionFineDto implements DataSerializable {
    private String infractionDefinition;
    private Double fineAmount;

    public InfractionFineDto() {
    }

    public InfractionFineDto(String infractionDefinition, Double fineAmount) {
        this.infractionDefinition = infractionDefinition;
        this.fineAmount = fineAmount;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(infractionDefinition);
        out.writeDouble(fineAmount);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        infractionDefinition = objectDataInput.readUTF();
        fineAmount = objectDataInput.readDouble();
    }

    @Override
    public String toString() {
        return "InfractionFineDto{" +
                "infractionDefinition='" + infractionDefinition + '\'' +
                ", fineAmount=" + fineAmount +
                '}';
    }

    public String getInfractionDefinition() {
        return infractionDefinition;
    }

    public Double getFineAmount() {
        return fineAmount;
    }
}
