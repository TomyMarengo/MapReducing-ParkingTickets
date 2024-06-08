package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class FineAmountSumDto implements DataSerializable {
    private BigDecimal sum;
    private Integer count;

    public FineAmountSumDto() {
        // Empty constructor needed by Hazelcast
    }

    public FineAmountSumDto(BigDecimal sum, Integer count) {
        this.sum = sum;
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public void writeData(com.hazelcast.nio.ObjectDataOutput out) throws IOException {
        out.writeUTF(sum.toString());
        out.writeInt(count);
    }

    @Override
    public void readData(com.hazelcast.nio.ObjectDataInput in) throws IOException {
        sum = new BigDecimal(in.readUTF());
        count = in.readInt();
    }

    @Override
    public String toString() {
        return "FineAmountSumDto{" +
                "sum=" + sum +
                ", count=" + count +
                '}';
    }
}