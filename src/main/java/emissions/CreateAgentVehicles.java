package emissions;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.*;

public class CreateAgentVehicles {

    public static void main(String[] args) {

        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        new PopulationReader(scenario).readFile("./scenarios/munich/demand/population_munich5pct.xml");

        Vehicles vehicles = VehicleUtils.createVehiclesContainer();
        VehicleType type = VehicleUtils.createVehicleType(Id.create("car", VehicleType.class));

        type.setNetworkMode("car");
        type.setDescription("BEGIN_EMISSIONSPASSENGER_CAR;average;average;averageEND_EMISSIONS");

        vehicles.addVehicleType(type);

        for (Person person: scenario.getPopulation().getPersons().values()) {
            Vehicle vehicle = VehicleUtils.createVehicle(Id.createVehicleId(person.getId()), type);
            vehicles.addVehicle(vehicle);
        }

        new MatsimVehicleWriter(vehicles).writeFile("./scenarios/munich/emissions/cars.xml");
    }


}
