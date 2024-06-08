package ar.edu.itba.pod.api.models.dtos;

import com.hazelcast.nio.serialization.DataSerializable;

public class PlateTicketsDto implements DataSerializable {
    private String plate;
    private Integer tickets;

    public PlateTicketsDto() {}

    public PlateTicketsDto(String plate, Integer tickets) {
        this.plate = plate;
        this.tickets = tickets;
    }

    public String getPlate() {
        return plate;
    }

    public Integer getTickets() {
        return tickets;
    }

    @Override
    public void writeData(com.hazelcast.nio.ObjectDataOutput out) throws java.io.IOException {
        out.writeUTF(plate);
        out.writeInt(tickets);
    }

    @Override
    public void readData(com.hazelcast.nio.ObjectDataInput in) throws java.io.IOException {
        plate = in.readUTF();
        tickets = in.readInt();
    }
}
