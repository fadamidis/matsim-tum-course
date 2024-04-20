/*
package org.matsim.pt2matsim.run;

import org.matsim.pt.transitSchedule.api.TransitSchedule;
import org.matsim.pt2matsim.gtfs.GtfsConverter;
import org.matsim.pt2matsim.gtfs.GtfsFeed;
import org.matsim.pt2matsim.gtfs.GtfsFeedImpl;
import org.matsim.pt2matsim.tools.ScheduleTools;
import org.matsim.vehicles.VehicleUtils;
import org.matsim.vehicles.Vehicles;

import java.util.Arrays;
import java.util.List;

public class MergeSchedules {

    static final String COORDINATE_SYSTEM = "EPSG:31468";
    static final List<String> GTFS_DIRS = Arrays.asList("munich/input/mvg_gtfs/", "munich/input/mvv_gtfs/", "munich/input/regional_gtfs/");
    static final String TRANSIT_SCHEDULE_OUTPUT = "munich/output/munichSchedule.xml";
    static final String TRANSIT_VEHICLES_OUTPUT = "munich/output/munichVehicles.xml";

    public static void main(String[] args) {
        TransitSchedule transitSchedule = prepareTransitSchedule(GTFS_DIRS.get(0));
        for(String gtfsDir : GTFS_DIRS.subList(1, GTFS_DIRS.size())) {
            TransitSchedule additionalSchedule = prepareTransitSchedule(gtfsDir);
            ScheduleTools.mergeSchedules(transitSchedule, additionalSchedule);
        }

        Vehicles transitVehicles = VehicleUtils.createVehiclesContainer();
        ScheduleTools.createVehicles(transitSchedule, transitVehicles);

        ScheduleTools.writeTransitSchedule(transitSchedule, TRANSIT_SCHEDULE_OUTPUT);
        ScheduleTools.writeVehicles(transitVehicles, TRANSIT_VEHICLES_OUTPUT);

    }
    private static TransitSchedule prepareTransitSchedule(String gtfsDir) {
        String param = GtfsConverter.DAY_WITH_MOST_TRIPS;
        GtfsFeed gtfsFeed = new GtfsFeedImpl(gtfsDir);
        GtfsConverter converter = new GtfsConverter(gtfsFeed);
        converter.convert(param, COORDINATE_SYSTEM);
        return converter.getSchedule();
    }
}
*/