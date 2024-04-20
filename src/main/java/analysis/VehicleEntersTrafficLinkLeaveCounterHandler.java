package analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.VehicleEntersTrafficEvent;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fadamidis based on VSP https://github.com/matsim-vsp/matsim-analysis
 *
 */

public class VehicleEntersTrafficLinkLeaveCounterHandler implements VehicleEntersTrafficEventHandler, LinkLeaveEventHandler {

    private Network network;
    private Map<String, String> agentModeMapper = new HashMap<String, String>();
    private Map<Id<Link>, Integer> linkLeaveCounterCar = new HashMap<Id<Link>, Integer>();
    private Map<Id<Link>, Integer> linkLeaveCounterBike = new HashMap<Id<Link>, Integer>();

    public VehicleEntersTrafficLinkLeaveCounterHandler(Network network) {
        this.network = network;
    }

    @Override
    public void reset(int iteration) {
        this.agentModeMapper.clear();
        this.linkLeaveCounterCar.clear();
        this.linkLeaveCounterBike.clear();
    }

    // Find the mode that each agent uses when entering traffic
    @Override
    public void handleEvent(VehicleEntersTrafficEvent event) {
        this.agentModeMapper.put(event.getVehicleId().toString(), event.getNetworkMode().toString());

    }


    @Override
    public void handleEvent(LinkLeaveEvent event) {

        String mode = agentModeMapper.get(event.getVehicleId().toString());

        if (mode != null) {
            if (mode.equals("car")) {
                if (this.linkLeaveCounterCar.containsKey(event.getLinkId())) {
                    int agents = this.linkLeaveCounterCar.get(event.getLinkId());
                    this.linkLeaveCounterCar.put(event.getLinkId(), agents + 1);

                } else {
                    this.linkLeaveCounterCar.put(event.getLinkId(), 1);
                }

            }

            if (mode.equals("bike")) {
                if (this.linkLeaveCounterBike.containsKey(event.getLinkId())) {
                    int agents = this.linkLeaveCounterBike.get(event.getLinkId());
                    this.linkLeaveCounterBike.put(event.getLinkId(), agents + 1);

                } else {
                    this.linkLeaveCounterBike.put(event.getLinkId(), 1);
                }
            }
        }



    }

    public void writeResults(String fileName) {
        File file = new File(fileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("link;car;bike");
            bw.newLine();

            for (Id<Link> linkId : this.network.getLinks().keySet()) {

                double volumeCar = 0.;
                double volumeBike = 0.;

                if ((this.linkLeaveCounterCar.get(linkId) != null)) {
                    volumeCar = this.linkLeaveCounterCar.get(linkId);
                }

                if ((this.linkLeaveCounterBike.get(linkId) != null)) {
                    volumeBike = this.linkLeaveCounterBike.get(linkId);
                }

                bw.write(linkId + ";" + volumeCar + ";" + volumeBike);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
