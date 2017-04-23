/*
 *     RandomCoords, Provding the best Bukkit Random Teleport Plugin
 *     Copyright (C) 2014  James Shopland
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.jolbol1.RandomCoordinates.managers;

import com.jolbol1.RandomCoordinates.RandomCoords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

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
            final File dataFolder = new File(RandomCoords.getPlugin().getDataFolder(), "Logs");
            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }

            final File saveTo = new File(RandomCoords.getPlugin().getDataFolder() + "/Logs", "log_" + + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute()+ ".txt");
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
