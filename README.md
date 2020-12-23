# OOP_Ex2
### This README explains the 3rd asssinment we got on OOP course in Ariel university. 
### the assinment has two parts:

#### the first part is to create a directed weighted graph that represents real locations.
#### there were 5 classes for this part:

- geoLocation is the most basic one, gives the location as x,y,z variables (represent coordinates and hight) 
and distances between any two of them.

- nodeData represents the nodes in the graph, each node has some information and a geoLocation object.

- edgeData represents the edges on the graph, ech edge has source and destenation nodes and the weight of the edge.

- DWGraph_DS represents the graph itself, includes nodes, edges and mode account that count the changes in the graph.

- DWGraph_Algo is the class that made to use any given graph, the funclions checks connectivity, 
shortest way between two nodes, they can save the graph as jason file and load graph in the same way.


#### The second part is to run the "Pokemons Game":

The game has agents that can move only on the graph's edges. 
Those agents should collect pokemons that spreds on the graph's edges.
We were asked to show a window with the game on it by using jframe methods, 
and get as high scoure as possible by collecting the pokemons.

The game works with web server that can send information about the graph, the agents and the pokemons and their location.
We got an Arena class (which we improvise) that translate the jason formats to DWGraph_DS, CL_agent and CL_pokemon  objects.

The class ex2 created to run the game.
The class init the game by asking what level to play, if to save the id of the player 
and where to locate the agents in the begining of the game.
on every 10th of a second we moved the agents and choose for them the next move in the function 'nextNode'.

#### The method works this way:

First we choose a close location in the init part and locate the agents there.
Then they move towords the pokemon by using shortest path method from DWGraph_Algo.
When they catch an pokemon(get close to its location) they search for another pokemon that hasn't taken by another agent yet.

We used the class myFrame to show the game by using the jFrame methods the window shoes the graph,
the pokemons and their type(represent on wich edge are they), the agents and their value (it updates every time the cought a pokemon) 
and the time left untill the game will be over.

##### This asssinment has written by Gil Zioni and Itamar Shpitzer.
