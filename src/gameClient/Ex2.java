package gameClient;

import api.*;

import gameClient.CL_Pokemon;
import gameClient.CL_Agent;
//import dataStructure.*;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server_Ex2;
import api.game_service;

import gameClient.util.Point3D;
import gameClient.util.StdDraw;

import javax.swing.*;

import static java.lang.System.*;
import Server.Game_Server_Ex2;

import java.util.*;


/**
	 * This class represents a simple example for using the GameServer API:
	 * the main file performs the following tasks:
	 * 1. Creates a game_service [0,23] (line 36)
	 * 2. Constructs the graph from JSON String (lines 37-39)
	 * 3. Gets the scenario JSON String (lines 40-41)
	 * 4. Prints the fruits data (lines 49-50)
	 * 5. Add a set of robots (line 52-53) // note: in general a list of robots should be added
	 * 6. Starts game (line 57)
	 * 7. Main loop (should be a thread) (lines 59-60)
	 * 8. move the robot along the current edge (line 74)
	 * 9. direct to the next edge (if on a node) (line 87-88)
	 * 10. prints the game results (after "game over"): (line 63)
	 *
	 * @author boaz.benmoshe
	 *
	 */
	public class Ex2 {
		private static Queue<Integer>[] myway = new ArrayDeque[5];
		private static boolean auto = false;
		static long time;
		private static int scenario_num = -1;


		public static void main(String[] a)
		{
			theGame();
		}

		/**
		 * That is the main thing. When everything connected.
		 * The heart and the brain of the system.
		 */
		private static void theGame() {
			JFrame f = new JFrame();
			connect();
			//KmlForGame kmlForGame = new KmlForGame();
			try {
				auto =  JOptionPane.showConfirmDialog(f, "Do you want auto game?", "Start Game",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			} catch (Exception e){
				exit(1);
			}
			while (!(scenario_num <=23&&scenario_num>=0)) {
				try {
					scenario_num = Integer.parseInt(JOptionPane.showInputDialog(f, "Pls select level between 0-23 "));

				} catch (Exception e){

					exit(1);
				}
			}
			game_service game = Game_Server_Ex2.getServer(11); // you have [0,23] games
			String g = game.getGraph();
			DWGraph_Algo gg = new DWGraph_Algo();
			gg.load(g);
			Graph_GUI myg = new Graph_GUI((DWGraph_DS) gg.getGraph(), game);
			String info = game.toString();
			JSONObject line;
			try {
				line = new JSONObject(info);
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("pokemons");
				if(!auto)
					JOptionPane.showMessageDialog(f,"The game is about to start.\n" +
							"Pls select where to put the robots by clicking on node.\n" +
							"You have "+ rs+ " robots.");
				for (int a = 0; a < rs; a++) {
					int src_node = -1;
					if (!auto) {
						while (src_node == -1) {
							if (StdDraw.isMousePressed()) {
								double x = StdDraw.mouseX();
								double y = StdDraw.mouseY();
								src_node = findsrc(x, y, (DWGraph_DS) gg.getGraph());
							}
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {
							//do stuff
						}
					} else {
						src_node = findStart( (DWGraph_DS) gg.getGraph(), game) + a+1;
						if (a==1&& scenario_num == 13)
							src_node = 40;
						if (scenario_num==16||scenario_num==23) {
							if (a == 1) {
								src_node = 10;
							}
							else
								src_node = 39;
						}
						if (scenario_num == 23){
							if (a==0){
								src_node =  13;
							}
							else if(a==1){
								src_node = 19;
							}
							else {
								src_node = 40;
							}
						}
//					if (scenario_num==20) {
//						if (a == 1) {
//							src_node = 10;
//						}
//						else if(a==2)
//							src_node = 15;
//						else if(a==0)
//							src_node = 19;
//					}

					}
					game.addAgent(src_node);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				///kmlForGame.addGraph(gg);
				if (!auto)
					JOptionPane.showMessageDialog(f,"START GAME");
				game.startGame();
				Long l = game.timeToEnd();
				try {
					time = game.timeToEnd();
					pokemons pokemons = new pokemons(game,  (DWGraph_DS) gg.getGraph());

					while (game.isRunning()) {
						if (l-game.timeToEnd()>119L) {
							///kmlForGame.writeMyRnF(gg, game);
							l = game.timeToEnd();

							moveRobots(game,  (DWGraph_DS) gg.getGraph(), myg, pokemons);
						}


					}
				} catch (Exception e) {
					pokemons pokemons = new pokemons(game,  (DWGraph_DS) gg.getGraph());
					while (game.isRunning()) {
						moveRobots(game,  (DWGraph_DS) gg.getGraph(), myg, pokemons);
					}
				}
			} catch (Exception ignored) {
			}
			String results = game.toString();
			System.out.println("Game Over: " + results);
			try {
				JSONObject endGame;
				endGame = new JSONObject(results);
				JSONObject ttt = endGame.getJSONObject("GameServer");
				int rs = ttt.getInt("grade");
				int moves = ttt.getInt("moves");
				JOptionPane.showMessageDialog(f, "Game Over \n" +
						"Game scenario num: "+scenario_num+"\n"+
						"your grade is:     " + rs+"\n" +
						"amount of moves:   "+ moves);

				int save = JOptionPane.showConfirmDialog(f, "Do you want to save this game in kml?\n" +
						"your score was: "+rs+"\n" +
						"Game scenario num: "+scenario_num);
				if (save == 0) {

					String filename = JOptionPane.showInputDialog(f, "Enter name to file save game REMEMBER: grade was: "+ rs+"\n" +
							"Game scenario num: "+scenario_num);
					//kmlForGame.saveToFile(filename);
					//game.sendKML(kmlForGame.getLgerOfGame()+"");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//exit(0);
		}

		/**
		 * ask the player if he want to connect
		 */
		private static void connect() {
			JFrame f = new JFrame();
			try {
				if(  JOptionPane.showConfirmDialog(f, "Do you want to connect the server?", "Start Game",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					int id = Integer.parseInt(JOptionPane.showInputDialog(f, "Pls enter ID "));
				//	Game_Server_Ex2.login(id);



				}
			} catch (Exception e){
				JOptionPane.showMessageDialog(f, "something went wrong I'il continue now.. ");
			}

		}

		/**
		 *
		 * @param gg -  graph of the game "map"
		 * @param game - the server game that running
		 * @return src to start on the graph
		 */
		private static int findStart(DWGraph_DS gg, game_service game) {
			pokemons po = new pokemons(game, gg);
			CL_Pokemon max = po.getMaxValue();
			//pokemons p=new pokemons();
			//CL_Pokemon max = p.getMaxValue();
			edge_data eData = po.getEdge(max.getId());
			if (eData != null) {
				if (max.getType() == -1) {
					return eData.getSrc();
				} else
					return eData.getDest();

			}
			System.out.println("PROBLEM");
			return 0;
		}

		/**
		 * Moves each of the robots along the edge,
		 * in case the robot is on a node the next destination (next edge) is chosen.
		 * Not randomly.
		 * We have AI (Kind Of) that separate the fruit between the robot and strategy to each one Robot.
		 *
		 * @param game - server
		 * @param gg - Map (graph)
		 */
		private static void moveRobots(game_service game, DWGraph_DS gg, Graph_GUI myGameGUI,pokemons pokemons) {
			String[] log = game.move().split(",");
			if (log != null) {
				long t = game.timeToEnd();
				for (int i = 0; i < log.length; i++) {

					String robot_json = log[i];
					try {
						JSONObject line = new JSONObject(robot_json);
						JSONObject ttt = line.getJSONObject("Robot");
						int rid = ttt.getInt("id");
						int src = ttt.getInt("src");
						int dest = ttt.getInt("dest");
						int speed = ttt.getInt("speed");

						if (dest == -1) {
							if (auto) {
								pokemons =  new pokemons(game,gg);
								Agents list = new Agents(game,gg);
								if (rid==0) {
									boolean b = scenario_num ==19;
									dest = goCloser(gg, src, rid, game,pokemons,b);
//								if (list.collection().size()<3 && speed<4){
//								dest = nextNode(gg, src, fruits);

//								}
//								else if (speed==1){
//									dest = getFF(gg,game,rid,src,fruits,1);
//								}
//								if (speed>=4){
////									dest = goCloser(gg, src, rid, game,fruits,true);
////									dest = getFF(gg,game,rid,src,fruits,0);
//									dest = nextNode(gg, src, fruits);
////									if (list.zise()) {
////										dest = nextNode(gg, src, rid, game, fruits);
////									}
//									}
//								}
									if (scenario_num == 23){
										dest = getAtAREA(gg,0,13,src,pokemons);
									}
									if (speed>3&&scenario_num!=23){
										//	dest = getMinf(gg, src,fruits,3);
										//dest = nextNode(gg, src, fruits);
										dest = goCloser(gg, src, rid, game,pokemons,b);
									}
								}
								if (rid==1) {
									if (scenario_num!=22&&scenario_num!=23&&scenario_num!=16) {
//								dest = getMinf(gg, src, rid, game,fruits);
//								dest = getFF(gg,game,rid,src,fruits,4);
//									dest = nextNode(gg, src, fruits);
									}
									else if (scenario_num==22||scenario_num==16||scenario_num==23) {
//									dest = goCloser(gg, src, rid, game, fruits, true);
										//dest = nextNode(gg, src, fruits);
										dest = getAtAREA(gg,16,32,src,pokemons);
										if (game.timeToEnd()<5000){
											dest = getFF(gg,src,pokemons,1);
										}
//								dest = goCloser(gg, src, rid, game,fruits,true);
//									dest = getFF(gg,src,fruits,1);
									}
									else {
										dest = getFF(gg,src,pokemons,3);
									}
								}
//							if ((dest == -1 ||speed > 3)&&log.size()!=1){
//								dest = nextNode(gg, src, rid, game,fruits);
//							}
								if ((dest == -1 && rid != 2 )) {
//								dest = nextNode(gg, src, fruits);
									dest = getMinf(gg, src,pokemons,2);
//								dest = goCloser(gg, src, rid, game,fruits,true);
								}
								if (dest==-1){
									//dest = goCloser(gg, src, rid, game,fruits,false);
//								dest = getMinf(gg, src,fruits);
									//dest = getFF(gg,src,fruits,2);
									dest = getAtAREA(gg,23,50,src,pokemons);
									if (speed>2&& scenario_num != 23) {
										dest = getMinf(gg, src, pokemons, 0);
										//dest = goCloser(gg, src, rid, game,fruits,true);
									}
								}


								if (dest == -1) {
									dest = randomedge(gg, src, rid);
								}
							} else {
								dest = nextNode(gg, src,null);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * return the src the the minimum fruit is on
		 * @param g - graph of the game "Map"
		 * @param src - where is the robot now
		 * @param pokemons - all the pokemons currently
		 * @return next node in int(id)
		 */

		private static int getMinf(DWGraph_DS g, int src,pokemons pokemons,int fromEND) {
			DWGraph_Algo graph_algo = new DWGraph_Algo(g);
			CL_Pokemon fruit = pokemons.geMinValue(fromEND);
			edge_data edgedata = pokemons.getEdge(fruit.getId());

			if (edgedata == null) {
				return -1;
			}
			if (edgedata.getDest() == src)
				return edgedata.getSrc();
			if (edgedata.getSrc() == src)
				return edgedata.getDest();
			if (fruit.getType() == -1) {

				return graph_algo.shortestPath(src, edgedata.getDest()).get(1).getKey();
			} else return graph_algo.shortestPath(src, edgedata.getSrc()).get(1).getKey();
		}

		private static int goCloser(DWGraph_DS g, int src, int rid, game_service game,pokemons pokemons,boolean spd) {
			DWGraph_Algo graph_algo = new DWGraph_Algo(g);
			Agents agent = new Agents(game, g);
			CL_Agent r = agent.getAgent(rid);
			CL_Pokemon fruit = pokemons.getCloseF(src,spd);

			edge_data edgedata = pokemons.getEdge(fruit.getId());
			if (edgedata == null) {
				return -1;
			}
			if (edgedata.getDest() == src)
				return edgedata.getSrc();
			if (edgedata.getSrc() == src)
				return edgedata.getDest();
			if (fruit.getType() == -1) {

				return graph_algo.shortestPath(src, edgedata.getDest()).get(1).getKey();
			} else return graph_algo.shortestPath(src, edgedata.getSrc()).get(1).getKey();

		}

		/**
		 *
		 * @param g - Map (graph)
		 * @param src - where the robot is now
		 * @param rid - robot id
		 * @return next node in id
		 */

		private static int randomedge(DWGraph_DS g, int src, int rid) {
			DWGraph_Algo graph_algo = new DWGraph_Algo(g);

			Iterator<node_data> temp = g.getV().iterator();
			ArrayList<Integer> showChoices = new ArrayList<>();
			while (temp.hasNext()) {
				showChoices.add(temp.next().getKey());
			}
			Collection<node_data> ev = g.getV();
			myway[rid] = new ArrayDeque<>();
			int r = (int) (Math.random() * ev.size());
			Iterator<node_data> temp2 = graph_algo.shortestPath(src, showChoices.get(r)).iterator();
			while (temp2.hasNext()) {
				myway[rid].add(temp2.next().getKey());
			}
			//
			if (myway[rid] == null || myway[rid].isEmpty()) {
				return -1;
			}
			try {
				return graph_algo.shortestPath(src, showChoices.get(r)).get(1).getKey();
			} catch (Exception e) {
				return -1;
			}
		}

		/**
		 * a very simple choose next edge
		 * if this is manual game - we only find next node by x,y of the mouse
		 * else  so we need to active the AI that we crated
		 *
		 * @param g - Map (graph)
		 * @param src - where the robot is now
		 * @return next node in id
		 */
		private static int nextNode(DWGraph_DS g, int src,pokemons pokemons) {
			if (!auto) {
				if (StdDraw.isMousePressed()) {
					double x = StdDraw.mouseX();
					double y = StdDraw.mouseY();
					src = findsrc(x, y, g);
					return src;
				}
				return -1;
			} else {
				return findNextNode(g, src,pokemons);
			}
		}

		/**
		 * this method uses the Ai in Fruits and solve bugs that the robot stay in place
		 * @param g - graph of the game "Map"
		 * @param src - where is the robot now
		 * @param pokemons - all the pokemons currently
		 * @return id of the next node
		 */
		private static int findNextNode(DWGraph_DS g,int src,pokemons pokemons) {
			DWGraph_Algo graph_algo = new DWGraph_Algo(g);
			//Robots robots = new Robots(game,g);
			//Robot current = robots.getRobot(rid);
			CL_Pokemon max = pokemons.getMaxValue();
			edge_data eData = pokemons.getEdge(max.getId());
			if (eData != null) {
				if (eData.getDest() == src) {
					return eData.getSrc();
				}
				if (eData.getSrc() == src) {
					return eData.getDest();
				}
				if (max.getType() == -1) {

					return graph_algo.shortestPath(src, eData.getDest()).get(1).getKey();
				} else return graph_algo.shortestPath(src, eData.getSrc()).get(1).getKey();
			}
			return -1;
		}

		/**
		 * where is the user click on the screen
		 * @param x - mouse x
		 * @param y - mouse y
		 * @param g - Map (graph)
		 * @return id of the node
		 */

		private static int findsrc(double x, double y, DWGraph_DS g) {
			for (node_data temp : g.getV()) {
				geo_location lTemp = temp.getLocation();
				if (Math.abs(lTemp.x() - x) < 0.0003 && Math.abs(lTemp.y() - y) < 0.0003)
					return temp.getKey();
			}
			return -1;
		}

		/**
		 * one of the nice method tha help to separate the fruits
		 * but be careful because there is no always a lot of fruits
		 *
		 * @param g - Map (graph)
		 * @param src - where is the robot now
		 * @param fruts - all the fruits currently
		 * @param id - of the fruit
		 * @return id of the next node
		 */
		private static int getFF(DWGraph_DS g,int src,pokemons fruts,int id){
			DWGraph_Algo algo =new DWGraph_Algo(g);
			edge_data dde = fruts.getEdge(id);
			if (dde!=null)
				if (dde.getDest()==src)
					return dde.getSrc();
				else if (dde.getSrc()==src){
					return dde.getDest();

				}
				else return algo.shortestPath(src,dde.getDest()).get(1).getKey();
			return -1;

		}

		/**
		 * this method solve level 23 by separate the fruits between the robots by area.
		 * @param g - map
		 * @param rX - min key node
		 * @param rY - max key node
		 * @param src - src of the robot
		 * @param pokemons - all the fruits
		 * @return int (next node to the root)
		 */
		private static int getAtAREA(DWGraph_DS g,int rX,int rY,int src,pokemons pokemons){
			for (Iterator<CL_Pokemon> it = pokemons.iterator(); it.hasNext(); ) {
				CL_Pokemon p = it.next();
				if (p.inRange(rX,rY)) {
					return getFF(g, src, pokemons, p.getId());
				}
			}
			return -1;
		}

	}

