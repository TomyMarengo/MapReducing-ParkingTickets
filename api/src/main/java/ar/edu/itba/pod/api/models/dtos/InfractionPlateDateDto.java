package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class InfractionPlateDateDto implements DataSerializable {
    private String plate;
    private Date date;
    private String county;

    public InfractionPlateDateDto() {}

    public InfractionPlateDateDto(String plate, Date date, String county) {
        this.plate = plate;
        this.date = date;
        this.county = county;
    }

    public String getPlate() {
        return plate;
    }

    public String getCounty(){
        return county;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfractionPlateDateDto that = (InfractionPlateDateDto) o;
        return Objects.equals(plate, that.plate) &&
                Objects.equals(date, that.date) &&
                Objects.equals(county, that.county);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, date, county);
    }
}
