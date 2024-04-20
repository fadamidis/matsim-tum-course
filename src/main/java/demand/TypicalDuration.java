package demand;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TypicalDuration {

    private static final String NETWORK = "scenarios/course/network/munich-bicycle.xml.gz";
    private static final String ACTIVITIES = "scenarios/abitDemand/demand/activities.csv";



    public static void main(String[] args) throws IOException {

        Network network = NetworkUtils.readNetwork(NETWORK);

        Coord lowestCoord = findLowestCoord(network);
        Coord highestCoord = findHighestCoord(network);

        // Create objects
        int totalDuration_home = 0;
        int totalDuration_work = 0;
        int totalDuration_education = 0;
        int totalDuration_recreation = 0;
        int totalDuration_shopping = 0;
        int totalDuration_other = 0;
        int totalDuration_accompany = 0;

        int count_home = 0;
        int count_work = 0;
        int count_education = 0;
        int count_recreation = 0;
        int count_shopping = 0;
        int count_other = 0;
        int count_accompany = 0;

        // Use BufferedReader to read csv file
        BufferedReader reader = new BufferedReader(new FileReader(ACTIVITIES));
        String line;

        // Skip first line (header)
        reader.readLine();

        while ((line = reader.readLine()) != null) {

            String[] values = line.split(",");

            int day = Integer.valueOf(values[2]);

            int start_time = Integer.valueOf(values[3]);
            int end_time = Integer.valueOf(values[4]);

            String purpose = values[5].replace("\"", "");

            double coordX = Double.valueOf(values[7]);
            double coordY = Double.valueOf(values[8]);

            if (findCoordInMunich(new Coord(coordX, coordY), lowestCoord, highestCoord)) {

                if (day == 2) {

                    switch (purpose) {
                        case "HOME":
                            count_home++;
                            totalDuration_home += end_time - start_time;
                            break;
                        case "WORK":
                            count_work++;
                            totalDuration_work += end_time - start_time;
                        case "EDUCATION":
                            count_education++;
                            totalDuration_education +=  end_time - start_time;
                        case "RECREATION":
                            count_recreation++;
                            totalDuration_recreation +=  end_time - start_time;
                        case "SHOPPING":
                            count_shopping++;
                            totalDuration_shopping += end_time - start_time;
                        case "OTHER":
                            count_other++;
                            totalDuration_other += end_time - start_time;
                        case "ACCOMPANY":
                            count_accompany++;
                            totalDuration_accompany += end_time - start_time;
                    }
                }
            }
        }

        System.out.println("The average duration of home is: " + ((double)totalDuration_home/(double)count_home)/(double)60);
        System.out.println("The average duration of work is: " + ((double)totalDuration_work/(double)count_work)/(double)60);
        System.out.println("The average duration of education is: " + ((double)totalDuration_education/(double)count_education)/(double)60);
        System.out.println("The average duration of recreation is: " + ((double)totalDuration_recreation/(double)count_recreation)/(double)60);
        System.out.println("The average duration of shopping is: " + ((double)totalDuration_shopping/(double)count_shopping)/(double)60);
        System.out.println("The average duration of other is: " + ((double)totalDuration_other/(double)count_other)/(double)60);
        System.out.println("The average duration of accompany is: " + ((double)totalDuration_accompany/(double)count_accompany)/(double)60);

    }


    public static boolean findCoordInMunich(Coord coord, Coord lowestCoord, Coord highestCoord) {

        boolean isInMunich = false;

        if ((coord.getX() >= lowestCoord.getX()) && (coord.getX() <= highestCoord.getX()) &&
                (coord.getY() >= lowestCoord.getY()) && (coord.getY() < highestCoord.getY())) {
            isInMunich = true;
        }

        return isInMunich;
    }


    public static Coord findLowestCoord (Network network) {

        double lowest_X = 10000000.00;
        double lowest_Y = 10000000.00;

        // Find lower-left corner of the bounding box of the study area
        for (Node node : network.getNodes().values()) {

            if (node.getCoord().getX() < lowest_X) {lowest_X = node.getCoord().getX();}
            if (node.getCoord().getY() < lowest_Y) {lowest_Y = node.getCoord().getY();}

        }

        Coord coord = new Coord(lowest_X, lowest_Y);
        System.out.println("Lowest X: " + coord.getX() + ", Lowest Y: " + coord.getY());

        return coord ;
    }

    public static Coord findHighestCoord (Network network) {

        double highest_X = -1.00;
        double highest_Y = -1.00;

        // Find upper-right corner of the bounding box of the study area
        for (Node node : network.getNodes().values()) {

            if (node.getCoord().getX() > highest_X) {highest_X = node.getCoord().getX();}
            if (node.getCoord().getY() > highest_Y) {highest_Y = node.getCoord().getY();}

        }

        Coord coord = new Coord(highest_X, highest_Y);
        System.out.println("Highest X: " + coord.getX() + ", Highest Y: " + coord.getY());

        return coord ;
    }
}
