package zz_other;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;

public class RunMultimodalLinkDemandEventHandler {

    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork("scenarios/miniMatsim/network.xml");

        EventsManager manager = EventsUtils.createEventsManager();

        //MultimodalLinkDemandEventHandler multimodalLinkDemandEventHandler = new MultimodalLinkDemandEventHandler(network);

        //manager.addHandler(multimodalLinkDemandEventHandler);

        new MatsimEventsReader(manager).readFile("scenarios/miniMatsim/output/output_events.xml.gz");

        //multimodalLinkDemandEventHandler.printResults("scenarios/miniMatsim/output/output_links_events.csv");
    }
}
