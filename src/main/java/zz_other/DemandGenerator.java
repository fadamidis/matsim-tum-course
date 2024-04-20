package zz_other;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DemandGenerator {

    private static final String ACTIVITIES_FILE = "scenarios/abitDemand/activities.csv";
    private static final Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

    // Assume that the mode of all legs is CAR (activities.csv does not include mode info)
    private static final String MODE = "CAR";

    public static void main(String[] args) throws IOException {

        // Using the activities file
        readDemandFromCsvAndCreatePlans(ACTIVITIES_FILE);

        // Write plans
        writePlans();

    }

    private static void readDemandFromCsvAndCreatePlans(String filePath) throws IOException {

        // Use BufferedReader to read csv file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip first line
        reader.readLine();

        int last_person = -1;

        // Use this counter to consider only the first 3,000,000 rows due to memory issues
        int i = 0;

        // Continue with rest of lines
        while ((line = reader.readLine()) != null) {

            i++;
            if (i > 3000000) {
                break;
            }

            String[] values = line.split(",");

            int person_id = Integer.valueOf(values[0]);
            int start_time = Integer.valueOf(values[3]);
            int end_time = Integer.valueOf(values[4]);
            String purpose = values[5].replace("\"", "");
            double coord_x = Double.valueOf(values[7]);
            double coord_y = Double.valueOf(values[8]);

            // Create person and plan
            if (person_id != last_person) {

                // Remove last leg from the previous person
                if (last_person != -1) {

                    Id<Person> lastPersonId = Id.createPersonId("agent_" + last_person);
                    Plan lastPersonPlan = scenario.getPopulation().getPersons().get(lastPersonId).getPlans().get(0);

                    if (!lastPersonPlan.getPlanElements().isEmpty()) {

                        lastPersonPlan.getPlanElements().remove(lastPersonPlan.getPlanElements().size()-1);
                    }

                }

                // Create new person
                Id<Person> personId = Id.createPersonId("agent_" + person_id);
                Person person = scenario.getPopulation().getFactory().createPerson(personId);
                scenario.getPopulation().addPerson(person);

                Plan plan = scenario.getPopulation().getFactory().createPlan();
                person.addPlan(plan);

                last_person = person_id;
            }

            // Add activities for day 2 (assumed Tuesday) only
            if (Integer.valueOf(values[2]) == 2) {

                Id<Person> personId = Id.createPersonId("agent_" + person_id);
                Plan plan = scenario.getPopulation().getPersons().get(personId).getPlans().get(0);

                Activity activity = scenario.getPopulation().getFactory().createActivityFromCoord(purpose, new Coord(coord_x, coord_y));

                // end_time is in minutes
                activity.setEndTime(end_time * 60 - 24 * 60 * 60);
                // start_time is optional (in minutes)
                activity.setStartTime(start_time * 60 - 24 * 60 * 60);

                // Add activity to plan
                plan.addActivity(activity);

                Leg leg = scenario.getPopulation().getFactory().createLeg(MODE);
                plan.addLeg(leg);

            }

        }

    }

    public static void writePlans() {

        // Write final population to xml file
        PopulationWriter writer = new PopulationWriter(scenario.getPopulation());
        writer.write("/Users/adfil/Desktop/Applied Transport Modeling with MATSim/mat-sim-ws-2324-filippos/scenarios/munich/population.xml");

    }

}
