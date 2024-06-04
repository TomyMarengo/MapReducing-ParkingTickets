package ar.edu.itba.pod.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import java.util.Collections;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        String networkInterface = null;

        // Busca el parámetro -Daddress en los argumentos
        for (String arg : args) {
            if (arg.startsWith("-Daddress=")) {
                networkInterface = arg.substring("-Daddress=".length());
                break;
            }
        }

        // Verifica si se proporcionó la interfaz de red
        if (networkInterface == null) {
            logger.error("No network interface provided. Use -Daddress=\"...\"");
            throw new IllegalArgumentException("No network interface provided");
        }

        System.out.println(networkInterface);

        logger.info("Network interface: {}", networkInterface);
        logger.info("Setting up server");

        // Config
        Config config = new Config();

        // Group config
        GroupConfig groupConfig = new GroupConfig().setName("g2").setPassword("g2-pass");
        config.setGroupConfig(groupConfig);

        // **** Network config **** //

        // Multicast config
        MulticastConfig multicastConfig = new MulticastConfig();

        // Join config
        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        // Interfaces config
        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList(networkInterface))
                .setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);

        // **** End Network config **** //

        /*// Management Center Config
        ManagementCenterConfig managementCenterConfig = new
                ManagementCenterConfig()
                .setUrl("http://localhost:32768/mancenter/")
                .setEnabled(true);
        config.setManagementCenterConfig(managementCenterConfig);
*/
        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
