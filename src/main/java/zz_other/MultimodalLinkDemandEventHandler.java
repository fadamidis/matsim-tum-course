/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** *//*


package ZZ_other;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
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

*/
/**
 * @author Ihab
 * @adapted Filippos
 *//*

public class MultimodalLinkDemandEventHandler implements LinkLeaveEventHandler {


	private static final Logger log = Logger.getLogger(MultimodalLinkDemandEventHandler.class);
	private Network network;

	private Map<Id<Link>,Integer> linkId2CarDemand = new HashMap<Id<Link>, Integer>();
	private Map<Id<Link>,Integer> linkId2BikeDemand = new HashMap<Id<Link>, Integer>();

	public MultimodalLinkDemandEventHandler(Network network) {
		this.network = network;
	}

	@Override
	public void reset(int iteration) {
		this.linkId2CarDemand.clear();
	}
	
	@Override
	public void handleEvent(LinkLeaveEvent event) {

		if (event.getVehicleId().equals(TransportMode.car)) {
			if (this.linkId2CarDemand.containsKey(event.getLinkId())) {
				int agents = this.linkId2CarDemand.get(event.getLinkId());
				this.linkId2CarDemand.put(event.getLinkId(), agents + 1);

			} else {
				this.linkId2CarDemand.put(event.getLinkId(), 1);
			}
		} else if (event.getVehicleId().equals(TransportMode.bike)) {
			if (this.linkId2BikeDemand.containsKey(event.getLinkId())) {
				int agents = this.linkId2BikeDemand.get(event.getLinkId());
				this.linkId2BikeDemand.put(event.getLinkId(), agents + 1);

			} else {
				this.linkId2BikeDemand.put(event.getLinkId(), 1);
			}
		}


	}

	public void printResults(String fileName) {
		File file = new File(fileName);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("link;agents");
			bw.newLine();
			
			for (Id<Link> linkId : this.network.getLinks().keySet()){
				
				double volume = 0.;
				if (this.linkId2CarDemand.get(linkId) != null) {
					volume = this.linkId2CarDemand.get(linkId);
				}
				bw.write(linkId + ";" + volume);
				bw.newLine();
			}
			
			bw.close();
			log.info("Output written to " + fileName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<Id<Link>, Integer> getLinkId2demand() {
		return linkId2CarDemand;
	}

}
*/
