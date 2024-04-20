package network;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.bicycle.network.BicycleOsmNetworkReaderV2;
import org.matsim.contrib.osm.networkReader.OsmBicycleReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.MultimodalNetworkCleaner;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

import java.util.*;

public class BicycleOsmNetworkGenerator {

    private static final String inputFile = "scenarios/munich/network/munich.osm.pbf";
    private static final String outputFile = "scenarios/munich/network/munich-bicycle.xml.gz";
    private static final CoordinateTransformation ct =
            TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:31468");

    private static List<Link> linkRemove = new ArrayList<>();

    public static void main(String[] args) {


        Network network = new OsmBicycleReader.Builder()
                .setCoordinateTransformation(ct)
                .build().read(inputFile);

        MultimodalNetworkCleaner multimodalNetworkCleaner =  new MultimodalNetworkCleaner(network);

        multimodalNetworkCleaner.removeNodesWithoutLinks();


        Set<String> car = new HashSet<String>();
        car.add(TransportMode.car);


        // Some main streets with dedicated bicycle path have mixed car and bike by default. Remove bike path from
        // main road
        for (Link link : network.getLinks().values()) {

            if (link.getAttributes().getAttribute("bike") != null) {

                if (link.getAttributes().getAttribute("bike").toString().equals("use_sidepath")) {

                    link.setAllowedModes(car);
                }

            }

        }


        // First clean bike network
        multimodalNetworkCleaner.run(Set.of(TransportMode.bike));

        // Then clean car network
        multimodalNetworkCleaner.run(Set.of(TransportMode.car));

        for (Link link : network.getLinks().values()) {

            if (link.getLength() <= 60.0) {

                System.out.println(link.getLength());
                link.setCapacity(22500);
            }

            if (link.getNumberOfLanes() == 0) {

                link.setNumberOfLanes(1.0);
            }


        }


        // Write clean network
        new NetworkWriter(network).write(outputFile);


    }
}
