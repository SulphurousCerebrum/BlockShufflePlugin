# BlockShufflePlugin
An implementation of the Block Shuffle Minigame 

Inspired by [Dream's](https://www.youtube.com/user/DreamTraps) YouTube [video](https://www.youtube.com/watch?v=p34C7fNFgTA). When the game starts, the inventory of each player is cleared, and they are given a fixed amount of food to start off (Can be set to 0 in the config file). A random block is assigned to each player and they score a point if they find the block and stand on it before their time runs out. After a fixed number of rounds, whoever has the highest score, wins.

## Available Commands
Every command starts with `/blockshuffle` with different parameters.

-   `/blockshuffle start` - Starts the game
-   `/blockshuffle stop` - Stops the game
-   `/blockshuffle info` - Shows the current game settings like total rounds, round time etc.
-   `/blockshuffle setRounds [number_of_rounds]` - Sets the number of rounds to be played
-   `/blockshuffle setRoundTime [round_time]` - Sets the round time in **ticks** (20 ticks is 1 second)
-   `/blockshuffle setFoodAmount [amount_of_food_to_be_given]` - Sets the initial amount of food to be given to each player. (Set to 0 to start with a clear inventory)

## How it works
PLugin reads the available blocks from [config.yml](https://github.com/SulphurousCerebrum/BlockShufflePlugin/blob/main/src/config.yml) and assigns a random block to each player. A check is done using BukkitScheduler which does a check every 10 ticks (half a second) 

