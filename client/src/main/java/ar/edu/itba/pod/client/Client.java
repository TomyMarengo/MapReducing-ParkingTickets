package ar.edu.itba.pod.client;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

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
        String listName = "tickets";
        System.out.println("Loading tickets from " + arguments.getInPath() + " for city " + arguments.getCity());
        IList<Ticket> tickets = hazelcastInstance.getList(listName);
        CsvFileIterator.parseTicketsCsv(arguments.getInPath(), arguments.getCity(), tickets);
        for (Object o : hazelcastInstance.getList(listName)) {
            Ticket ticket = (Ticket) o;
            System.out.println(ticket);
        }

        HazelcastClient.shutdownAll();
    }
}
