package org.osariusz;

import org.osariusz.Actors.Player;
import org.osariusz.Game.Game;
import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.osariusz.Utils.FightScenarios.*;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.gameLoop();
    }
}
