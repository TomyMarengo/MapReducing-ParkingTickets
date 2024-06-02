package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Infraction implements DataSerializable {
    private Integer code;
    private String definition;

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {

    }
}
