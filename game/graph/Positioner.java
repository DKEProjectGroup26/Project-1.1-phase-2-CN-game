package game.graph;

import game.useful.Tools;
import game.visual.Board;

import java.awt.Point;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class Positioner {
    public static void createCoords(GraphData data, Board board) {
        class PhysicsSimulation extends Thread {
            private GraphData data;
            private int iterations;
            public boolean running = true;
            public PhysicsSimulation(GraphData d, int i) {
                data = d;
                iterations = i;
            }
            
            public void run() {
                var timer = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        running = false;
                    }
                });
                timer.setRepeats(false);
                timer.start();
                
                for (; iterations >= 0; iterations++) {
                    if (!running)
                        return;
                    
                    iteratePhysics(data);
                    if (iterations % 10 == 0)
                        board.repaint();
                }
            }
        }
        
        for (Node node : data.nodes) {
            node.x = Math.random();
            node.y = Math.random();
        }
        
        PhysicsSimulation simulation = null;
        
        simulation = new PhysicsSimulation(data, 50_000); // how many times to run physics
        simulation.start();
        
        normalizeCoords(data); // makes range [0, 1]
    }
    
    private static void normalizeCoords(GraphData data) {
        var min = new Point.Double(data.nodes[0].x, data.nodes[0].y);
        var max = new Point.Double(data.nodes[0].x, data.nodes[0].y);
        
        for (Node node : data.nodes) {
            // one iteration is redundant
            if (node.x < min.x) min.x = node.x;
            if (node.x > max.x) max.x = node.x;
            if (node.y < min.y) min.y = node.y;
            if (node.y > max.y) max.y = node.y;
        }
        
        for (Node node : data.nodes) {
            node.x = Tools.range(node.x, min.x, max.x, 0, 1);
            node.y = Tools.range(node.y, min.y, max.y, 0, 1);
            
            if (node.x < 0 || node.x > 1) {
                System.err.println("x out of bounds");
                node.x = Math.random();
            }
            if (node.y < 0 || node.y > 1) {
                System.err.println("y out of bounds");
                node.y = Math.random();
            }
        }
    }
    
    public static void iteratePhysics(GraphData data) {
        var forces = generateForces(data);
        
        for (int i = 0; i < data.nodes.length; i++) {
            data.nodes[i].x += forces[i].x;
            data.nodes[i].y += forces[i].y;
            
            data.nodes[i].lastForce = forces[i];
        }
        
        normalizeCoords(data);
    }
    
    private static void normalize(Point.Double[] forces) {
        double maxForce = 0.0005;
        
        double maxVectorLength = 0;
        
        for (Point.Double force : forces) {
            double vectorLength = Math.sqrt(force.x*force.x + force.y*force.y);
            if (vectorLength > maxVectorLength) maxVectorLength = vectorLength;
        }
        
        if (maxVectorLength < maxForce)
            return; // don't increase the forces if they're low
        
        for (Point.Double force : forces) {
            force.x = Tools.range(force.x, 0, maxVectorLength, 0, maxForce);
            force.y = Tools.range(force.y, 0, maxVectorLength, 0, maxForce);
        }
    }
    
    // maybe find point of lowest potential or lowest force
    private static Point.Double[] generateForces(GraphData data) {
        var forces = new Point.Double[data.nodes.length];
        for (int i = 0; i < forces.length; i++) forces[i] = new Point.Double(0, 0);
        
        double maxNodeAttraction = 0;
        double maxNodeRepulsion = 0;
        
        for (int i = 0; i < data.nodes.length; i++) {
            var node = data.nodes[i];
            
            // linked node attraction
            for (Node neighbor : node.myNodes) {
                var force = getForce(node.point(), neighbor.point(), 300, 1, 1);
                forces[i].x += force.x;
                forces[i].y += force.y;
            }
            
            // all node repulsion
            for (Node other : data.nodes) {
                if (other == node) continue;
                var force = getForce(node.point(), other.point(), 1, -2, -1);
                forces[i].x += force.x;
                forces[i].y += force.y;
            }
            
            // standard border repulsion
            // double t;
            // t = 1 / node.x;
            // forces[i].x += Double.isFinite(t) ? t : 1000;
            // t = 1 / (1 - node.x);
            // forces[i].x -= Double.isFinite(t) ? t : 1000;
            // t = 1 / node.y;
            // forces[i].y += Double.isFinite(t) ? t : 1000;
            // t = 1 / (1 - node.y);
            // forces[i].y -= Double.isFinite(t) ? t : 1000;
            
            // // center repulsion (spread)
            // var force = getForce(node.point(), new Point.Double(0.5, 0.5), k, 1, -1);
            // forces[i].x += force.x;
            // forces[i].y += force.y;
        }
        
        normalize(forces);
        return forces;
    }
    
    private static Point.Double getForce(Point.Double point, Point.Double to, double k, int p, int sign) {
        // k = coefficient
        // p = power (will always be absolute)
        // sign = 1 for attraction, -1 for repulsion
        double strength = k * Math.pow(point.distance(to), p);
        
        var force = new Point.Double(
            -sign * Double.compare(point.x, to.x) * strength,
            -sign * Double.compare(point.y, to.y) * strength
        );
        return force;
    }
}