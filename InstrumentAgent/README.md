# Java Agent Sample
Sample application that demonstrates a very basic java agent.

## NOTE: This is just for a discussion and should not be used in production. This is NOT thread-safe.

## Run
 Add these arguments to the java application you want the agent to run against.
 -javaagent:<path>/instrument-0.0.1-SNAPSHOT.jar -DblackList=<package_to_ignore> -DwhiteList=<package_to_analyze> -DimportPackage=<pacakge_to_use_as_lookup>
 
 Example:
 -javaagent:/adrepo/InstrumentAgent/target/instrument-0.0.1-SNAPSHOT-jar-with-dependencies.jar -DblackList=com/activedevsolutions/instrument -DwhiteList=com/activedevsolutions/test/ -DimportPackage=com.activedevsolutions