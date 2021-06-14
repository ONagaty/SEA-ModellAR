In order to generate java files during the compilation of a jet the 
Cessar project must contain a preference named "dump.source" into the
"eu.cessar.ct.jet.core" namespace.

In other words, a file named "eu.cessar.ct.jet.core.prefs" must exist
into the ".settings" folder of the project and it must contain a line
that read "dump.source=true".

Optional, the "dump.source.folder" can be set to the name of the folder
where the source files should be dumped. 