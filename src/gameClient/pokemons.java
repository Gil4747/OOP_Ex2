package gameClient;

import api.*;
import api.DWGraph_Algo;
import org.json.JSONObject;
import gameClient.util.Point3D;
import gameClient.util.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class pokemons {
    DWGraph_DS graph;
    ArrayList<CL_Pokemon> myPokemons = new ArrayList<>();

    /**
     * init Fruits
     * @param game - game after start
     * @param g - graph after init
     */

    public pokemons(game_service game, DWGraph_DS g){
        graph = g;
        int i =0;
        Iterator<CL_Pokemon> f_iter = Arena.json2Pokemons(game.getPokemons()).iterator();
        while (f_iter.hasNext()){
           CL_Pokemon p=f_iter.next();
                double value = p.getValue();
                int type = p.getType();

                Point3D position = new Point3D(p.getLocation().x(),p.getLocation().y());
                CL_Pokemon po = new CL_Pokemon(position,type,value,i,p.get_edge());
                i++;
                myPokemons.add(po);
                po.set_edge((getEdge(po.getId())));

        }
    }

    public pokemons() {

    }


    /**
     * return collection of the fruit in the game
     * @return collection
     */
    public Collection<CL_Pokemon> getmyPokemons() {
        return myPokemons;
    }
    public Iterator<CL_Pokemon> iterator(){
        return myPokemons.iterator();
    }

    /**
     * return the edge of the the fruit
     * @param id - of the Fruit
     * @return the Edge the the fruit is on edge_data(type)
     */
    public edge_data getEdge(int id){
        Iterator<node_data> nodeDataIterator = graph.getV().iterator();
        CL_Pokemon current = getFruit(id);
        while (nodeDataIterator.hasNext()){
            node_data temp = nodeDataIterator.next();
            for (edge_data tempE : graph.getE(temp.getKey())) {
                int src = tempE.getSrc();
                int des = tempE.getDest();
                node_data srcN = graph.getNode(src);
                node_data desN = graph.getNode(des);
                Point3D middle = new Point3D((srcN.getLocation().x() * 0.5 + desN.getLocation().x() * 0.5), (srcN.getLocation().y() * 0.5 + desN.getLocation().y() * 0.5));
//                if (middle.distance2D(current.getPosition())<0.002){
//                    return tempE;
//                }
                try {

                    double distanceA = srcN.getLocation().distance(current.getLocation()) + current.getLocation().distance(desN.getLocation());
                    double distanceB = srcN.getLocation().distance(desN.getLocation());
                    //middle.distance2D(current.getPosition())<0.001
                    if (Math.abs(distanceA - distanceB) <= 0.0000000001) {
                        return tempE;
                    }
                } catch (Exception ignored) {

                }
            }


        }
        return null;

    }

    /**
     * return object fruit
     * @param id - of the fruit
     * @return Fruit by id
     */
    public CL_Pokemon getFruit(int id){
        Iterator<CL_Pokemon> fruitIterator = iterator();
        while (fruitIterator.hasNext()){
            CL_Pokemon temp = fruitIterator.next();
            if (id==temp.getId()){
                return temp;
            }
        }
        return null;
    }

    /**
     * This is one of the methods that help so spare the Fruit between the Robots
     * @return the maximum value between all the fruits
     */
    public CL_Pokemon getMaxValue() {
        Iterator<CL_Pokemon> fruitIterator = iterator();
        if (fruitIterator.hasNext()) {
            CL_Pokemon max = fruitIterator.next();
//            while (!max.isTaken()&&fruitIterator.hasNext()){
//                max = fruitIterator.next();
//            }
            while (fruitIterator.hasNext()){
                CL_Pokemon temp = fruitIterator.next();

                if(max.getValue()<temp.getValue()&&!temp.isTaken()){
                    max = temp;
                }
            }
            // max.take();
            return max;
        }
        return null;
    }
    /**
     * This is one of the methods that help so spare the Fruit between the Robots
     * @return the minimum value between all the fruits
     */
    public CL_Pokemon geMinValue(int fromEnd) {
        Iterator<CL_Pokemon> fruitIterator = iterator();
        int count = 0;
        if (fruitIterator.hasNext()) {
            CL_Pokemon min = fruitIterator.next();
            if (min.isTaken()) {
                while (!min.isTaken() && fruitIterator.hasNext()) {
                    min = fruitIterator.next();
                }
            }
            while (fruitIterator.hasNext()){
                CL_Pokemon temp = fruitIterator.next();

                if(min.getValue()>temp.getValue()&&!temp.isTaken()){
                    min = temp;
                }
            }
            if (count<fromEnd) {
                min.take();
                return geMinValue(fromEnd-1);

            }
            else
                return min;
        }
        return null;
    }

    /**
     * This is one of the methods that help so spare the Fruit between the Robots
     * @param src - int of the id that the Robot is on
     * @param spd - boolean flag to select between "distance" to "time walk"
     * @return Fruit that close to you
     */

    public CL_Pokemon getCloseF(int src,boolean spd){
        node_data nnode = graph.getNode(src);
        DWGraph_Algo graph_algo = new DWGraph_Algo(graph);
        geo_location p = nnode.getLocation();
        Iterator<CL_Pokemon> fruitIterator = iterator();
        if (fruitIterator.hasNext()) {
            CL_Pokemon close = fruitIterator.next();
//            while (!close.isTaken()&&fruitIterator.hasNext()){
//                close = fruitIterator.next();
//            }
            edge_data closeE = getEdge(close.getId());
            while (fruitIterator.hasNext()) {
                CL_Pokemon temp = fruitIterator.next();
                edge_data etepm = getEdge(temp.getId());
                try {
                    if (spd) {
                        if (graph_algo.shortestPathDist(src, closeE.getDest()) > graph_algo.shortestPathDist(src, etepm.getDest())) {
                            close = temp;
                        }
                    }
                    else {
                        if (close.getLocation().distance(p)>temp.getLocation().distance(p)){
                            close=temp;
                        }
                    }

                }catch (Exception e){
                    if (close.getLocation().distance(p)>temp.getLocation().distance(p)){
                        close=temp;
                    }

                }

            }
            //close.take();
            return close;
        }
        return null;

    }
}
