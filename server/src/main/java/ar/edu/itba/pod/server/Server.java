package ar.edu.itba.pod.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);
    private static final String GROUP_NAME = "g2";
    private static final String GROUP_PASSWORD = "g2-pass";
    private static final String MANAGEMENT_CENTER_URL = "http://localhost:8080/mancenter-3.8.5/";

    public static void main(String[] args) {
        String networkInterface = null;
        boolean managementCenter = false;
        List<String> memberAddresses = new ArrayList<>();

        // Search for arguments
        for (String arg : args) {
            if (arg.startsWith("-Daddress=")) {
                networkInterface = arg.substring("-Daddress=".length());
            }
            if (arg.startsWith("-DmanagementCenter=")) {
                managementCenter = Boolean.parseBoolean(arg.substring("-DmanagementCenter=".length()));
            }
            if (arg.startsWith("-Dmembers=")) {
                String members = arg.substring("-Dmembers=".length());
                memberAddresses.addAll(Arrays.asList(members.split(",")));
            }
        }

        // Verifies if the network interface was provided
        if (networkInterface == null) {
            logger.error("No network interface provided. Use -Daddress=\"...\"");
            throw new IllegalArgumentException("No network interface provided");
        }

        logger.info("Network interface: {}", networkInterface);
        logger.info("Setting up server");

        // Config
        Config config = new Config();

        // Group config
        GroupConfig groupConfig = new GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASSWORD);
        config.setGroupConfig(groupConfig);

        // **** Network config **** //

        // Multicast config
        MulticastConfig multicastConfig = new MulticastConfig().setEnabled(false);

        // TcpIp config
        TcpIpConfig tcpIpConfig = new TcpIpConfig().setEnabled(true);
        for (String member : memberAddresses) {
            tcpIpConfig.addMember(member);
        }

        // Join config
        JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(multicastConfig)
                .setTcpIpConfig(tcpIpConfig);

        // Interfaces config
        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList(networkInterface))
                .setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);

        // **** End Network config **** //

        // Management Center Config
        if (managementCenter) {
            ManagementCenterConfig managementCenterConfig = new
                    ManagementCenterConfig()
                    .setUrl(MANAGEMENT_CENTER_URL)
                    .setEnabled(true);
            config.setManagementCenterConfig(managementCenterConfig);
        }

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
