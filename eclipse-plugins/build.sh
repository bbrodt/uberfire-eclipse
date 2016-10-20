#!/bin/sh
# The runtime must be built before the eclipse plugin 
# See https://wiki.eclipse.org/Tycho/How_Tos/Dependency_on_pom-first_artifacts#It_is_not_possible_to_mix_pom-first_and_manifest-first_projects_in_the_same_reactor_build.
mvn -f org.uberfire.eclipse.runtime/pom.xml clean install
mvn clean verify
