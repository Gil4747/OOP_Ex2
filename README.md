# OOP_Ex2
This README explains the 3rd asssinment we got on OOP course in Ariel university.

the assinment has two parts:

the first part is to create a directed weighted graph that represents real locations.

there were 5 classes for this part:

*geoLocation is the most basic one, gives the location as x,y,z variables and distances between two of them.

*nodeData represents the nodes in the graph, each node has some information and a geoLocation object.

*edgeData represents the edges on the graph, ech edge has source and destenation nodes and the weight of the edge.

*DWGraph_DS represents the graph itself, includes nodes, edges and mode account that count the changes in the graph.

*DWGraph_Algo is the class that made to use any given graph, the funclions checks connectivity, shortest way between two nodes,
 they can save the graph as jason file and load graph in the same way.

the second part is to run the "Pokemons Game".
the game has agents that can move only on the graph's edges. those agents should collect pokemons that spreds on the graph's edges 
