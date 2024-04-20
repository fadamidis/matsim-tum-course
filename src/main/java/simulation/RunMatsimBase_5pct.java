package simulation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.bicycle.BicycleConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

import java.util.Arrays;

public class RunMatsimBase_5pct {

    public static void main(String[] args) {

        Config config = ConfigUtils.createConfig();

        // Global config settings
        config.global().setCoordinateSystem("EPSG:31468");

        // Used to reduce network capacity and throughput based on population sample
        config.qsim().setFlowCapFactor(1.0);
        config.qsim().setStorageCapFactor(0.5);

        config.qsim().setPcuThresholdForFlowCapacityEasing(0.15);

        config.network().setInputFile("scenarios/munich/network/munich-bicycle.xml.gz");
        config.plans().setInputFile("scenarios/munich/demand/population_munich5pct.xml");

        config.qsim().setStartTime(0);
        config.qsim().setEndTime(30 * 60 * 60);
        config.qsim().setSnapshotPeriod(0);

        // Activate PassingQ because cars and bikes may use same links
        config.qsim().setLinkDynamics(QSimConfigGroup.LinkDynamics.PassingQ);
        config.qsim().setStuckTime(10.0);

        // Activity scoring
        config.planCalcScore().setLearningRate(1);
        config.planCalcScore().setLateArrival_utils_hr(-18.0);  // Not considered because there is no activity start time in the plans file
        config.planCalcScore().setEarlyDeparture_utils_hr(0.0);
        config.planCalcScore().setPerforming_utils_hr(5.0);
        config.planCalcScore().setMarginalUtlOfWaiting_utils_hr(0.0);
        config.planCalcScore().setMarginalUtilityOfMoney(1.0);

        // Mode choice parameter suggested to be 1.0
        config.planCalcScore().setBrainExpBeta(1.0);

        // Activity parameters
        PlanCalcScoreConfigGroup.ActivityParams home = new PlanCalcScoreConfigGroup.ActivityParams("HOME");
        home.setActivityType("HOME");
        home.setPriority(1);
        home.setTypicalDuration(6.8 * 60 * 60);
        config.planCalcScore().addActivityParams(home);

        PlanCalcScoreConfigGroup.ActivityParams work = new PlanCalcScoreConfigGroup.ActivityParams("WORK");
        work.setActivityType("WORK");
        work.setPriority(1);
        work.setTypicalDuration(7.1 * 60 * 60);
        work.setOpeningTime(7 * 60 * 60);
        work.setLatestStartTime(9 * 60 * 60);
        work.setEarliestEndTime(0);
        work.setClosingTime(18 * 60 * 60);
        config.planCalcScore().addActivityParams(work);

        PlanCalcScoreConfigGroup.ActivityParams education = new PlanCalcScoreConfigGroup.ActivityParams("EDUCATION");
        education.setActivityType("EDUCATION");
        education.setPriority(1);
        education.setTypicalDuration(6.8 * 60 * 60);
        education.setOpeningTime(7.5 * 60 * 60);
        education.setLatestStartTime(8.5 * 60 * 60);
        education.setEarliestEndTime(0);
        education.setClosingTime(15 * 60 * 60);
        config.planCalcScore().addActivityParams(education);

        PlanCalcScoreConfigGroup.ActivityParams accompany = new PlanCalcScoreConfigGroup.ActivityParams("ACCOMPANY");
        accompany.setActivityType("ACCOMPANY");
        accompany.setPriority(1);
        accompany.setTypicalDuration(2.8 * 60 * 60);
        config.planCalcScore().addActivityParams(accompany);

        PlanCalcScoreConfigGroup.ActivityParams recreation = new PlanCalcScoreConfigGroup.ActivityParams("RECREATION");
        recreation.setActivityType("RECREATION");
        recreation.setPriority(2);
        recreation.setTypicalDuration(4.5 * 60 * 60);
        config.planCalcScore().addActivityParams(recreation);

        PlanCalcScoreConfigGroup.ActivityParams shopping = new PlanCalcScoreConfigGroup.ActivityParams("SHOPPING");
        shopping.setActivityType("SHOPPING");
        shopping.setPriority(2);
        shopping.setTypicalDuration(3.3 * 60 * 60);
        config.planCalcScore().addActivityParams(shopping);

        PlanCalcScoreConfigGroup.ActivityParams subtour = new PlanCalcScoreConfigGroup.ActivityParams("SUBTOUR");
        subtour.setActivityType("SUBTOUR");
        subtour.setPriority(2);
        subtour.setTypicalDuration(0.5 * 60 * 60);
        config.planCalcScore().addActivityParams(subtour);

        PlanCalcScoreConfigGroup.ActivityParams other = new PlanCalcScoreConfigGroup.ActivityParams("OTHER");
        other.setActivityType("OTHER");
        other.setPriority(2);
        other.setTypicalDuration(3 * 60 * 60);
        config.planCalcScore().addActivityParams(other);

        // Adding teleported modes
        PlansCalcRouteConfigGroup.TeleportedModeParams pt = new PlansCalcRouteConfigGroup.TeleportedModeParams("pt");
        pt.setTeleportedModeSpeed(18.0/3.6);
        pt.setBeelineDistanceFactor(1.5);
        config.plansCalcRoute().addTeleportedModeParams(pt);

        PlansCalcRouteConfigGroup.TeleportedModeParams walk = new PlansCalcRouteConfigGroup.TeleportedModeParams("walk");
        walk.setTeleportedModeSpeed(4.3/3.6);
        walk.setBeelineDistanceFactor(1.3);
        config.plansCalcRoute().addTeleportedModeParams(walk);

        PlansCalcRouteConfigGroup.TeleportedModeParams passenger = new PlansCalcRouteConfigGroup.TeleportedModeParams("passenger");
        passenger.setTeleportedModeFreespeedFactor(1.0);
        config.plansCalcRoute().addTeleportedModeParams(passenger);

        // Set travel scoring parameters
        PlanCalcScoreConfigGroup.ModeParams modeParamsPt = config.planCalcScore().getOrCreateModeParams("pt");
        modeParamsPt.setConstant(-3.0);
        modeParamsPt.setMarginalUtilityOfTraveling(-2.5);
        modeParamsPt.setMarginalUtilityOfDistance(-2.2);
        modeParamsPt.setMonetaryDistanceRate(-0.0002);
        modeParamsPt.setDailyMonetaryConstant(-2.3);
        config.planCalcScore().addModeParams(modeParamsPt);

        PlanCalcScoreConfigGroup.ModeParams modeParamsWalk = config.planCalcScore().getOrCreateModeParams("walk");
        modeParamsWalk.setConstant(-2.2);
        modeParamsWalk.setMarginalUtilityOfTraveling(-1.5);
        modeParamsWalk.setMarginalUtilityOfDistance(-2.5);
        config.planCalcScore().addModeParams(modeParamsWalk);

        PlanCalcScoreConfigGroup.ModeParams modeParamsBike = config.planCalcScore().getOrCreateModeParams("bike");
        modeParamsBike.setConstant(-1.5);
        modeParamsBike.setMarginalUtilityOfTraveling(-1.0);
        modeParamsBike.setMarginalUtilityOfDistance(-2.5);
        config.planCalcScore().addModeParams(modeParamsBike);

        // Scoring for passenger not relevant because not routed and not included in mode choice
        PlanCalcScoreConfigGroup.ModeParams modeParamsPassenger = config.planCalcScore().getOrCreateModeParams("passenger");
        config.planCalcScore().addModeParams(modeParamsPassenger);

        PlanCalcScoreConfigGroup.ModeParams modeParamsCar = config.planCalcScore().getOrCreateModeParams("car");
        modeParamsCar.setConstant(-2.0);
        modeParamsCar.setMarginalUtilityOfTraveling(-2.5);
        modeParamsCar.setMarginalUtilityOfDistance(-2.5);
        modeParamsCar.setMonetaryDistanceRate(-0.0002);
        modeParamsBike.setDailyMonetaryConstant(-5.3);
        config.planCalcScore().addModeParams(modeParamsCar);

        // Add network modes to router calculation
        config.plansCalcRoute().setNetworkModes(Arrays.asList(TransportMode.car, TransportMode.bike));

        config.strategy().setMaxAgentPlanMemorySize(3);
        //config.strategy().setFractionOfIterationsToDisableInnovation(0.8);

        StrategyConfigGroup.StrategySettings reRoute = new StrategyConfigGroup.StrategySettings();
        reRoute.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.ReRoute);
        reRoute.setWeight(0.2);
        config.strategy().addStrategySettings(reRoute);

        StrategyConfigGroup.StrategySettings chooseBest = new StrategyConfigGroup.StrategySettings();
        chooseBest.setStrategyName(DefaultPlanStrategiesModule.DefaultSelector.BestScore);
        chooseBest.setWeight(0.1);
        config.strategy().addStrategySettings(chooseBest);

        StrategyConfigGroup.StrategySettings selectExpBeta = new StrategyConfigGroup.StrategySettings();
        selectExpBeta.setStrategyName(DefaultPlanStrategiesModule.DefaultSelector.SelectExpBeta);
        selectExpBeta.setWeight(0.3);
        config.strategy().addStrategySettings(selectExpBeta);

        StrategyConfigGroup.StrategySettings subTourModeChoice = new StrategyConfigGroup.StrategySettings();
        subTourModeChoice.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.SubtourModeChoice);
        subTourModeChoice.setWeight(0.4);
        config.strategy().addStrategySettings(subTourModeChoice);

//        StrategyConfigGroup.StrategySettings changeSingleTripMode = new StrategyConfigGroup.StrategySettings();
//        changeSingleTripMode.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.ChangeSingleTripMode);
//        changeSingleTripMode.setWeight(0.4);
//        config.strategy().addStrategySettings(changeSingleTripMode);

        config.changeMode().setModes(new String[]{"car", "walk", "pt", "bike"});
        config.changeMode().setIgnoreCarAvailability(true);

        // Write link stats every 10 iterations
        config.linkStats().setWriteLinkStatsInterval(10);

        config.controler().setOutputDirectory("scenarios/munich/output/output-base-5pct");
        config.controler().setFirstIteration(0);
        config.controler().setLastIteration(200);
        config.controler().setRunId("munich-base-5pct");
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);

        // Create scenario object
        Scenario scenario = ScenarioUtils.loadScenario(config);

        // Add network modes (car, bike) to qsim
        scenario.getConfig().qsim().setMainModes(Arrays.asList(TransportMode.car, TransportMode.bike));

        // Insert network modes (car, bike) into simulation
        VehicleType car = VehicleUtils.createVehicleType(Id.create("car", VehicleType.class));
        car.setMaximumVelocity(140.0/3.6);
        car.setPcuEquivalents(1);
        scenario.getVehicles().addVehicleType(car);

        VehicleType bike = VehicleUtils.createVehicleType(Id.create("bike", VehicleType.class));
        bike.setMaximumVelocity(15.0/3.6);
        bike.setPcuEquivalents(0.25);
        bike.setLength(1.8);
        bike.setWidth(0.8);
        scenario.getVehicles().addVehicleType(bike);

        // Access egress option [otherwise agents using routed modes cannot reach their destination (arrive very close but not exactly)]
        config.plansCalcRoute().setAccessEgressType(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink);

        ConfigUtils.writeConfig(scenario.getConfig(), "scenarios/munich/config/munich-base-5pct_config.xml");

        // Create controller object
        Controler controler = new Controler(scenario);

        controler.run();
    }
}
