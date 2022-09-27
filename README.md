# ConceptModels
BEAST2 package for analysing lexical linguistic data as multistate meaning/concept classes.

# Quick start in R
The functions in the [ConceptModels-Rtools](https://github.com/king-ben/ConceptModels-Rtools/blob/master/multistate_functions.R) package produce xml blocks which can be used to assemble an xml file for a multistate analysis.
The multistate_xml function will produce these xml blocks when applied to a nexus file in the working directory. The file should have the meaning partitions defined and have an ascertainment (all zero) site for every meaning.


```
library(devtools)
source_url("https://raw.githubusercontent.com/king-ben/ConceptModels-Rtools/master/multistate_functions.R?token=GHSAT0AAAAAABZG3DCAIZNLSWL5W4CLPAZQYZS6RDA")

xml <- multistate_xml("name-of-file.nex")

saveall_multistate(xml)

```


Building package from source
----------------------------
Ensure Apache Ant is installed.
To build this package from source, download the source code and unzip the folder. In terminal, navigate to the archive and type "ant"
This will create a "dist" folder inside the archive, within which is a zip file containing the package.
Copy this zip file to the BEAST addon directory in your computer and unzip it. To find out where the addon directory is, open beauti, File - manage packages, click on the question mark in the bottom right corner. You may also need to clear the beast class path. This is possible in the file menu of beauti.You may need to build the dependencies (beast2, BeastLabs and SA) from source first.

Archive Contents
----------------

* `README.md` : this file
* `build.xml` : Ant build script
* `/examples` : Example beast2 xml files
* `/src` : source files. See below for details.
* `version.xml` : BEAST package version file.

The Java packages in the ConceptModels BEAST2 package are:

### `beast.core.util`
* 'UnobservedFrequencyLogger' - Logs the frequency of the unobserved state (i.e. logs only the last dimension of the frequency parameter).

### `beast.evolution.operators`
* 'MutationRateReweight' - Operator to change the mutation rate spread parameter and changes the mutation rate of each concept according to this parameter and the number of states. 
* 'UnobservedFrequencyExchanger' - In a single concept, changes the frequency of the unobserved state and changes the observed frequencies so that all frequencies sum to one.
* 'UnobservedFrequenciesLinkedExchange' - Changes all unobserved frequencies which are distributed as a concave function according to the number of states. Probably does not work.
