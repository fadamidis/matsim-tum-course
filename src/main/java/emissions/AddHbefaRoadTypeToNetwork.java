package emissions;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.network.io.NetworkWriter;

public class AddHbefaRoadTypeToNetwork {

    private static final String HBEFA_ROAD_TYPE = "hbefa_road_type";

    public static void main(String[] args) {

        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile("scenarios/munich/network/munich-bicycleimproved.xml.gz");

        for(Link link: network.getLinks().values()) {

            if (link.getCapacity() < 6000) {

                link.getAttributes().putAttribute(HBEFA_ROAD_TYPE, "URB/Local/50");
                //link.getAttributes().putAttribute(HBEFA_ROAD_TYPE, "Urban");
            } else {

                link.getAttributes().putAttribute(HBEFA_ROAD_TYPE, "RUR/MW/>130");
                //link.getAttributes().putAttribute(HBEFA_ROAD_TYPE, "Urban");
            }

        }

        new NetworkWriter(network).write("scenarios/munich/network/munich-bicycle-improved_hbefa.xml.gz");
    }

}
