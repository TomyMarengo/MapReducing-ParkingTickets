package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/* No se usa, solo para query 4 alternativa */
public class PlateAndQuantity implements DataSerializable {
    private String plate;
    private int quantity;

    public PlateAndQuantity() {}

    public PlateAndQuantity(String plate, int quantity) {
        this.plate = plate;
        this.quantity = quantity;
    }

    public String getPlate() {
        return plate;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeInt(quantity);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        quantity = objectDataInput.readInt();
    }
}