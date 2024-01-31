package org.osariusz;

import org.osariusz.Actors.Player;
import org.osariusz.Game.Game;
import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;
import org.osariusz.Utils.Logging;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import static org.osariusz.Utils.FightScenarios.*;

public class Main {
    public static void main(String[] args) {
        Logging.logger.setLevel(Level.SEVERE);
        Game game = new Game();
        game.gameLoop();
    }
}
