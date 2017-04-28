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

package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Util.ArgMode;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 22/03/2017.
 */
public class CommonMethods {

    private MessageManager messages = new MessageManager();
    public int key = 574272099;
    private Coordinates coordinates = new Coordinates();




    public Map processCommonArguments(String[] arg) {
        return processCommonArguments(arg, false);
    }


    public Map processCommonArguments(String[] arg, boolean playerNeeded) {
        Map modesUsed = new HashMap<ArgMode, Object>();
        ArgMode argMode = ArgMode.NONE;
        int argNumber = 0;
        for(String s : arg) {

            if (Bukkit.getPlayer(s) != null && playerNeeded == true && !modesUsed.containsKey(ArgMode.PLAYER)) {
                modesUsed.put(ArgMode.PLAYER, Bukkit.getPlayer(s));
            } else if (Bukkit.getWorld(s) != null && !modesUsed.containsKey(ArgMode.WORLD)) {
                modesUsed.put(ArgMode.WORLD, s);
            } else if (s.equalsIgnoreCase("max")) {
                argMode = ArgMode.MAX;
                modesUsed.put(ArgMode.MAX, null);
            } else if (s.equalsIgnoreCase("min")) {
                argMode = ArgMode.MIN;
                modesUsed.put(ArgMode.MIN, null);

            } else if (s.contains("min:") && canParseInteger(s.replace("min:", ""))) {
                modesUsed.put(ArgMode.MIN, Integer.parseInt(s.replace("min:", "")));
            } else if (s.contains("max:") && canParseInteger(s.replace("max:", ""))) {
                modesUsed.put(ArgMode.MAX, Integer.parseInt(s.replace("max:", "")));
            } else if (s.contains("to:") && Bukkit.getWorld(s.replace("to:", "")) != null) {
                modesUsed.put(ArgMode.WORLD, s.replace("to:", ""));
            } else if (canParseInteger(s)) {

                if (argMode == ArgMode.MAX) {
                    modesUsed.put(ArgMode.MAX, Integer.parseInt(s));
                } else if (argMode == ArgMode.MIN) {
                    modesUsed.put(ArgMode.MIN, Integer.parseInt(s));
                } else if (!modesUsed.containsKey(ArgMode.MAX)) {
                    modesUsed.put(ArgMode.MAX, Integer.parseInt(s));
                } else if (!modesUsed.containsKey(ArgMode.MIN)) {
                    modesUsed.put(ArgMode.MIN, Integer.parseInt(s));
                } else if (Bukkit.getWorld(s) != null && !modesUsed.containsKey(ArgMode.WORLD)) {
                    modesUsed.put(ArgMode.WORLD, s);
                } else {
                    modesUsed.put(ArgMode.INCORRECT, s);
                }


            } else {
                if (argNumber != 0) {
                    modesUsed.put(ArgMode.WORLDNOTEXIST, s);

                }


            }
            argNumber++;
        }
        return modesUsed;
    }

    public Map processCommonArguments(String[] arg, boolean playerNeeded, boolean portals) {
        Map modesUsed = new HashMap<ArgMode, Object>();
        ArgMode argMode = ArgMode.NONE;
        int argNumber = 0;
        for(String s : arg) {

            if(Bukkit.getPlayer(s) != null && playerNeeded == true && !modesUsed.containsKey(ArgMode.PLAYER) ) {
                modesUsed.put(ArgMode.PLAYER, Bukkit.getPlayer(s));

            }


            else if(s.equalsIgnoreCase("create") && !modesUsed.containsKey(ArgMode.CREATE) && portals == true) {
                modesUsed.put(ArgMode.CREATE, null);

            }

            else if(s.equalsIgnoreCase("list") && !modesUsed.containsKey(ArgMode.LIST) && portals == true) {
                modesUsed.put(ArgMode.LIST, null);

            } else if((s.equalsIgnoreCase("delete") || s.equalsIgnoreCase("remove")) && !modesUsed.containsKey(ArgMode.DELETE) && portals == true) {
                modesUsed.put(ArgMode.DELETE, null);

            } else if(s.contains("name:") && portals == true) {
                modesUsed.put(ArgMode.PORTAL, s.replace("name:", ""));
            }





            else if(Bukkit.getWorld(s) != null && !modesUsed.containsKey(ArgMode.WORLD)) {
                modesUsed.put(ArgMode.WORLD, s);

            }

            else if(s.equalsIgnoreCase("max")) {
                argMode = ArgMode.MAX;

            }

            else if(s.equalsIgnoreCase("min")) {
                argMode = ArgMode.MIN;

            }

            else if(s.contains("min:") && canParseInteger(s.replace("min:", ""))) {
                modesUsed.put(ArgMode.MIN, Integer.parseInt(s.replace("min:", "")));

            }

            else if(s.contains("max:") && canParseInteger(s.replace("max:", ""))) {
                modesUsed.put(ArgMode.MAX, Integer.parseInt(s.replace("max:", "")));

            }

            else if(s.contains("to:") && Bukkit.getWorld(s.replace("to:", "")) != null) {
                modesUsed.put(ArgMode.WORLD, s.replace("to:", ""));

            }


            else if(canParseInteger(s)) {

                if(argMode == ArgMode.MAX) {
                    modesUsed.put(ArgMode.MAX, Integer.parseInt(s));

                }

                else if(argMode == ArgMode.MIN ) {
                    modesUsed.put(ArgMode.MIN, Integer.parseInt(s));

                }

                else if(!modesUsed.containsKey(ArgMode.MAX)) {
                    modesUsed.put(ArgMode.MAX, Integer.parseInt(s));

                }

                else if(!modesUsed.containsKey(ArgMode.MIN)) {
                    modesUsed.put(ArgMode.MIN, Integer.parseInt(s));

                }

                else if(Bukkit.getWorld(s) != null && !modesUsed.containsKey(ArgMode.WORLD)) {
                    modesUsed.put(ArgMode.WORLD, s);

                } else {
                    modesUsed.put(ArgMode.INCORRECT, s);

                }


            } else {
                if(argNumber != 0) {
                    if(!modesUsed.containsKey(ArgMode.PORTAL)) {

                        modesUsed.put(ArgMode.PORTAL, s);
                    } else {
                        modesUsed.put(ArgMode.INCORRECT, s);

                    }
                }
            }


            argNumber++;
        }
        return modesUsed;

    }



    public boolean canParseInteger(String maximum) {
        try {
            Integer.parseInt(maximum);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public boolean minToLarge(CommandSender sender, int min, int max, World w) {
       if(min == key) {
           min = coordinates.getMin(w);
       }
       if(max == key) {
           max = coordinates.getMax(w);
       }
        if(min > max) {
            messages.minTooLarge(sender);
            return false;
        }
        return true;

    }


    public boolean minToLarge(CommandSender sender, int min, int max) {
        if(min > max) {
            messages.minTooLarge(sender);
            return false;
        }
        return true;

    }


    /**
     * Check to see if the world provided is on the config banned list?
     * @param world - World to check
     * @return True if no banned, False is banned.
     */
    public boolean isWorldBanned(World world) {
        return !RandomCoords.getPlugin().config.getStringList("BannedWorlds").contains(world.getName());
    }

    /**
     * Does the world exist?
     * @param sender - Not used, Was previously to send message. - NEEDSREMOVING
     * @param worldName - The worldName we are checking
     * @return True if world exists, False if not.
     */
    public boolean doesWorldExist(CommandSender sender, String worldName) {

        World w = Bukkit.getWorld(worldName);
        return w != null;


    }

    public Collection<Player> allPlayers(CommandSender sender) {
        Collection<Player> players = new ArrayList<>();
        for(Player p: Bukkit.getOnlinePlayers()) {
            players.add(p);

        }
        return players;
    }




}
