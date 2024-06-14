package ar.edu.itba.pod.api.models.dtos;

import ar.edu.itba.pod.api.interfaces.CsvWritable;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/* No se usa, solo para query 4 alternativa */
public class CountyPlateByInfractionCountDto implements Comparable<CountyPlateByInfractionCountDto>, DataSerializable, CsvWritable {
    private String county;
    private String plate;
    private int count;

    // No-arg constructor for deserialization
    public CountyPlateByInfractionCountDto() {}

    public CountyPlateByInfractionCountDto(final String county, final String plate, final int count) {
        this.county = county;
        this.plate = plate;
        this.count = count;
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeUTF(county);
        out.writeUTF(plate);
        out.writeInt(count);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        county = in.readUTF();
        plate = in.readUTF();
        count = in.readInt();
    }

    public String getCounty() {
        return county;
    }

    public String getPlate() {
        return plate;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(final CountyPlateByInfractionCountDto o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.county, o.county);
    }

    @Override
    public String toString() {
        return county + ";" + plate + ";" + count;
    }

    @Override
    public String toCsv() {
        return county + ";" + plate + ";" + count;
    }
}