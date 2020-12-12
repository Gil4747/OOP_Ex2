package gameClient;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.edge_data;
import api.geoLocation;
import api.geo_location;
import api.nodeData;
import api.node_data;
//import dataStructure.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class Graph_GUI implements Serializable{
    final String res = "update";


    private boolean inAction = false;

    static class Active extends TimerTask {
        Graph_GUI gui;
        boolean press = false;
        geoLocation temp1 = null;
        geoLocation temp2 = null;
        Date date;
        long time;
        boolean update = false;
        public  Active(Graph_GUI graphGui){
            gui = graphGui;
        }
        public synchronized void run(){
            //newLocation(gui);
            if (StdDraw.isMousePressed()&&!press){
                date = new Date();
                time = date.getTime();
                press = true;
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                temp1 = new geoLocation(x,y);
                gui.addPoint(temp1);
                gui.update();
            }
            if (StdDraw.isMousePressed()&&press){
                date = new Date();

            }

            if (!StdDraw.isMousePressed()&&press&&date.getTime()-time>300){
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                temp2 = new geoLocation(x,y);
                gui.addPoint(temp2);
                gui.update();
                gui.addE(gui._id-1,gui._id,0);
                press = false;
                gui.update();

            }
            else if (!StdDraw.isMousePressed()&&press){
                press = false;

            }
            synchronized (gui.res){
            if (!gui.inAction()&&gui.thisMC()) {
                gui.update();
            }

            }
        }
    }

    private boolean inAction() {
        return inAction;
    }

    public  int _id = 0;
    private int mc = -1;

    private DWGraph_DS dGraph;
    private DWGraph_Algo graphAlgo = new DWGraph_Algo();


    public Graph_GUI(){
        draw(1200,800,new Range(-100,100),new Range(-100,100));
        Timer timer = new Timer();
        timer.schedule(new Active(this), 1, 1);

    }
    public Graph_GUI(directed_weighted_graph graph){
        dGraph = (DWGraph_DS) graph;
        draw(1200,800,new Range(nodeData.getMinX(),nodeData.getMaxX()),new Range(nodeData.getMinY(),nodeData.getMaxY()));
        Timer timer = new Timer();
        if (!inAction)
            timer.schedule(new Active(this), 1, 1);

    }

    public void addPoint(geoLocation p){
        nodeData temp = new nodeData(p);
        dGraph.addNode(temp);

    }

    public void addE(int src,int dest, double weight){
        dGraph.connect(src,dest,weight);
    }

    public void draw(int width,int height){
        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(nodeData.getMinX()-10,nodeData.getMaxX()+10);
        StdDraw.setYscale(nodeData.getMinY()-10,nodeData.getMaxY()+10);
        update();
    }
    public void draw(int width, int height, Range x, Range y){
        StdDraw.setCanvasSize(width,height);
        update();
    }

    public synchronized void update() {
        try {

            if (inAction) System.out.println("wird");
            while (inAction) {

            }
            StdDraw.setXscale(nodeData.getMinX()-10,nodeData.getMaxX()+10);
            StdDraw.setYscale(nodeData.getMinY()-10,nodeData.getMaxY()+10);
            inAction = true;
            StdDraw.clear();
            StdDraw.setPenRadius(0.01);
            Iterator<node_data> temp = dGraph.getV().iterator();
            while (temp.hasNext()) {
                node_data n = temp.next();
                StdDraw.setPenColor(Color.blue);
                StdDraw.setPenRadius(0.04);
                StdDraw.point(n.getLocation().x(), n.getLocation().y());
                StdDraw.setPenColor(Color.green);
                StdDraw.setPenRadius(0.02);
                StdDraw.text(n.getLocation().x(), n.getLocation().y(), n.getKey() + "");
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                if (dGraph.getE(n.getKey()) != null) {
                    List<edge_data> myE = new LinkedList<>(dGraph.getE(n.getKey()));
                    for (edge_data edge : myE) {
                        double x0 = n.getLocation().x();
                        double y0 = n.getLocation().y();
                        double y1 = dGraph.getNode(edge.getDest()).getLocation().y();
                        double x1 = dGraph.getNode(edge.getDest()).getLocation().x();
                        StdDraw.setPenRadius(0.003);
                        StdDraw.line(x0, y0, x1, y1);
                        StdDraw.setPenColor(Color.RED);
                        StdDraw.text(0.3 * x0 + 0.7 * x1, 0.3 * y0 + 0.7 * y1, edge.getWeight() + "");
                        StdDraw.setPenRadius(0.03);
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.point(0.1 * x0 + 0.9 * x1, 0.1 * y0 + 0.9 * y1);
                        StdDraw.setPenRadius(0.01);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                }
                _id = n.getKey();

            }
        }catch (Exception e){

        }
            inAction = false;
    }

    public void delete(int key) {
        dGraph.removeNode(key);
    }
    public void delete(int src,int des) {
        dGraph.removeEdge(src,des);
    }

    public static void newLocation(Graph_GUI graph_gui){
        for (int i = 0; i<graph_gui.dGraph.nodeSize();i++){
            double x = graph_gui.dGraph.getNode(i).getLocation().x()+1;
            double y = graph_gui.dGraph.getNode(i).getLocation().y()+1;
            Point3D temp = new Point3D(x,y);
            graph_gui.dGraph.getNode(i).setLocation(temp);
        }
        graph_gui.update();
    }

    public boolean isConected(){
        graphAlgo.init(dGraph);
        return graphAlgo.isConnected();
    }
    public double shortestPathDist(int src, int dest) {
        graphAlgo.init(dGraph);
        return graphAlgo.shortestPathDist(src,dest);
    }
    public List<node_data> shortestPath(int src, int dest) {
        graphAlgo.init(dGraph);
        return graphAlgo.shortestPath(src,dest);
    }
    public void save(String filename){
        graphAlgo.init(dGraph);
        graphAlgo.save(filename);

    }
    public void initGraph(String filename){
        graphAlgo.load(filename);
        dGraph=new DWGraph_DS((DWGraph_DS)(graphAlgo.getGraph()));

    }
    public List<node_data> TSP(List<Integer> targets){
        graphAlgo.init(dGraph);
        return graphAlgo.TSP(targets);
    }
    public boolean thisMC(){
        if (dGraph.getMC()==mc) return false;
        mc = dGraph.getMC();
        return  true;
    }

    private void addNode(node_data n1)
    {
        dGraph.addNode(n1);
    }
public static void main(String[] args) {
	
}

}
