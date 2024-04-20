/*
package org.matsim.pt2matsim.run;

import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.ConfigWriter;
import org.matsim.core.utils.collections.CollectionUtils;
import org.matsim.pt2matsim.config.PublicTransitMappingConfigGroup;
import org.matsim.pt2matsim.run.CreateDefaultPTMapperConfig;
import org.matsim.pt2matsim.run.PublicTransitMapper;

public class MapSchedule2Network {

    public static void main(String[] args) {
        // Create an empty mapping config:
        CreateDefaultPTMapperConfig.main(new String[]{ "munich/input/config.xml"});
        // Open the mapping config and set the parameters to the required values
        // (usually done manually by opening the config with a simple editor)
        Config config = ConfigUtils.loadConfig(
                "munich/input/config.xml",
                PublicTransitMappingConfigGroup.createDefaultConfig());
        PublicTransitMappingConfigGroup ptmConfig = ConfigUtils.addOrGetModule(config, PublicTransitMappingConfigGroup.class);

        ptmConfig.setInputNetworkFile("munich/output/munichMultiModal.xml.gz");
        ptmConfig.setOutputNetworkFile("munich/output/munichMultiModalMapped.xml.gz");
        ptmConfig.setOutputStreetNetworkFile("munich/output/munich_streetnetwork.xml.gz");
        ptmConfig.setInputScheduleFile("munich/output/munichSchedule.xml");
        ptmConfig.setOutputScheduleFile("munich/output/munichScheduleMapped.xml");
        ptmConfig.setScheduleFreespeedModes(CollectionUtils.stringToSet("rail, light_rail"));
        // Save the mapping config
        // (usually done manually)
        new ConfigWriter(config).write("munich/output/config.xml");
        PublicTransitMapper.run("munich/output/config.xml");
    }
}
*/