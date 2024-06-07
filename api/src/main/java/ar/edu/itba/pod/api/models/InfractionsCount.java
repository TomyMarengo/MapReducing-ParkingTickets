package ar.edu.itba.pod.api.models;

public class InfractionsCount implements Comparable<InfractionsCount> {
    private String infractionDescription;
    private int count;

    public InfractionsCount() {
    }

    public InfractionsCount(String infractionDescription, int count) {
        this.infractionDescription = infractionDescription;
        this.count = count;
    }

    public String getInfractionDescription() {
        return infractionDescription;
    }

    public void setInfractionDescription(String infractionDescription) {
        this.infractionDescription = infractionDescription;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public int compareTo(InfractionsCount other) {
        int compare = Integer.compare(other.count, this.count);
        if(compare == 0){
            return this.infractionDescription.compareTo(other.infractionDescription);
        }
        return compare;
    }


    @Override
    public String toString() {
        return "InfractionsCount{" +
                "infractionDescription='" + infractionDescription + '\'' +
                ", count=" + count +
                '}';
    }
}
