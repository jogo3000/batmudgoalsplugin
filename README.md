BatMUDGoalsPlugin
=================


## General Info
http://www.bat.org/play/batclient

Official batclient is now Javaless but still supports plugins.
Goal command allows the player to set a goal to improve a skill or a spell. 
After setting a goal 'exp' command outputs extra information showing what the 
goal skill/spell is, how much experience is needed for improving said 
skill/spell, and how much experience is missing from the needed amount.

## Installing

Build the plugin with gradle build. Copy the .jar file to your /plugins directory, and you're good to go.

## Usage

First the plug in needs some data to work on. Let's say the player is in the 
Ranger guild. The player should read the guild info by typing

'ranger info'

and scroll through the output. The skills and skill maxes for each level and 
player's level in the guild is then stored. This should be repeated for each
guild the player is a member of. Next the player should go to the trainer of 
the guild and list the experience cost for each percent of the skill that is 
their goal. For example, to read the costs for 'attack' skill

'cost train attack'

command should be used. Finally the goal skill/spell can be set. For example, 
to set a goal of improving 'attack' skill the command 

'goal attack'

should be used. After that amount of exp needed to improve the skill is shown 
with the command

'exp'

Unfortunately the commands do not work well with delimiters, so output can be a 
bit erratic if e.g.

'ranger info;cost train attack;goal attack'

is used. Delimiting these commands should be avoided.

## Dev info

Sources located in:
https://github.com/jogo3000/batmudgoalsplugin

I've tried too keep this without many dependencies. In fact, not counting the bat client's interface definitions, I'm only using test dependencies at the moment.

## Logging
Logging goes to Users' AppData/Local/Temp in Windows. It has been configured to the "system temporary directory" as specified in java.uti.logging.FileHandler
