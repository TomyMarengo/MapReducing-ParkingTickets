package ar.edu.itba.pod.api.models.dtos;


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class CountyInfractionDto implements DataSerializable {
    private String county;
    private String infractionDefinition;

    // Default constructor is required for DataSerializable
    public CountyInfractionDto() {
    }

    public CountyInfractionDto(String county, String infractionDefinition){
        this.county = county;
        this.infractionDefinition = infractionDefinition;
    }

    // Getters and setters
    public String getCounty() {
        return county;
    }

    public String getInfractionDefinition() {
        return infractionDefinition;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(county);
        out.writeUTF(infractionDefinition);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        county = in.readUTF();
        infractionDefinition = in.readUTF();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountyInfractionDto that)) return false;
        return county.equals(that.county) && infractionDefinition.equals(that.infractionDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(county, infractionDefinition);
    }
}
