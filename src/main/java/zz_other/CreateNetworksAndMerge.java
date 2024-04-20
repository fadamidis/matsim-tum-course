package zz_other;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.osm.networkReader.SupersonicOsmNetworkReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

public class CreateNetworksAndMerge {

    private static final String inputFileCar = "scenarios/munich/munich.osm.pbf";
    private static final String inputFileBike = "scenarios/munich/munich.osm.pbf";
    private static final String outputFile = "scenarios/munich/munich_network.xml.gz";

    private static final CoordinateTransformation ct =
            TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, TransformationFactory.DHDN_GK4);

    public static void main(String[] args) {

        // Create car network
        Network networkCar = NetworkUtils.createNetwork();

        networkCar = new SupersonicOsmNetworkReader.Builder().setCoordinateTransformation(ct)
                .build().read(inputFileCar);

        NetworkUtils.runNetworkSimplifier(networkCar);
        NetworkUtils.runNetworkCleaner(networkCar);

//        // Create bike network
//        Network networkBike = NetworkUtils.createNetwork();
//
//        networkBike = new OsmBicycleReader.Builder().setCoordinateTransformation(ct).build().read(inputFileBike);
//
//        for (Link link : networkBike.getLinks().values()) {
//
//            if (link.getAllowedModes().contains("car")) {
//                //link.setAllowedModes();
//            }
//        }
//
//        NetworkUtils.runNetworkSimplifier(networkBike);
//        NetworkUtils.runNetworkCleaner(networkBike);
//
//        // Create mrged network
//        Network network = NetworkUtils.createNetwork();
//
//        MergeNetworks.merge(networkCar, "car", networkBike, "bike", network);


        new NetworkWriter(networkCar).write(outputFile);

    }
}
