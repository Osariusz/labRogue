# LabRogue

LabRogue is a rogue-like game inspired by SCP and the original Rogue game. It's set in an abandoned research facility. The game is developed in Java using Lombok and JUnit for unit tests and is built with Maven.

## Game Mechanics
- Randomly generated facility
- Turn-based gameplay
- Monsters inspired by SCP and more
- Items found in rooms (some can be SCP objects that can permanently increase your stats)
- Upgraders that can transform items into other items (or destroy them)
- Permadeath
- ASCII graphics
- Command based controls, with ``help`` and ``rules`` commands

## Story
You are an escapee in a research facility from which all personnel have disappeared. Your task is to escape from the facility, but it won't be easy due to the rampaging research entities and traps. You will face several separate levels with different enemies and only the last level contains the exit.

## Technical Aspects
The project is developed in Java with the use of Lombok for reducing boilerplate code. The project is built using Maven, and the pom.xml file includes dependencies for JUnit Jupiter Engine and JUnit Platform Runner for testing purposes.

## Run
To run the application you will first have to create artifact and then, use the following command:
```
java -jar out/artifacts/LabRogue_jar/LabRogue.jar
```

![Game presentation](https://raw.githubusercontent.com/Osariusz/labRogue/main/image.png)
