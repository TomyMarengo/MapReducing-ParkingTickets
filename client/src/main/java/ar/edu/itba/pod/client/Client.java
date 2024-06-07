package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.queries.Query;
import ar.edu.itba.pod.client.queries.TopNCollectorAgenciesQuery;
import ar.edu.itba.pod.client.queries.TopNInfractionsByCountyQuery;
import ar.edu.itba.pod.client.queries.TotalTicketsByInfractionQuery;
import ar.edu.itba.pod.client.utils.Arguments;
import ar.edu.itba.pod.client.utils.ArgumentParser;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static final String GROUP_NAME = "g2";
    private static final String GROUP_PASSWORD = "g2-pass";

    public static void main(String[] args) {
        logger.info("Setting up client");

        // Client Config
        ClientConfig clientConfig = new ClientConfig();
        Arguments arguments = ArgumentParser.parse(args);

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASSWORD);
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(arguments.getAddresses());
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        // Ejecutar la query según el argumento
        Query query;
        switch (arguments.getQuery()) {
            case 1:
                query = new TotalTicketsByInfractionQuery();
                break;
            case 2:
                query = new TopNInfractionsByCountyQuery();
                break;
            case 3:
                query = new TopNCollectorAgenciesQuery();
                break;
            default:
                logger.error("Unknown query: " + arguments.getQuery());
                return;
        }

        query.execute(hazelcastInstance, arguments);

        HazelcastClient.shutdownAll();
    }
}
