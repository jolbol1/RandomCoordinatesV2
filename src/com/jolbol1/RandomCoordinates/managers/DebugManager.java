package com.jolbol1.RandomCoordinates.managers;

import com.jolbol1.RandomCoordinates.RandomCoords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by James on 03/02/2017.
 */
public class DebugManager {

    public void logToFile(String message)

    {
      if(!RandomCoords.getPlugin().config.getString("debug").equalsIgnoreCase("true")) {
          return;
      }
        try
        {
            File dataFolder = RandomCoords.getPlugin().getDataFolder();
            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }

            File saveTo = new File(RandomCoords.getPlugin().getDataFolder(), "log.txt");
            if (!saveTo.exists())
            {
                saveTo.createNewFile();
            }


            FileWriter fw = new FileWriter(saveTo, true);

            PrintWriter pw = new PrintWriter(fw);

            pw.println(message);

            pw.flush();

            pw.close();

        } catch (IOException e)
        {

            e.printStackTrace();

        }

    }



}
