package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Date;



/* No se usa, solo para query 4 alternativa */
public class Ticket implements DataSerializable {
    public static final Integer FIELD_COUNT = 6;
    private String plate;
    private Date issueDate;
    private String infractionCode;
    private Double fineAmount;
    private String countyName;
    private String issuingAgency;

    // No-arg constructor for deserialization
    public Ticket() {
    }

    public Ticket(String plate, Date issueDate, String infractionCode, Double fineAmount, String countyName, String issuingAgency) {
        this.plate = plate;
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
        this.countyName = countyName;
        this.issuingAgency = issuingAgency;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeLong(issueDate.getTime());
        out.writeUTF(infractionCode);
        out.writeDouble(fineAmount);
        out.writeUTF(countyName);
        out.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        issueDate = new Date(in.readLong());
        infractionCode = in.readUTF();
        fineAmount = in.readDouble();
        countyName = in.readUTF();
        issuingAgency = in.readUTF();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "plate='" + plate + '\'' +
                ", issueDate=" + issueDate +
                ", infractionCode=" + infractionCode +
                ", fineAmount=" + fineAmount +
                ", countyName='" + countyName + '\'' +
                ", issuingAgency='" + issuingAgency + '\'' +
                '}';
    }

    public String getPlate() {
        return plate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public String getInfractionCode() {
        return infractionCode;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }
}