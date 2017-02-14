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

    public void logToFile(final String message)

    {
      if(!RandomCoords.getPlugin().config.getString("debug").equalsIgnoreCase("true")) {
          return;
      }
        try
        {
            final File dataFolder = RandomCoords.getPlugin().getDataFolder();
            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }

            final File saveTo = new File(RandomCoords.getPlugin().getDataFolder(), "log.txt");
            if (!saveTo.exists())
            {
                saveTo.createNewFile();
            }


            final FileWriter fw = new FileWriter(saveTo, true);

            final PrintWriter pw = new PrintWriter(fw);

            pw.println(message);

            pw.flush();

            pw.close();

        } catch (IOException e)
        {

            e.printStackTrace();

        }

    }



}
