package ar.edu.itba.pod.api.models;


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class CountyAndInfractionDto implements DataSerializable {
    private String county;
    private String infractionDescription;

    // Default constructor is required for DataSerializable
    public CountyAndInfractionDto() {
    }

    public CountyAndInfractionDto(String county, String infractionDescription){
        this.county = county;
        this.infractionDescription = infractionDescription;
    }

    // Getters and setters
    public String getCounty() {
        return county;
    }

    public String getInfractionDescription() {
        return infractionDescription;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(county);
        out.writeUTF(infractionDescription);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        county = in.readUTF();
        infractionDescription = in.readUTF();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountyAndInfractionDto)) return false;
        CountyAndInfractionDto that = (CountyAndInfractionDto) o;
        return county.equals(that.county) && infractionDescription.equals(that.infractionDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(county, infractionDescription);
    }
}
