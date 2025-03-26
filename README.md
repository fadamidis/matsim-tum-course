# MATSim Course at TUM

The semester project at TUM investigates if better cycling conditions can attract travellers from car. This repository contains Java classes to prepare and run two MATSim simulations:
* the `base` scenario simulates the status quo
* the `measure` scenario includes enhancements in the cycling infrastructure

The project starts by creating a Munich network with car and bicycle network modes. Then, a synthetic population is used to generate the 5 pct. plans of Munich residents.

### Network modifications 

In principle, two major network modifications take place:
* Increasing the speed limit of bicycles on links that allow bicycles
* Decreasing the speed limit of cars on all links

As a result, the speed limit is set at 30 km/h for all modes. Because public transport and walking are simulated as teleportation modes, the proposed changes do not affect them.

### Visualisation

![screenshot](images/simulation_screenshot.png)

### Emissions

The project uses the `emissions` contrib and a simplified emissions mapping from HBEFA for cars on urban roads and motorways. Emissions from cars are estimated in the `base` and `measure` scenarios and compared.

### Results

The simulation results did not show any notable change in the modal split in favour of bicycle traffic. The total emissions remained largely unchanged.

### Files

The repository does not include any data that can be used to produce the results. The repository includes the required Java classes to prepare and run MATSim simulations.

### Structure

A recommended directory structure is as follows:
* `src` for sources
* `original-input-data` for original input data (currently empty)
* `scenarios` for MATSim scenarios, i.e. MATSim input and output data. Currently includes only the configurations of the `base` and `measure` scenarios.
  
### Import into IntelliJ

`File -> New -> Project from Version Control` paste the repository url and hit 'clone'. IntelliJ usually figures out
that the project is a maven project. If not: `Right click on pom.xml -> import as maven project`.

### Licenses

The **MATSim program code** in this repository is distributed under the terms of the [GNU General Public License as published by the Free Software Foundation (version 2)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html). The MATSim program code are files that reside in the `src` directory hierarchy and typically end with `*.java`.

The **MATSim input files, output files, analysis data and visualizations** are licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/80x15.png" /></a><br /> MATSim input files are those that are used as input to run MATSim. They often, but not always, have a header pointing to matsim.org. They typically reside in the `scenarios` directory hierarchy. MATSim output files, analysis data, and visualizations are files generated by MATSim runs, or by postprocessing.  They typically reside in a directory hierarchy starting with `output`.
