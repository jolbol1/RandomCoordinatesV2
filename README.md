# RandomCoordinatesV2
![Logo](http://i.imgur.com/AAKjFOE.png)
***
# Features
![TrulyRandom](http://i.imgur.com/nXXzyfC.png)
---
The main feature of this plugin, is to teleport a player to a random location. This location is chosen, and then the necessary checks are carried out to see if the location is safe. The method for gathering a random number in Java may not necessarily be "Truly Random" [(Explanation)](https://github.com/jolbol1/RandomCoordinatesV2/wiki/Truly-Random-Explanation), This is why in my plugin, I have added the ability to use Random.Org to generate a random coordinate! 

**Features relating to the random teleport function are as follows:**
* Ability to set a global Maximum coordinate and Minimum coordinate, As well as per world.
* Ability to blacklist worlds
* Ability to set the center of the Max, Min boundary.
* Ability to disable teleporting into / nearby to Factions, World Guard regions, WorldBorder plugin, Towny, Grief Prevention Claims, Nearby Players
* Ability to set a Limit on the amount of times they can Randomly Teleport
* Ability to teleport players on FIRST join, plus the ability to force a command when done (e.g /SetHome)
* Ability to set cooldown on the command
* Ability to set the time between running the command, and actually teleporting
* Ability to stop them teleporting if they move, or if they are involved in combat
* Ability to allow players a set invulnerable time when teleported
* Ability to play sound and effect to the player on teleport
* Ability to set the price of the command

---
![Safety Checks](http://i.imgur.com/ug1Glsg.png)

Teleporting to a random location could be dangerous, there could be lava etc. This is why in this plugin, you can only teleport if the location is free of such things!. It also wont teleport you into water, because who wants to be out in the ocean! In the event there is something there that could hurt you, You could always make the Invulnerable time longer! Not only this, but many claim that on slow servers, there is a chance you may teleport in a wall, With this plugin, If suffocating in the wall after teleport, you'll be taken to the surface!

**Not only does it check for environment damage, you can also make it avoid claims such as:**
* Factions
* Towny
* WorldGuard
* GriefPrevention
* WorldBorder plugin
* Vanilla World Border
* Nearby Players

---
![Portals](http://i.imgur.com/rIvwyS6.png)

This plugin is not just a command, You can also set up random teleport portals. These portals teleport players in the same way the command would when they enter, But they also allow you to randomly teleport them in another world. Thse are incredibly easy to set up and no longer needs WorldEdit to do so!

**Alongside these features, you can also:**
* Apply the limiter to portals
* Charge players for portals
* Place nether portal blocks inside of the portal, as well as flowing water / lava without it spreading outwards

---
![Warps](http://i.imgur.com/m4XjicW.png)

Alongside teleporting to random coordinates, you can also allow them to be teleported to a random warp, a preset location. This enables you to have warps in the world, but it be random as to which one they go to.

**Alongside these features, you can also:**
* Disable warping cross world, Will only warp to ones in current world
* Charge for using the warps
* Use a command just for warps
* Limit the usage of warps 
* Change the methods of Signs, Portals and Commands in order to only go to the warps

---
![Signs](http://i.imgur.com/CLT0OGG.png)

Alongside the commands, You can also set up Signs to do the same thing when clicked on. These signs can allow you to set the world they randomly teleport the player in, as well as a per sign price. 

**Alongside these features, you can also:**
* Limit the usage of signs
* Stop signs being broken by non-admins
* Change signs method to only go to random warps (See Warps above)

---
![Bonus Chest](http://i.imgur.com/kcuVO1E.png)

This feature directly relates to the OnJoin feature. This is where the player will teleport to a random location on first join. Alongside this I have added an ability to spawn a chest next to them, with various configurable items. If you are using essentials, you can set a kit to be in that chest. Lastly, the chest will only spawn once, on the first join.
