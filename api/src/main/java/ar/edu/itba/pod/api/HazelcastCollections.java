package ar.edu.itba.pod.api;

public enum HazelcastCollections {
    INFRACTIONS_MAP("infractions"),
    TICKETS_BY_INFRACTION_MAP("ticketsByInfraction"),
    TICKETS_BY_COUNTY_MAP("ticketsByCounty"),
    AGENCY_FINE_MAP("agencyFine");

    private final String name;

    HazelcastCollections(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
