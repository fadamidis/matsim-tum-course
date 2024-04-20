package zz_other;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.NetworkScenarioCut;

public class NetworkScenarioCutterRunner {

    private static final String inputFile = "scenarios/munich/oberbayern.xml.gz";
    private static final String outputFile = "scenarios/munich/munich.xml.gz";

    private static final Coord coordFrom = new Coord(4453553, 5321914);
    private static final Coord coordTo = new Coord(4483971, 5344879);

    public static void main(String[] args) {

        // Read network
        Network network = NetworkUtils.readNetwork(inputFile);

        // Create new NetworkCutter
        NetworkScenarioCut networkScenarioCut = new NetworkScenarioCut(coordFrom, coordTo);

        // Cut network
        networkScenarioCut.run(network);

        // Save new network
        new NetworkWriter(network).write(outputFile);

    }


}
