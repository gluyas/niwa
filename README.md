
1) Import .jar into Eclipse project.
2) Export project as runnable .jar with the main method from Launcher.java

Running the game:

	- Make sure the runnable .jar and resource folder are in the same folder.

1) Run the server by typing the following on the command line:
	
	java -jar <runnable jar> -server <number of connections
	
	e.g. java -jar niwa.jar -server 3

2) Connect to the server by typing the following on the command line:

	java -jar <runnable jar> -connect <server host>

	e.g. java -jar niwa.jar -connect daawat.ecs.vuw.ac.nz

Clients are immediately spawned in the game after connecting.



CONTROLS:

wasd - Move
f    - Interact
q    - Rotate CounterClockWise
e    - Rotate ClockWise
i    - Inspect
g    - Drop

