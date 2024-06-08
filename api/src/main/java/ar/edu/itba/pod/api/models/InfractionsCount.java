package ar.edu.itba.pod.api.models;

public class InfractionsCount implements Comparable<InfractionsCount> {
    private String infractionDefinition;
    private int count;

    public InfractionsCount() {
    }

    public InfractionsCount(String infractionDefinition, int count) {
        this.infractionDefinition = infractionDefinition;
        this.count = count;
    }

    public String getInfractionDefinition() {
        return infractionDefinition;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(InfractionsCount other) {
        int compare = Integer.compare(other.count, this.count);
        if(compare == 0){
            return this.infractionDefinition.compareTo(other.infractionDefinition);
        }
        return compare;
    }

    @Override
    public String toString() {
        return "InfractionsCount{" +
                "infractionDescription='" + infractionDefinition + '\'' +
                ", count=" + count +
                '}';
    }
}
