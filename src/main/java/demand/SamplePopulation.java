package demand;

import org.matsim.api.core.v01.population.*;
import org.matsim.core.population.PopulationUtils;

import java.util.ArrayList;
import java.util.List;

// This class creates a random 5pct sample of the Munich population

public class SamplePopulation {

    private static final String inputPopulation = "scenarios/munich/demand/population_munich.xml";
    private static final String outputPopulation = "scenarios/munich/demand/population_munich5pct.xml";

    // Create an ArrayList of the people that have no activities in the study area
    private static List<Person> personRemove = new ArrayList<>();

    private static Population population = PopulationUtils.readPopulation(inputPopulation);

    public static void main(String[] args) {


        for (Person person : population.getPersons().values()) {

            if (Math.random() <= 0.95) {
                personRemove.add(person);
            }
        }


        removeRedundant();

        writeFinalPopulation();

    }

    public static void removeRedundant() {

        // Remove 95pct of the agents in Munich
        for (Person person : personRemove) {

            System.out.println("Person to be removed: " + person.getId());
            population.removePerson(person.getId());
        }

    }

    public static void writeFinalPopulation() {

        // Write final population to xml file
        PopulationWriter writer = new PopulationWriter(population);
        writer.write(outputPopulation);

    }
}

