package analysis;

import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;

public class RunVehicleEnterTrafficLinkLeaveCounterHandler {

    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork("scenarios/munich/network/munich-bicycle-improved.xml.gz");

        EventsManager manager = EventsUtils.createEventsManager();

        VehicleEntersTrafficLinkLeaveCounterHandler vehicleEntersTrafficEventHandler = new VehicleEntersTrafficLinkLeaveCounterHandler(network);

        manager.addHandler(vehicleEntersTrafficEventHandler);

        new MatsimEventsReader(manager).readFile("scenarios/munich/output/output-measure-5pct/munich-measure-5pct.output_events.xml.gz");

        vehicleEntersTrafficEventHandler.writeResults("scenarios/munich/output/counts-measure.csv");
    }
}
