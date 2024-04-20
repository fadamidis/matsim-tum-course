package emissions;

import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigWriter;
import org.matsim.core.config.groups.NetworkConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.MatsimServices;

public final class CreateEmissionsConfig {

    private static final String averageFleetWarmEmissionFactorsFile = "./scenarios/munich/emissions/EFA_Hot_car_2020average.txt";
    private static final String averageFleetColdEmissionFactorsFile = "./scenarios/munich/emissions/EFA_ColdStart_car_2020average.txt";
    private static final String configFilePath = "./scenarios/munich/emissions/config_emissions.xml";
    //private static final String emissionEventOutputFile = "./scenarios/munich/emissions/output_emissions.xml";
    //private static final String eventsFile = "./scenarios/munich/output/output-basic-1pct/munich-basic-1pct.output_events.xml.gz";

    public static void main(String[] args) {

        Config config = new Config();
        config.addCoreModules();
        MatsimServices controller = new Controler(config);

        // Vehicles
        config.vehicles().setVehiclesFile("./scenarios/munich/emissions/vehicles.xml");

        // Network
        NetworkConfigGroup ncg = controller.getConfig().network();
        ncg.setInputFile("./scenarios/munich/network/munich-bicycle_hbefa.xml.gz");

        // Emissions
        EmissionsConfigGroup ecg = new EmissionsConfigGroup();
        controller.getConfig().addModule(ecg);

        // Define emissions input files
        ecg.setHbefaVehicleDescriptionSource(EmissionsConfigGroup.HbefaVehicleDescriptionSource.fromVehicleTypeDescription);
        ecg.setAverageColdEmissionFactorsFile(averageFleetColdEmissionFactorsFile);
        ecg.setAverageWarmEmissionFactorsFile(averageFleetWarmEmissionFactorsFile);
        ecg.setDetailedVsAverageLookupBehavior(EmissionsConfigGroup.DetailedVsAverageLookupBehavior.directlyTryAverageTable);
        ecg.setNonScenarioVehicles(EmissionsConfigGroup.NonScenarioVehicles.ignore);
        ecg.setWritingEmissionsEvents(true);

        // Write config
        ConfigWriter cw = new ConfigWriter(config);
        cw.write(configFilePath);
    }

}
