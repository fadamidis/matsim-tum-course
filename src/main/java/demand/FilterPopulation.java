package demand;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.population.PopulationUtils;

import java.util.ArrayList;
import java.util.List;

// This class filters and writes a population that has activities within Munich

public class FilterPopulation {

    private static final String NETWORK = "scenarios/munich/network/munich-bicycle.xml.gz";
    private static final String inputPopulation = "scenarios/munich/demand/population_typical.xml";
    private static final String outputPopulation = "scenarios/munich/demand/population_munich.xml";

    // Create an ArrayList of the people that have no activities in the study area
    private static List<Person> personRemove = new ArrayList<>();

    private static Population population = PopulationUtils.readPopulation(inputPopulation);

    public static void main(String[] args) {

        Network network = NetworkUtils.readNetwork(NETWORK);

        Coord lowestCoord = findLowestCoord(network);
        Coord highestCoord = findHighestCoord(network);

        for (Person person : population.getPersons().values()) {

            boolean outsideMunich = false;

            double activityCoordX;
            double activityCoordY;

            List<PlanElement> planElements = person.getSelectedPlan().getPlanElements();

            for (PlanElement planElement : planElements) {

                if (planElement instanceof Activity) {

                    System.out.println(((Activity) planElement).getCoord().getX());

                    activityCoordX = ((Activity) planElement).getCoord().getX();
                    activityCoordY = ((Activity) planElement).getCoord().getY();

                    System.out.println("Coordinate X: " + activityCoordX + " Coordinate Y: " + activityCoordY);


                    if ((activityCoordX > highestCoord.getX()) || (activityCoordX < lowestCoord.getX()) || (activityCoordY > highestCoord.getY()) || (activityCoordY < lowestCoord.getY())) {

                        outsideMunich = true;
                        System.out.println("Activities OUTSIDE Munich");
                    } else  {
                        System.out.println("Activities INSIDE Munich");
                    }


                }

            }

            if (outsideMunich  || person.getSelectedPlan().getPlanElements().isEmpty()) {

                personRemove.add(person);
            }

            // If there is only one activity the person is not mobile
            if (person.getSelectedPlan().getPlanElements().size() == 1) {

                personRemove.add(person);
            }

        }

        removeNonMunich();

        writeFinalPopulation();

    }

    public static void removeNonMunich() {

        // Remove agents with no activities in Munich
        for (Person person : personRemove) {

            System.out.println("Person to be removed: " + person.getId());
            population.removePerson(person.getId());
        }

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

    public static void writeFinalPopulation() {

        // Write final population to xml file
        PopulationWriter writer = new PopulationWriter(population);
        writer.write(outputPopulation);

    }
}
