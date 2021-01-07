package api;
import java.io.BufferedWriter;
import java.io.File;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.simple.parser.JSONParser;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import org.json.simple.parser.ParseException;


public class DWGraph_Algo implements dw_graph_algorithms {
	private DWGraph_DS graph;

	public DWGraph_Algo() {
		this.graph= new DWGraph_DS();
	}
	/**
	 * init
	 * @param G - the graph for the algorithms.
	 */
	public DWGraph_Algo(DWGraph_DS G) {
		this.graph= G;
	}
	/**
	 * Init the graph on which this set of algorithms operates on.
	 * @param g
	 */
	@Override
	public void init(directed_weighted_graph g) {
		this.graph = (DWGraph_DS) g;
	}
	/**
	 * Return the underlying graph of which this class works.
	 * @return
	 */
	@Override
	public directed_weighted_graph getGraph() {
		return this.graph;
	}
	/**
	 * Compute a deep copy of this weighted graph.
	 * @return
	 */
	@Override
	public directed_weighted_graph copy() {
		DWGraph_DS copy = new DWGraph_DS();
		if(!graph.getV().isEmpty()) {
			for (node_data i : graph.getV()) {
				copy.addNode(i);
			}
			for (node_data i : graph.getV()) {
				for (edge_data j : graph.getE(i.getKey())) {
					copy.connect(j.getSrc(), j.getDest(), j.getWeight());
				}
			}}
		return copy;
	}

	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return true if the graph is connected and not if not
	 */
	@Override
	public boolean isConnected() {
		if(graph==null||graph.getV().size()==0||graph.getV().size()==1)
			return true;
		if(DFS(this.graph,graph.getV().iterator().next())==-1)
			return false;
		if(DFS(transpose(),transpose().getV().iterator().next())==-1) {
			return false;
		}
		return true;
	}
	/**
	 * returns the length of the shortest path between src to dest, if no such path --> returns -1.
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return Distance of the src to dest in double
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(graph.getNode(src)==null||graph.getNode(dest)==null||shortestPath(src, dest)==null)
			return -1;
		if(src==dest)
			return 0;
		double ans = 0;
		int key = -1;
		for (node_data i : shortestPath(src, dest)) {
			if (key == -1) {
				key = i.getKey();
				continue;
			}
			ans += graph.getEdge(key, i.getKey()).getWeight();
			key = i.getKey();
		}

		return ans;
	}
	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * if no such path --> returns null;
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return the path of the way in list of node_data
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if (graph.getV().isEmpty() || graph.getV() == null || graph.nodeSize() == 0 || graph.nodeSize() == 1)
			return null;
		Map<Integer, node_data> visited = new HashMap<Integer, node_data>();
		List<node_data> ans = new LinkedList<node_data>();
		HashMap<Integer, Double> dis = new HashMap<>();
		PriorityQueue<node_data> que = new PriorityQueue<node_data>(new Comparator<node_data>() {
			@Override
			public int compare(node_data o1, node_data o2) {
				return -Double.compare(dis.get(o2.getKey()),dis.get(o1.getKey()));
			}
		});
		node_data start = graph.getNode(src);
		node_data s = start;
		node_data end = graph.getNode(dest);
		visited.put(graph.getNode(src).getKey(), graph.getNode(src));
		que.add(graph.getNode(src));
		dis.put(graph.getNode(src).getKey(), 0.0);
		while (que.size() != 0 && (!dis.containsKey(dest) || dis.get(que.peek().getKey()) < dis.get(dest))) {
			start = que.poll();
			visited.put(start.getKey(), start);
			for (edge_data n : graph.getE(start.getKey())) {
				if(!dis.containsKey(n.getDest())||dis.get(n.getDest())>dis.get(n.getSrc())+n.getWeight()){
					dis.put(n.getDest(),dis.get(n.getSrc())+n.getWeight());
					graph.getNode(n.getDest()).setTag(n.getSrc());
					que.add(graph.getNode(n.getDest()));
				}
			}
		}
		if (end.getTag() != 0 || dis.containsKey(dest)) {
			while (visited.get((int) end.getTag()) != null && end.getKey() != s.getKey()) {
				ans.add(end);
				end =  visited.get((int) end.getTag());
			}
			ans.add(s);
			cleanTags(visited);
			Collections.reverse(ans);
			return ans;
		}		return null;
	}
	/**
	 * Saves this weighted (directed) graph to the given
	 * file name - in JSON format
	 * @param file - the file name (may include a relative path).
	 * @return true - iff the file was successfully saved
	 * @throws FileNotFoundException
	 */
	@Override
	public boolean save(String file) {
		JSONArray ansE=new JSONArray();
		JSONArray ansN=new JSONArray();
		JSONObject ans=new JSONObject();

		FileWriter file2=null;
		try {
			file2 = new FileWriter(file);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		Iterator<node_data> it=graph.getV().iterator();
		while(it.hasNext()) {
			node_data temp=it.next();
			JSONObject n=new JSONObject();
			try {
				n.put("pos:", temp.getLocation().x()+" ,"+temp.getLocation().y()+" ,"+temp.getLocation().z());
				n.put("id:",temp.getKey());
			}
			catch (JSONException e1) {
				e1.printStackTrace();
			}
			ansN.put(n);	
			Iterator<edge_data> it2=graph.getE(temp.getKey()).iterator();
			while(it2.hasNext()) {		
				edge_data temp2=it2.next();
				JSONObject e=new JSONObject();
				try {
					e.put("src", temp.getKey());
					e.put("w", temp2.getWeight());
					e.put("dest", temp2.getDest());
					ansE.put(e);
				}
				catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}

		try {
			ans.put("Edges:",ansE);
			ans.put("Nodes",ansN);
		}
		catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			file2.write(ans.toString());
			file2.flush();
			System.out.println(ans.toString());

		}

		catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * This method load a graph to this graph algorithm.
	 * if the file was successfully loaded - the underlying graph
	 * of this class will be changed (to the loaded one), in case the
	 * graph was not loaded the original graph should remain "as is".
	 * @param file - file name of JSON file
	 * @return true - iff the graph was successfully loaded.
	 */
	@Override
	public boolean load(String file) {
		if (file.charAt(0) == '{') {
			try {
				JSONObject jsonObject = new JSONObject(file);
				JSONArray Edges = (JSONArray) jsonObject.get("Edges");
				JSONArray Nodes = (JSONArray) jsonObject.get("Nodes");
				//System.out.println(Nodes.toString());
				DWGraph_DS g = new DWGraph_DS();
				for (int i = 0; i < Nodes.length(); i++) {
					JSONParser parser1 = new JSONParser();
					JSONObject NodeD = new JSONObject(Nodes.get(i).toString());
					String[] s = NodeD.get("pos").toString().split(",");
					geo_location location1 = new geoLocation(Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]));
					nodeData node = new nodeData((int) Double.parseDouble(NodeD.get("id").toString()), location1);
					g.addNode(node);
				}
				for (int i = 0; i < Edges.length(); i++) {
					JSONParser parser1 = new JSONParser();
					JSONObject NodeD = new JSONObject(Edges.get(i).toString());
					g.connect((int) Double.parseDouble(NodeD.get("src").toString()), (int) Double.parseDouble(NodeD.get("dest").toString()), Double.parseDouble(NodeD.get("w").toString()));
				}
				init(g);
			} catch (JSONException e) {
				return false;
			}
			try {
				try {
					FileReader reader = new FileReader((file));
					JSONParser parser = new JSONParser();
					// JsonReader jsonReader= reader;
					JSONObject jsonObject = (JSONObject) parser.parse(reader);
					JSONArray Edges = (JSONArray) jsonObject.get("Edges");
					JSONArray Nodes = (JSONArray) jsonObject.get("Nodes");
					//System.out.println(Nodes.toString());
					DWGraph_DS g = new DWGraph_DS();
					for (int i = 0; i < Nodes.length(); i++) {
						JSONParser parser1 = new JSONParser();
						JSONObject NodeD = (JSONObject) parser1.parse(Nodes.get(i).toString());
						String[] s = NodeD.get("pos").toString().split(",");
						geo_location location1 = new geoLocation(Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]));
						nodeData node = new nodeData((int) Double.parseDouble(NodeD.get("id").toString()), location1);
						g.addNode(node);
					}
					for (int i = 0; i < Edges.length(); i++) {
						JSONParser parser1 = new JSONParser();
						JSONObject NodeD = (JSONObject) parser1.parse(Edges.get(i).toString());
						g.connect((int) Double.parseDouble(NodeD.get("src").toString()), (int) Double.parseDouble(NodeD.get("dest").toString()), Double.parseDouble(NodeD.get("w").toString()));
					}
					init(g);
				} catch (FileNotFoundException e) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true;

	}

	/**
	 * Checks whether the class graph is equal to the graph we got from the function.
	 *@param sec
	 * @return true if they are equal and false if not.
	 */
	public boolean graph_equals(directed_weighted_graph sec) {
		boolean yah=true;
		if(this.graph.getV().size()!=sec.getV().size()) {
			return false;
		}
		for(node_data i: this.graph.getV()) {
			for(edge_data j: this.graph.getE(i.getKey()) ) {
				yah&=sec.getEdge(i.getKey(), j.getDest()).equals(this.graph.getEdge(i.getKey(), j.getDest()));
			}
		}
		return yah;
	}
	/**
	 *Dfs algorithm. The algorithm starts the search from a node in the graph
	 * and advances along the graph until it gets stuck,
	 * then it repeats its traces until it can choose to advance to the node it has not yet reached.
	 *@param start-My starting point.
	 * @param g-The graph I am going through.
	 * @return the number of connected nodes.
	 */
	public int DFS(directed_weighted_graph g,node_data start) {
		// hashmap saves all nodes which visited by bfs algorithm
		Map<Integer, node_data> visited = new HashMap<Integer, node_data>();
		// queue of BFS
		Stack<node_data> queue = new Stack<node_data>();
		visited.put(start.getKey(), start);
		queue.push(start);
		int ans=-1;
		while (queue.size() != 0) {
			// take the first-added node, poll it from the queue, name it start and repeat the process
			start = queue.pop();
			//check all start node neighbours and add the unchecked nodes to the queue and the hashmap
			for (edge_data n : g.getE(start.getKey())) {
				if (!visited.containsKey(n.getDest())) {
					visited.put(n.getDest(), g.getNode(n.getDest()));
					queue.push(g.getNode(n.getDest()));
					if(visited.size()==g.nodeSize()) {
						ans = n.getDest();
					}
				}
			}
		}
		//returns the number of connected nodes
		return ans;
	}
	/**
	 * This function transpose on the graph and returns it after the change.
	 *@return
	 */
	public directed_weighted_graph transpose() {
		DWGraph_DS trans = new DWGraph_DS();
		for (node_data i : graph.getV()) {
			trans.addNode(i);
		}
		for (node_data i : graph.getV()) {
			for (edge_data j : graph.getE(i.getKey())) {
				trans.connect(j.getDest(),j.getSrc(), j.getWeight());
			}
		}
		return trans;
	}
	/**
	 * the function set back the graph's node's tags to 0.
	 *@param hashMap
	 */
	public void cleanTags(Map<Integer, node_data> hashMap) {
		hashMap.forEach((key, value) -> {
			value.setTag(0);
		});
	}
	public List<Integer> DFS1(directed_weighted_graph g,int s) {
		node_data start = g.getNode(s);
		// hashmap saves all nodes which visited by bfs algorithm
		Map<Integer, node_data> visited = new HashMap<Integer, node_data>();
		// queue of BFS
		Stack<node_data> queue = new Stack<node_data>();
		visited.put(start.getKey(), start);
		queue.push(start);
		while (queue.size() != 0) {
			// take the first-added node, poll it from the queue, name it start and repeat the process
			start = queue.pop();
			//check all start node neighbours and add the unchecked nodes to the queue and the hashmap
			for (edge_data n : g.getE(start.getKey())) {
				if (!visited.containsKey(n.getDest())) {
					visited.put(n.getDest(), g.getNode(n.getDest()));
					queue.push(g.getNode(n.getDest()));
				}
			}
		}
		//returns the number of connected nodes
		return (List<Integer>) visited.keySet();
	}

	public int connectivityParts(){
		List<Integer> li = DFS1(graph,graph.getV().iterator().next().getKey());
		int ans =0;
		directed_weighted_graph g=transpose();
		while (li.size()>0){
			List<Integer> litg= DFS1(g,li.iterator().next());
			li.removeAll(litg);
			for (int i: litg){
				g.removeNode(i);
			}
		}
		return ans;
	}
}
