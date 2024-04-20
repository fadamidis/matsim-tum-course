package zz_other;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;

public class RunLinkLeaveCounterHandler {

    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork("scenarios/munich/network/munich-bicycle.xml.gz");

        EventsManager manager = EventsUtils.createEventsManager();

        LinkLeaveCounterHandler linkLeaveCounterHandler = new LinkLeaveCounterHandler(network);

        manager.addHandler(linkLeaveCounterHandler);

        new MatsimEventsReader(manager).readFile("scenarios/munich/output/output-base-1pct/munich-base-1pct.output_events.xml.gz");

        linkLeaveCounterHandler.writeResults("scenarios/course/demand/output_links_events.csv");
    }
}
