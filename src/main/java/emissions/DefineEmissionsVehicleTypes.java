package emissions;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.vehicles.MatsimVehicleWriter;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;
import org.matsim.vehicles.Vehicles;

public class DefineEmissionsVehicleTypes {

    public static void main(String[] args) {

        // Create vehicles data container
        Vehicles vehicles = VehicleUtils.createVehiclesContainer();

        // Insert network modes (car, bike) into simulation
        VehicleType car = VehicleUtils.createVehicleType(Id.create("car", VehicleType.class));
        car.setNetworkMode(TransportMode.car);
        car.setPcuEquivalents(1);

        // Add vehicle description for emissions
        car.setDescription("BEGIN_EMISSIONSPASSENGER_CAR;average;average;averageEND_EMISSIONS");

        vehicles.addVehicleType(car);

        new MatsimVehicleWriter(vehicles).writeFile("scenarios/munich/emissions/vehicles.xml");

    }
}
