package zz_other;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fadamidis
 * @based TUBerlin VSP https://github.com/matsim-vsp/matsim-analysis
 */

public class LinkLeaveCounterHandler implements LinkLeaveEventHandler {

    private Network network;
    private Map<Id<Link>, Integer> linkLeaveCounter = new HashMap<Id<Link>, Integer>();

    public LinkLeaveCounterHandler(Network network) {
        this.network = network;
    }

    @Override
    public void reset(int iteration) {
        this.linkLeaveCounter.clear();
    }

    public void handleEvent(LinkLeaveEvent event) {

        if (this.linkLeaveCounter.containsKey(event.getLinkId())) {
            int agents = this.linkLeaveCounter.get(event.getLinkId());
            this.linkLeaveCounter.put(event.getLinkId(), agents + 1);

        } else {
            this.linkLeaveCounter.put(event.getLinkId(), 1);
        }
    }

    public void writeResults(String fileName) {
        File file = new File(fileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("link;agents");
            bw.newLine();

            for (Id<Link> linkId : this.network.getLinks().keySet()) {

                double volume = 0.;
                if (this.linkLeaveCounter.get(linkId) != null) {
                    volume = this.linkLeaveCounter.get(linkId);
                }
                bw.write(linkId + ";" + volume);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
