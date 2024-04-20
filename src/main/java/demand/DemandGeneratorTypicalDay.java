package demand;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// This class created the plans of all agents in the ABIT population based on a typical day

public class DemandGeneratorTypicalDay {

    // The variables are static because they are valid throughout the class

    private static final String legs = "scenarios/abitDemand/legs.csv";
    private static final Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());


    public static void main(String[] args) throws IOException {

        // Using the legs file
        readDemandFromCsvAndCreatePopulation(legs);

        // Write final population
        writePopulation();

    }

    private static void readDemandFromCsvAndCreatePopulation(String filePath) throws IOException {

        // Use BufferedReader to read csv file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip first line (header)
        reader.readLine();

        int last_person = -1;

        // Use this counter to consider only the first 3,000,000 rows due to memory issues
        int i = 0;

        // Continue with rest of lines
        while ((line = reader.readLine()) != null) {

            i++;
            System.out.println(i);

            String[] values = line.split(",");

            int person_id = Integer.valueOf(values[0]);
            String purpose = values[1].replace("\"", "");

            // Starting time of the leg is the end_time of the activity
            int end_time = Integer.valueOf(values[2]);

            double coord_x = Double.valueOf(values[3]);
            double coord_y = Double.valueOf(values[4]);

            String mode = values[11].replace("\"", "");

            if (person_id != last_person) {
                Id<Person> personId = Id.createPersonId( "agent_" + person_id);
                Person person = scenario.getPopulation().getFactory().createPerson(personId);
                scenario.getPopulation().addPerson(person);

                Plan plan = scenario.getPopulation().getFactory().createPlan();
                person.addPlan(plan);

                last_person = person_id;
            }

            Id<Person> personId = Id.createPersonId( "agent_" + person_id);
            Plan plan = scenario.getPopulation().getPersons().get(personId).getPlans().get(0);

            // Add activities for day 2 (assumed Tuesday) only
            if (Integer.valueOf(values[7]) >= 1440 && Integer.valueOf(values[7]) < 2880) {
                Activity activity = scenario.getPopulation().getFactory().createActivityFromCoord(purpose, new Coord(coord_x, coord_y));

                // end_time must also belong in the same day
                if (end_time >= 1440 && end_time < 2880) {
                    // End_time is in minutes
                    activity.setEndTime(end_time * 60 - 24 * 60 * 60);
                    plan.addActivity(activity);

                    Leg leg = scenario.getPopulation().getFactory().createLeg(mode);
                    plan.addLeg(leg);
                }

            }

        }

        // Finalise population by adding Home activity at the end of day 2 if necessary
        for (Person person : scenario.getPopulation().getPersons().values()) {

            // Find home coordinates
            Plan plan = person.getSelectedPlan();

            if (!plan.getPlanElements().isEmpty()) {

                Activity lastActivity = (Activity) plan.getPlanElements().get(plan.getPlanElements().size() - 2);

                int size = plan.getPlanElements().size();

                // If last activity is not Home
                if (!lastActivity.getType().contains("HOME")) {

                    // Search for the Home location
                    double homeX = -1;
                    double homeY = -1;

                    for (int counter = 0; counter < size; counter++) {

                        PlanElement planElement = plan.getPlanElements().get(counter);

                        // Exclude legs from the search
                        if (planElement instanceof Activity) {

                            // Find Home activity
                            if (((Activity) planElement).getType().contains("HOME")) {

                                homeX = ((Activity) planElement).getCoord().getX();
                                homeY = ((Activity) planElement).getCoord().getY();

                            }
                        }


                    }

                    // Add Home activity
                    //Activity homeActivity = (Activity) plan.getPlanElements().get(0);
                    Activity activity = scenario.getPopulation().getFactory().createActivityFromCoord("HOME", new Coord(homeX, homeY));
                    plan.addActivity(activity);
                } else {

                    // Remove the leg after Home
                    plan.getPlanElements().remove(plan.getPlanElements().size() - 1);

                }


            }

        }


    }


    public static void writePopulation() {

        // Write final population to xml file
        PopulationWriter writer = new PopulationWriter(scenario.getPopulation());
        writer.write("scenarios/munich/demand/population_typical.xml");

    }
}