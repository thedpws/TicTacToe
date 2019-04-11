# Tic Tac Toe in a GUI

## About this project
* Built using IntelliJ IDEA
* Using Java 8

## How to run this project
1. Using IntelliJ IDEA... Create a new JavaFX Project.
2. Copy-paste these files into the project.
3. Run course.oop.application.Main

## Features implemented
* Basic TicTacToe functionality (selecting tiles, timeout, player creation/selection)
* Player persistence. Players are saved along with their records.
* Customized markers (select an emoji)
* Winning/Losing animations (it spins whether you lose or win)

## \"Features\" implemented
* Timeout of 1 second causes turns to be skipped
* Failure to create a second player automatically pits Player1 against a Bot
* Usernames of empty string "" are valid and persisted.

## Architecture (MVC)
The project uses a state machine pattern, switching between an initial MainMenu state to a GameSetup state, etc. In the CLI, each state had a set of supported commands. Instead of the user inputting commands via stdin, the GUI inputs commands via View.execute(String command). The translation from the CLI to the GUI was very natural. Each state has its own TTTView and switching between states causes a switching between TTTViews. This project is designed as per the MVC standard. The Model communicates with the View only through the Controller.
You can see the "commands" executing in the CLI as buttons are pressed on the GUI.

## Cannot compile?
* I was unable to export this project as a jar. [Here] (https://youtu.be/WA_84b9XkMI) is a youtube link to watch ME play tic tac toe. (https://youtu.be/WA_84b9XkMI).
