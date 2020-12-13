package gameClient;

import api.*;
//import dataStructure.*;
import org.json.JSONObject;
import gameClient.util.*;

import java.util.*;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


public class Graph_GUI implements Serializable{
    private final String res = "update";


    private volatile boolean inAction = false;

    /**
     * Thread that will repaint the gui.
     */
    static class Active extends TimerTask {
        Graph_GUI gui;
        private   Active(Graph_GUI graphGui){
            gui = graphGui;
        }
        public synchronized void run(){
            gui.agentWalk();
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

    private int _id = 0;
    private int mc = -1;
    private game_service game;

    private DWGraph_DS dGraph;
    private double minX = 35.186, maxX =35.21, minY = 32.1, maxY = 32.11;

    Graph_GUI(DWGraph_DS graph, game_service game){
        try {
            Thread thread = new Thread(() -> {


            });
            thread.start();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        dGraph = (DWGraph_DS) graph;
        this.game = game;
        draw();
        Timer timer = new Timer();
        if (!inAction)
            timer.schedule(new Active(this), 70, 1);

    }


    /**
     * first draw
     */
    private void draw(){
        StdDraw.setCanvasSize(1200, 800);
        StdDraw.setXscale(minX,maxX);
        StdDraw.setYscale(minY,maxY);
        update();
    }

    /**
     * repaint the graph
     */
    private synchronized void update() {
        try {

            while (inAction) {
                Thread.onSpinWait();
            }

            inAction = true;
            StdDraw.setPenRadius(0.01);
            for (node_data n : dGraph.getV()) {
//                if (first){
//                    first=false;
//                    maxX = n.getLocation().x();
//                    maxY = n.getLocation().y();
//                    minX = n.getLocation().x();
//                    minY = n.getLocation().y();
//                    second = true;
//                }
                maxX = Math.max(n.getLocation().x(), maxX);
                maxY = Math.max(n.getLocation().y(), maxY);
                minX = Math.min(n.getLocation().x(), minX);
                minY = Math.min(n.getLocation().y(), minY);
//                System.out.println(minX + " " + maxX);
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
                        // StdDraw.text(0.3 * x0 + 0.7 * x1, 0.3 * y0 + 0.7 * y1, edge.getWeight() + "");
                        StdDraw.setPenRadius(0.03);
                        //StdDraw.setPenColor(StdDraw.RED);
                        //StdDraw.point(0.1 * x0 + 0.9 * x1, 0.1 * y0 + 0.9 * y1);
                        StdDraw.setPenRadius(0.01);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                }

                _id = n.getKey();
                StdDraw.setXscale(minX, maxX);
                StdDraw.setYscale(minY, maxY);
            }
        }catch (Exception ignored){

        }
        inAction = false;
    }

    /**
     * repaint the robots
     */
    private void agentWalk() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.white);
        StdDraw.picture((minX+maxX)/2,(minY+maxY)/2,"res/Backround.png");
        List<CL_Agent> log = Arena.getAgents(game.getAgents(),(directed_weighted_graph) dGraph);
        addPokimon();
        update();
        if (log != null) {

                if (game.isRunning()) {
                    long t = game.timeToEnd();
                    for (CL_Agent ag : log) {
                            int agentId = ag.getID();
                            geo_location pos = ag.getLocation();
                            //Point3D position = new Point3D(Double.parseDouble(pos3D[0]), Double.parseDouble(pos3D[1]));
                            StdDraw.setPenRadius(0.04);
                            //StdDraw.setPenColor(StdDraw.GREEN);
                            double dtx = maxX - minX;
                            double dty = maxY - minY;
                            StdDraw.picture(minX + dtx / 2, maxY - dty / 10, "res/headelin.png");
                            Font font = new Font("Arial", Font.BOLD, 20);
                            StdDraw.setFont(font);
                            StdDraw.setPenColor(Color.BLACK);
                            StdDraw.text(minX + dtx / 2, maxY - dty / 10, "Time left: " + t / 1000 + "." + t % 1000);
                            StdDraw.setFont();
                            if (agentId == 0) {
                                StdDraw.picture(pos.x(), pos.y(), "res/harryR01.png");
                            } else if (agentId == 1) {
                                StdDraw.picture(pos.x(), pos.y(), "res/robotP.png", 0.001, 0.001);
                            } else if (agentId == 2) {
                                StdDraw.picture(pos.x(), pos.y(), "res/wolf.png");

                            } else if (agentId == 3) {
                                StdDraw.picture(pos.x(), pos.y(), "res/robot3.jpg", 0.001, 0.0004);

                            } else if (agentId == 4) {
                                StdDraw.picture(pos.x(), pos.y(), "res/robot4.jpg", 0.0004, 0.0004);

                            }

                    }
                }

        }
        StdDraw.show();

    }

    /**
     * repaint the fruit
     */

    private void addPokimon(){
        Arena a=new Arena();
        for (CL_Pokemon s : Arena.json2Pokemons(game.getPokemons())) {
                int type=s.getType();
                double value = s.getValue();
                double speed=10.0;
                edge_data edge=s.get_edge();
                if (s.getType() == -1) {
                    StdDraw.picture(s.getLocation().x(), s.getLocation().y(), "res/robotH.png", 0.001, 0.001);
                } else {
                    StdDraw.picture(s.getLocation().x(), s.getLocation().y(), "res/mapHarry.png");

                }
            a.getPokemons().add(new CL_Pokemon(s.getLocation(),type,value,speed,edge));
            }
        }




    private boolean thisMC(){
        if (dGraph.getMC()==mc) return false;
        mc = dGraph.getMC();
        return  true;
    }




}
