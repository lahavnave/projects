package src.drawingHelper.mathBasics;

public class Vector {
    public Point p1, p2;

    // region INITIALIZING
    public Vector(Point P1, Point P2){
        p1 = P1;
        p2 = P2;
    }
    public Vector(double x1, double y1, double x2, double y2){
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
    }
    public Vector(Point P1, double angle, double l){
        p1 = P1;
        p2 = new Point(p1.x + l * Math.cos(angle), p1.y + l * Math.sin(angle));
    }
    public Vector(){
        p1 = new Point();
        p2 = new Point();
    }

    public void setByRadian(double x1, double y1, double angle, double l){
        p2.x = x1 + l * Math.cos(angle);
        p2.y = y1 + l * Math.sin(angle);
    }

    public void setLength(double l){
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        p2.x = p1.x + l * Math.cos(angle);
        p2.y = p1.y + l * Math.sin(angle);
    }
    public void setAngle(double angle){
        double l = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        p2.x = p1.x + l * Math.cos(angle);
        p2.y = p1.y + l * Math.sin(angle);
    }
    // endregion

    // region GETTING INFO
    public double getLength(){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    public double getAngle(){
        return Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }

    public static double getLength(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    public static double getAngle(Point p1, Point p2){
        return Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }

    public Point getRelativePos(double t){
        return p1.plus(p2.minus(p1).times(t));
    }
    public static Point getRelativePos(Point p1, Point p2, double t){
        return p1.plus(p2.minus(p1).times(t));
    }

    public double distance(Point p) {
        if      ((getAngle() - Math.atan2(p.y - p1.y, p.x - p1.x) + Math.PI * 3.5) % (Math.PI * 2) < Math.PI) return p1.distance(p);
        else if ((getAngle() - Math.atan2(p.y - p2.y, p.x - p2.x) + Math.PI * 3.5) % (Math.PI * 2) > Math.PI) return p2.distance(p);
        else return Math.abs(Math.sin(getAngle() - getAngle(p1, p)) * getLength(p1, p));
    }

    public Point getOrigin() {
        return p1;
    }
    // endregion

    // region BASIC OPERATION SHORTCUTS
    // region POINTS
    public Vector minus(Point p){
        return new Vector(p1.minus(p), p1.minus(p));
    }
    public Vector plus (Point p){
        return new Vector(p1.plus(p), p1.plus(p));
    }
    public Vector times(Point p){
        return new Vector(p1.times(p), p1.times(p));
    }
    public Vector over (Point p){
        return new Vector(p1.over(p), p1.over(p));
    }

    public void subtract(Point p) {
        p1.subtract(p);
        p2.subtract(p);
    }
    public void add     (Point p) {
        p1.add(p);
        p2.add(p);
    }
    public void multiply(Point p) {
        p1.multiply(p);
        p2.multiply(p);
    }
    public void divide  (Point p) {
        p1.divide(p);
        p2.divide(p);
    }
    // endregion

    // region VECTORS
    public Vector subtract(Vector v){
        return new Vector(p1.minus(v.p1), p1.minus(v.p2));
    }
    public Vector add     (Vector v){
        return new Vector(p1.plus(v.p1), p1.plus(v.p2));
    }
    public Vector multiply(Vector v){
        return new Vector(p1.times(v.p1), p1.times(v.p2));
    }
    public Vector divide  (Vector v){
        return new Vector(p1.over(v.p1), p1.over(v.p2));
    }

    public void subtractBy(Vector v) {
        p1.subtract(v.p1);
        p2.subtract(v.p2);
    }
    public void addBy     (Vector v) {
        p1.add(v.p1);
        p2.add(v.p2);
    }
    public void multiplyBy(Vector v) {
        p1.multiply(v.p1);
        p2.multiply(v.p2);
    }
    public void divideBy  (Vector v) {
        p1.divide(v.p1);
        p2.divide(v.p2);
    }
    // endregion

    public Vector copy(){
        return new Vector(p1.copy(), p2.copy());
    }
    public void copy(Vector v){
        p1.copy(v.p1);
        p2.copy(v.p2);
    }

    public void flip(){
        Point p = p1;
        p1 = p2;
        p2 = p;
    }
    // endregion
}
