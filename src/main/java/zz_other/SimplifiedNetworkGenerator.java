package zz_other;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.osm.networkReader.SupersonicOsmNetworkReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

public class SimplifiedNetworkGenerator {

    private static final String inputFile = "scenarios/pricing/munich-region.osm.pbf";
    private static final String outputFile = "scenarios/pricing/munich-region.xml.gz";
    private static final CoordinateTransformation ct =
            TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:31468");

    public static void main(String[] args) {

        Network network = NetworkUtils.createNetwork();

        network = new SupersonicOsmNetworkReader.Builder()
                .setCoordinateTransformation(ct)
                //.setAfterLinkCreated((link, osmTags, isReverse) -> link.setAllowedModes(new HashSet<>(Arrays.asList(TransportMode.car, TransportMode.bike))))
                .build().read(inputFile);

        //NetworkUtils.isMultimodal(network);
        NetworkUtils.runNetworkSimplifier(network);
        NetworkUtils.runNetworkCleaner(network);

        new NetworkWriter(network).write(outputFile);





    }

}
