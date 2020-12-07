package uet.oop.bomberman.entities.moveEntities.enemy.graph;

import uet.oop.bomberman.entities.Entity;

public class Point {
    public boolean cannotMove = false;
    public int x, y;
    public double f;

    public Point(int x, int y, double f) {
        this.x = x;
        this.y = y;
        this.f = f;
    }

    public boolean equal(Point other) {
        return (x == other.x && y == other.y);
    }
}
