package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class myTest {
 private static Random _rnd = null;

 @Test
 void copy() {
	 directed_weighted_graph g= myGraph();
	 dw_graph_algorithms ga = new DWGraph_Algo();
     ga.init(g);
     directed_weighted_graph cop=ga.copy();
     assertEquals(cop,g);
 }
/**
 * Checks the amount of vertices in the graph.
 */
// @Test
// void nodeSize() {
//   directed_weighted_graph g = new DWGraph_DS();
//   g.addNode(0);
//   g.addNode(1);
//   g.addNode(2);
//   g.addNode(3);
//   g.addNode(0);
//	g.removeNode(0);
//   g.removeNode(2);
//   g.removeNode(4);
//   int s = g.nodeSize();
//   assertEquals(2,s);
//	}
// /**
//  * Checks the amount of arcs in the graph.
//  */
// @Test
// void edgeSize() {
//	 directed_weighted_graph g = new WGraph_DS();
//     g.addNode(0);
//     g.addNode(1);
//     g.addNode(2);
//     g.addNode(3);
//     g.addNode(4);
//     g.addNode(5);
//     g.addNode(6);
//     g.connect(0,1,2.0);
//     g.connect(0,2,2.0);
//     g.connect(1,5,1.0);
//     g.connect(1,1,10.0);
//     g.connect(3,2,8.0);
//     g.connect(4,6,4.0);      
//     g.connect(2,3,1.0);
//     g.connect(5,1,1.0);
//     int sizeEdge =  g.edgeSize();
//     assertEquals(5, sizeEdge);
//     double x1 = g.getEdge(0,2);
//     double x2 = g.getEdge(2,0);
//     assertEquals(x1, x2);
//     assertEquals(x2, 2.0);
// }
// /**
//  * Checks if there is no vertex that does not exist in the collection.
//  */
// @Test
// void getV() {
//	 directed_weighted_graph g = new DWGraph_DS();
//     Collection<node_data> x = g.getV();
//     Iterator<node_data> i = x.iterator();
//     while (i.hasNext()) {
//         node_data n = i.next();
//         assertNotNull(n);
//     }
//     g.addNode(0);
//     g.addNode(1);
//     g.addNode(2);
//     g.addNode(3);
//     g.addNode(4);
//     g.addNode(5);
//     g.connect(0,1,1.0);
//     g.connect(1,0,1.0);
//     g.connect(1,2,4.0);
//     g.connect(2,3,1.4);
//     g.connect(3,3,1.4);
//     g.connect(3,0,5.8);
//     g.connect(4,5,6.0);
//     g.connect(7,1,2.0);
//     Collection<node_data> c = g.getV();
//     Iterator<node_data> path = c.iterator();
//     while (path.hasNext()) {
//         node_data node = path.next();
//         assertNotNull(node);
//     }
// }
 @Test
 void isConnected() {
//     directed_weighted_graph g0=graph_creator(0,0,1);
//     dw_graph_algorithms ag0 = new DWGraph_Algo();
//     ag0.init(g0);
//     assertTrue(ag0.isConnected());
//     
//     g0=graph_creator(0,0,1);
//     ag0 = new DWGraph_Algo();
//     ag0.init(g0);
//     assertTrue(ag0.isConnected());
//
//     g0=graph_creator(1,0,1);
//     ag0 = new DWGraph_Algo();
//     ag0.init(g0);
//     assertTrue(ag0.isConnected());
//
//      g0=graph_creator(4,3,1);
//     ag0=new DWGraph_Algo();
//     ag0.init(g0);
//     assertFalse(ag0.isConnected());
//     
//      g0=graph_creator(2,1,1);
//     ag0=new DWGraph_Algo();
//     ag0.init(g0);
//     assertTrue(ag0.isConnected());
//     
//     g0=graph_creator(15,15,1);
//     ag0=new DWGraph_Algo();
//     ag0.init(g0);
//     assertFalse(ag0.isConnected());
//     
//     g0=graph_creator(15,23,1);
//     ag0=new DWGraph_Algo();
//     ag0.init(g0);
//     assertFalse(ag0.isConnected());
//
//     g0=graph_creator(8,16,1);
//     ag0.init(g0);
	 directed_weighted_graph g0 = new DWGraph_DS();
	    for (int i = 0; i < 4; i++) {
			geoLocation location= new geoLocation(0, 0, i);
	    	nodeData n = new nodeData(i, location);
	    	g0.addNode(n);
		}
		 g0.connect(0,1,1);
	     g0.connect(0,3,4.0);
	     g0.connect(3,0,4.0);
	     g0.connect(1,2,0.1);
	     g0.connect(1,3,10.0);
     dw_graph_algorithms GA= new DWGraph_Algo();
     GA.init(g0);
	     boolean b=GA.isConnected();
     assertTrue(b);
     directed_weighted_graph g1 = new DWGraph_DS();
	    for (int i = 0; i < 4; i++) {
			geoLocation location= new geoLocation(0, 0, i);
	    	nodeData n = new nodeData(i, location);
	    	g1.addNode(n);
		}
	    GA.init(g1);
	    b=GA.isConnected();
	    assertFalse(b);
 }
// @Test
// void shortestPath() {
//	 directed_weighted_graph g0 = new DWGraph_DS();
//	    for (int i = 0; i < 4; i++) {
//			geoLocation location= new geoLocation(0, 0, i);
//	    	nodeData n = new nodeData(i, location);
//	    	g0.addNode(n);
//		}
//		 g0.connect(0,1,1);
//	     g0.connect(0,3,4.0);
//	     g0.connect(3,0,4.0);
//	     g0.connect(1,2,0.1);
//	     g0.connect(1,3,10.0);
//     dw_graph_algorithms GA= new DWGraph_Algo();
//     GA.init(g0);
//     assertEquals(3, GA.shortestPath(0, 2).size());
//     directed_weighted_graph g1 = new DWGraph_DS();
//	    for (int i = 0; i < 4; i++) {
//			geoLocation location= new geoLocation(0, 0, i);
//	    	nodeData n = new nodeData(i, location);
//	    	g1.addNode(n);
//		}
//	    GA.init(g1);
//	     assertEquals(1, GA.shortestPath(0, 0).size());
//}
 @Test
 void save_load() throws FileNotFoundException  {
     directed_weighted_graph g0 = graph_creator(2,3,1);
    dw_graph_algorithms ag0 = new DWGraph_Algo();
     ag0.init(g0);
    // System.out.println();
     String str = "file.json";
     assertTrue(ag0.save(str));
    // directed_weighted_graph g1 =graph_creator(2,3,1);
     //ag0.load(str);
    // assertNotEquals(g0,g1);
//     g0.removeNode(0);
//     assertNotEquals(g0,g1);
//     ag0.load("C:\\Users\\Gil\\OneDrive\\מסמכים\\GitHub\\Ariel_OOP_2020\\Assignments\\Ex2\\data\\A0");
//assertEquals(ag0.shortestPathDist(0, 1), 1);
//assertTrue(ag0.load("C:\\Users\\Gil\\OneDrive\\מסמכים\\GitHub\\Ariel_OOP_2020\\Assignments\\Ex2\\data\\A0.json"));
 }
 

//C:\Users\Gil\OneDrive\מסמכים\GitHub\Ariel_OOP_2020\Assignments\Ex2\data

 private directed_weighted_graph myGraph() {
	 directed_weighted_graph g0 = new DWGraph_DS();
    for (int i = 0; i < 4; i++) {
		geoLocation location= new geoLocation(0, 0, i);
    	nodeData n = new nodeData(i, location);
    	g0.addNode(n);
	}
	 g0.connect(0,1,1);
     g0.connect(0,3,4.0);
     g0.connect(3,0,4.0);
     g0.connect(1,2,0.1);
     g0.connect(1,3,10.0);
     g0.connect(2,10,20.0);
//     g0.connect(2,3,10.0);
//     g0.connect(2,8,14.0);
//     g0.connect(4,7,31.0);
//     g0.connect(4,3,0.1);
//     g0.connect(5,10,4.0);
//     g0.connect(6,5,2.0);
//     g0.connect(6,7,0.5);
//     g0.connect(8,9,1.0);
//     g0.connect(9,10,1.5);
//     g0.connect(9,7,4.0);
//     g0.connect(6,10,1.0);
//     g0.connect(10,2,100.0);
//     g0.connect(9,1,12.0);
     return g0;
 }

public static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
	directed_weighted_graph g = new DWGraph_DS();
	_rnd = new Random(seed);
    for (int i = 0; i < v_size; i++) {
		geoLocation location= new geoLocation(0, 0, i);
	   	nodeData n = new nodeData(i, location);
	   	g.addNode(n);
	}
	int[] nodes = nodes(g);
	while(g.edgeSize() < e_size) {
		int a = nextRnd(0,v_size);
		int b = nextRnd(0,v_size);
		int i = nodes[a];
		int j = nodes[b];
		double w = _rnd.nextDouble();
		g.connect(i,j, w);
	}
	return g;
}
private static int nextRnd(int min, int max) {
	double v = nextRnd(0.0+min, (double)max);
	int ans = (int)v;
	return ans;
}
private static double nextRnd(double min, double max) {
	double d = _rnd.nextDouble();
	double dx = max-min;
	double ans = d*dx+min;
	return ans;
}
	/**
	 * Simple method for returning an array with all the node_data of the graph,
	 * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
	 * @param g
	 * @return
	 */
private static int[] nodes(directed_weighted_graph g) {
	int size = g.nodeSize();
	Collection<node_data> V = g.getV();
	node_data[] nodes = new node_data[size];
	V.toArray(nodes); 
	int[] ans = new int[size];
	for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
	Arrays.sort(ans);
	return ans;
}	
}
