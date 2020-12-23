package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {
private int edges=0; 
private int MC=0; 
private HashMap<Integer, node_data> nodes;
private HashMap<Integer, HashMap<Integer, edge_data>> go;
private HashMap<Integer, HashMap<Integer, edge_data>> back;
	/**
	 * default contractor
	 */
public DWGraph_DS() {
	this.edges=0;
	this.MC=0;
	this.nodes=new HashMap<Integer, node_data>();
	this.go=new HashMap<Integer, HashMap<Integer, edge_data>>();
	this.back=new  HashMap<Integer, HashMap<Integer, edge_data>>();
}
public DWGraph_DS(DWGraph_DS g) {
	this.edges=g.edges;
	this.MC=g.MC;
	this.nodes=g.nodes;
	this.go=g.go;
	this.back=g.back;
}
	/**
	 *
	 * @param key - the node_id.
	 * @return node in node_data, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		return nodes.get(key);
	}
	/**
	 *
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @return edge from src to dest in edge_data, null if none.
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
	    if(nodes.containsKey(src)&& nodes.containsKey(dest)&&go.containsKey(src)&&go.get(src).containsKey(dest)) {
			return go.get(src).get(dest);
		}
	    return null;
	}
	/**
	 * adds a new node to the graph with the given node_data.
	 * @param n
	 */
	@Override
	public void addNode(node_data n) {
		this.nodes.put(n.getKey(), n);
		HashMap<Integer, edge_data> nodesgo = new HashMap<>();
		HashMap<Integer, edge_data> nodesback = new HashMap<>();
		this.go.put(n.getKey(), nodesgo);
		this.back.put(n.getKey(), nodesback);
		MC++;

	}
	/**
	 * Connect an edge with weight w between node src to node dest.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if(nodes.containsKey(src)&& nodes.containsKey(dest)) {
			if(go.get(src).get(dest)!=null){
			edge_data goedge = new edgeData(this.nodes.get(src), this.nodes.get(dest), w);
			go.get(src).put(dest, goedge);
			back.get(dest).put(src, goedge);
			MC++;
		}
			else{
				edge_data goedge = new edgeData(this.nodes.get(src), this.nodes.get(dest), w);
				go.get(src).put(dest, goedge);
				back.get(dest).put(src, goedge);
				edges++;
				MC++;
			}
		}
	}
	/**
	 *
	 * @return all nodes in collection
	 */
	@Override
	public Collection<node_data> getV() {
		return nodes.values();
	}
	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the edges getting out of
	 * the given node (all the edges starting (source) at the given node).
	 * @return Collection<edge_data>
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		return go.get(node_id).values();
	}
	/**
	 * Deletes the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(k), V.degree=k, as all the edges should be removed.
	 * @return the data of the removed node (null if none).
	 * @param key
	 */
	@Override
	public node_data removeNode(int key) {
	if(!nodes.containsKey(key))	
		return null;
    Iterator<Integer> ig=this.go.get(key).keySet().iterator();
    while(ig.hasNext()) {
	   back.get(ig.next()).remove(key);
     }
    Iterator<Integer> ib=this.back.get(key).keySet().iterator();
    while(ib.hasNext()) {
	   go.get(ib.next()).remove(key);
     }
    edges-=(go.get(key).size()+back.get(key).size());
    MC++;
    go.remove(key);
    node_data ans=nodes.get(key);
    nodes.remove(key);
    return ans;
	}
	/**
	 *
	 * @param src - src of this edge
	 * @param dest - dest of this node
	 * @return edge that removed
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if(!nodes.containsKey(src)||!nodes.containsKey(dest))
			return null;
		if(go.get(src).get(dest)==null)
			return null;
		edge_data ans=go.get(src).get(dest);
		go.get(src).remove(dest);
		back.get(dest).remove(src);
		edges--;
		MC++;
		return ans;
	}
	/**
	 *
	 * @return amount of the node in the graph
	 */
	@Override
	public int nodeSize() {
		return nodes.size();
	}
	/**
	 *
	 * @return amount of the edge in the graph
	 */
	@Override
	public int edgeSize() {
		return this.edges;
	}
	/**
	 * the mode change every time when the graph changed.
	 * @return Mode Count
	 */
	@Override
	public int getMC() {
		return MC;
	}
	/**
	 * Checks whether the class graph is equal to the object we received.
	 *@param second
	 * @return true if they are equal and false if not.
	 */
	public boolean equals(Object second) {
		DWGraph_DS sec=(DWGraph_DS)second;
		boolean yah=true;
		if(this.getV().size()!=sec.getV().size()) {
			return false;
		}
		for(node_data i: this.getV()) {
			for(edge_data j: this.getE(i.getKey()) ) {
				yah&=sec.getEdge(i.getKey(), j.getDest()).getDest()
						==this.getEdge(i.getKey(), j.getDest()).getDest();
			}
		}
		return yah;
	}
	
}
