package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.client.utils.Arguments;
import ar.edu.itba.pod.client.utils.CsvFileIterator;
import ar.edu.itba.pod.client.utils.Parser;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("Setting up client");
        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        Arguments arguments = Parser.parse(args);

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g2").setPassword("g2-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(arguments.getAddresses());
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        String ticketsName = "tickets";
        String infractionsName = "infractions";
        IList<Ticket> tickets = hazelcastInstance.getList(ticketsName);
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(infractionsName);

        //TODO: Time this
        System.out.println("Loading infractions from " + arguments.getInPath() + " for city " + arguments.getCity());
        CsvFileIterator.parseInfractionsCsv(arguments.getInPath(), arguments.getCity(), infractions);
        System.out.println("Infractions loaded: " + infractions.size());

        //TODO: Time this
        System.out.println("Loading tickets from " + arguments.getInPath() + " for city " + arguments.getCity());
        CsvFileIterator.parseTicketsCsv(arguments.getInPath(), arguments.getCity(), tickets);
        System.out.println("Tickets loaded: " + tickets.size());

        //PRINT TICKETS & INFRACTIONS
        System.out.println("Tickets:");
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }

        System.out.println("Infractions:");
        for (String key : infractions.keySet()) {
            System.out.println(infractions.get(key));
        }

        HazelcastClient.shutdownAll();
    }
}
