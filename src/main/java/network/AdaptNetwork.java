package network;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;

public class AdaptNetwork {

    private static final String NETWORK = "scenarios/munich/network/munich-bicycle.xml.gz";

    private static final String outputFile = "scenarios/munich/network/munich-bicycle-improved.xml.gz";

    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork(NETWORK);

        for (Link link : network.getLinks().values()) {

            // Reduce the car speed everywhere except highway
            if (link.getCapacity() < 6000) {

                link.setFreespeed(30.0 / 3.6);

                link.getAttributes().removeAttribute("allowed_speed");
                link.getAttributes().putAttribute("allowed_speed", 8.333333);
            }

            // Increase the bike speed on all links where only bike is allowed
            if (link.getAllowedModes().toString().equals("[bike]")) {

                link.setFreespeed(30.0 / 3.6);

                link.getAttributes().removeAttribute("allowed_speed");
                link.getAttributes().putAttribute("allowed_speed", 8.333333);
            }


        }


        new NetworkWriter(network).write(outputFile);
    }

}
