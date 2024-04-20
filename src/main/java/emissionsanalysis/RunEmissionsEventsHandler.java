package emissionsanalysis;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.events.EmissionEventsReader;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;

public class RunEmissionsEventsHandler {


    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork("scenarios/munich/network/munich-bicycle_hbefa.xml.gz");

        EventsManager manager = EventsUtils.createEventsManager();

        // Insert here which pollutant should be calculated
        Pollutant pollutant = Pollutant.PM2_5;

        // Handle emission events for the whole simulation duration
        EmissionsEventsHandler emissionsEventsHandler = new EmissionsEventsHandler(network, pollutant);

        manager.addHandler(emissionsEventsHandler);

        new EmissionEventsReader(manager).readFile("/Users/adfil/Desktop/Applied Transport Modeling with MATSim/mat-sim-ws-2324-filippos/scenarios/munich/emissions/output/emissions-base-5pct.events.offline.xml.gz");

        // Insert the name of the output file
        emissionsEventsHandler.writeResults("scenarios/munich/emissions/output/emissions-base-pm.csv");

    }
}
