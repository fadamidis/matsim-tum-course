package emissionsanalysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.events.ColdEmissionEvent;
import org.matsim.contrib.emissions.events.ColdEmissionEventHandler;
import org.matsim.contrib.emissions.events.WarmEmissionEvent;
import org.matsim.contrib.emissions.events.WarmEmissionEventHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmissionsEventsHandler implements WarmEmissionEventHandler, ColdEmissionEventHandler {

    private Network network;
    private Pollutant pollutant;
    private Map<Id<Link>, Double> coldEmissionsCounter = new HashMap<Id<Link>, Double>();
    private Map<Id<Link>, Double> warmEmissionsCounter = new HashMap<Id<Link>, Double>();

    public EmissionsEventsHandler(Network network, Pollutant pollutant) {

        this.network = network;
        this.pollutant = pollutant;
    }

    @Override
    public void reset(int iteration) {

        this.coldEmissionsCounter.clear();
        this.warmEmissionsCounter.clear();
    }

    public void handleEvent(ColdEmissionEvent event) {

        if (this.coldEmissionsCounter.containsKey(event.getLinkId())) {
            double emissions = this.coldEmissionsCounter.get(event.getLinkId());
            this.coldEmissionsCounter.put(event.getLinkId(), emissions + event.getColdEmissions().get(pollutant));
        } else {
            this.coldEmissionsCounter.put(event.getLinkId(), event.getColdEmissions().get(pollutant));
        }

    }

    public void handleEvent(WarmEmissionEvent event) {

        if (this.warmEmissionsCounter.containsKey(event.getLinkId())) {
            double emissions = this.warmEmissionsCounter.get(event.getLinkId());
            this.warmEmissionsCounter.put(event.getLinkId(), emissions + event.getWarmEmissions().get(pollutant));
        } else {
            this.warmEmissionsCounter.put(event.getLinkId(), event.getWarmEmissions().get(pollutant));
        }

    }


    public void writeResults(String fileName) {
        File file = new File(fileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("link;pm");
            bw.newLine();

            double generalTotal = 0.;

            for (Id<Link> linkId : this.network.getLinks().keySet()) {

                double total = 0.;

                if (this.coldEmissionsCounter.get(linkId) != null) {
                    total += this.coldEmissionsCounter.get(linkId);
                }

                if (this.warmEmissionsCounter.get(linkId) != null) {
                    total += this.warmEmissionsCounter.get(linkId);
                }

                generalTotal += total;


                bw.write(linkId + ";" + total);
                bw.newLine();
            }

            bw.close();

            System.out.println("Total " + pollutant.toString() + " emissions: " + generalTotal);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
