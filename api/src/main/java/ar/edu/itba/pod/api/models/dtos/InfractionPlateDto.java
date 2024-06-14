package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;


/* No se usa, es para la query4 alternativa*/
public class InfractionPlateDto implements DataSerializable {
    private String plate;
    private String county;

    public InfractionPlateDto() {}

    public InfractionPlateDto(String plate, String county) {
        this.plate = plate;
        this.county = county;
    }

    public String getPlate() {
        return plate;
    }


    public String getCounty() {
        return county;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeUTF(county);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        county = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfractionPlateDto that = (InfractionPlateDto) o;
        return Objects.equals(plate, that.plate) &&
                Objects.equals(county, that.county);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, county);
    }
}