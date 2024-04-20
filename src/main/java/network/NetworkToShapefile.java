package network;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.utils.gis.matsim2esri.network.Links2ESRIShape;

import java.util.Set;

public class NetworkToShapefile {

    private static final String NETWORK = "scenarios/munich/network/munich-bicycle-improved.xml.gz";
    private static final String SHAPEFILE = "scenarios/munich/network/shapefile-measure/munich-measure-shapefile.shp";


    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork(NETWORK);

        TransportModeNetworkFilter transportModeNetworkFilter = new TransportModeNetworkFilter(network);

        Network carNetwork = NetworkUtils.createNetwork();

        transportModeNetworkFilter.filter(carNetwork, Set.of(TransportMode.car));

        Links2ESRIShape links2ESRIShape = new Links2ESRIShape(carNetwork, SHAPEFILE, "EPSG:31468");

        links2ESRIShape.write();
    }
}
