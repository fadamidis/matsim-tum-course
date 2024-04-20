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

package network;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.locationtech.jts.geom.Coordinate;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.PolylineFeatureFactory;
import org.matsim.core.utils.gis.ShapeFileWriter;
import org.opengis.feature.simple.SimpleFeature;

*//**
* @author ikaddoura
 * @adapted fadamidis
*//*

public class Network2Shape {
	private final static Logger log = Logger.getLogger(Network2Shape.class);

	public static void main(String[] args) {

		Network network = NetworkUtils.readNetwork("scenarios/munich/munich-bicycle.xml.gz");

		String outputDirectory = "scenarios/munich/networkShapefileMultimodal";

		CoordinateTransformation ct =
				TransformationFactory.getCoordinateTransformation("EPSG:31468", "EPSG:31468");

		exportNetwork2Shp(network, outputDirectory, "EPSG:31468", ct);
	}

	public static void exportNetwork2Shp(Network network, String outputDirectory, String crs, CoordinateTransformation ct){
		
		String outputPath = outputDirectory + "network-shp/";
		File file = new File(outputPath);
		file.mkdirs();
		
		PolylineFeatureFactory factory = new PolylineFeatureFactory.Builder()
		.setCrs(MGC.getCRS(crs))
		.setName("Link")
		.addAttribute("Id", String.class)
		.addAttribute("Length", Double.class)
		.addAttribute("capacity", Double.class)
		.addAttribute("lanes", Double.class)
		.addAttribute("Freespeed", Double.class)
		.addAttribute("Modes", String.class)
		.create();
		
		Collection<SimpleFeature> features = new ArrayList<SimpleFeature>();

		for (Link link : network.getLinks().values()){
			SimpleFeature feature = factory.createPolyline(
					new Coordinate[]{
							new Coordinate(MGC.coord2Coordinate(ct.transform(link.getFromNode().getCoord()))),
							new Coordinate(MGC.coord2Coordinate(ct.transform(link.getToNode().getCoord())))
					}, new Object[] {link.getId(), link.getLength(), link.getCapacity(), link.getNumberOfLanes(), link.getFreespeed(), link.getAllowedModes()
					}, null
			);
			features.add(feature);
		}
		
		log.info("Writing network to shapefile... ");

		ShapeFileWriter.writeGeometries(features, outputPath + "network.shp");

		log.info("Writing network to shapefile... Done.");
	}



}*/

