package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class AgencyFineDto implements DataSerializable {
    private String issuingAgency;
    private Double fineAmount;

    public AgencyFineDto() {
    }

    public AgencyFineDto(String issuingAgency, Double fineAmount) {
        this.issuingAgency = issuingAgency;
        this.fineAmount = fineAmount;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(issuingAgency);
        out.writeDouble(fineAmount);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        issuingAgency = in.readUTF();
        fineAmount = in.readDouble();
    }

    @Override
    public String toString() {
        return "AgencyFineDto{" +
                "issuingAgency='" + issuingAgency + '\'' +
                ", fineAmount=" + fineAmount +
                '}';
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

}
