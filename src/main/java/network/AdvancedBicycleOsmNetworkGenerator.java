package network;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.MultimodalNetworkCleaner;

import java.util.HashSet;
import java.util.Set;

public class AdvancedBicycleOsmNetworkGenerator {

    private static final String inputFile = "scenarios/munich/network/munich-bicycle.xml.gz";
    private static final String outputFile = "scenarios/munich/network/munich-bicycle.xml.gz";

    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork(inputFile);

        Set<String> car = new HashSet<String>();
        car.add(TransportMode.car);


        for (Link link : network.getLinks().values()) {

            if (link.getAttributes().getAttribute("bike") != null) {

                if (link.getAttributes().getAttribute("bike").toString().equals("use_sidepath")) {
                    System.out.println("TRUE");

                    link.setAllowedModes(car);
                }

            }

        }

        MultimodalNetworkCleaner multimodalNetworkCleaner =  new MultimodalNetworkCleaner(network);

        multimodalNetworkCleaner.removeNodesWithoutLinks();

        // Then clean bike network
        multimodalNetworkCleaner.run(Set.of(TransportMode.bike));

        // First clean car network
        multimodalNetworkCleaner.run(Set.of(TransportMode.car));


        // Write clean network
        new NetworkWriter(network).write(outputFile);


    }
}
