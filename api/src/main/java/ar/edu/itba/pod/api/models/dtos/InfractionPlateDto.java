package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Date;

public class InfractionPlateDto implements DataSerializable {
    private String plate;
    private Date date;
    private String county;

    public InfractionPlateDto() {}

    public InfractionPlateDto(String plate, Date date, String county) {
        this.plate = plate;
        this.date = date;
        this.county = county;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeLong(date.getTime());
        out.writeUTF(county);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        date = new Date(in.readLong());
        county = in.readUTF();
    }
}
