package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class TicketByInfractionDto implements Comparable<TicketByInfractionDto>, DataSerializable, CsvWritable {
    private String infractionCode;
    private int count;

    // No-arg constructor for deserialization
    public TicketByInfractionDto() {}

    public TicketByInfractionDto(String infractionCode, int count) {
        this.infractionCode = infractionCode;
        this.count = count;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(infractionCode);
        out.writeInt(count);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        infractionCode = in.readUTF();
        count = in.readInt();
    }

    public String getInfractionCode() {
        return infractionCode;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(TicketByInfractionDto other) {
        int countComparison = Integer.compare(other.count, this.count);
        return countComparison != 0 ? countComparison : this.infractionCode.compareTo(other.infractionCode);
    }

    @Override
    public String toString() {
        return infractionCode + "; " + count;
    }

    @Override
    public String toCsv() {
        return infractionCode + ";" + count;
    }
}