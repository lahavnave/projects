package src.drawingHelper.mathBasics;

/**
 * a class containing all the mathematical structure of a Point
 */
public class Point {
    public double x, y;

    // region INITIALIZING
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Point(){}
    // endregion

    // region GETTING INFO
    public double distance(Point p) {
        return Vector.getLength(p, this);
    }

    public Point getOrigin() {
        return this;
    }

    public String showValue() {
        String str = "(";

        if (0 == x % 1) str += Integer.toString((int)x);
        else            str += x;

        str += ", ";

        if (0 == y % 1) str += Integer.toString((int)y);
        else            str += y;

        str += ")";

        return str;
    }
    // endregion

    // region BASIC OPERATION SHORTCUTS
    // region NUMBERS
    public Point minus(double n){
        return new Point(x - n, y - n);
    }
    public Point plus(double n){
        return new Point(x + n, y + n);
    }
    public Point times(double n){
        return new Point(x * n, y * n);
    }
    public Point over(double n){
        return new Point(x / n, y / n);
    }

    public void subtract(double n){
        x -= n;
        y -= n;
    }
    public void add     (double n){
        x += n;
        y += n;
    }
    public void multiply(double n){
        x *= n;
        y *= n;
    }
    public void divide  (double n){
        x /= n;
        y /= n;
    }
    // endregion

    // region POINTS
    public Point minus(Point p){
        return new Point(x - p.x, y - p.y);
    }
    public Point plus (Point p){
        return new Point(x + p.x, y + p.y);
    }
    public Point times(Point p){
        return new Point(x * p.x, y * p.y);
    }
    public Point over (Point p){
        return new Point(x / p.x, y / p.y);
    }

    public void subtract(Point p){
        x -= p.x;
        y -= p.y;
    }
    public void add     (Point p){
        x += p.x;
        y += p.y;
    }
    public void multiply(Point p){
        x *= p.x;
        y *= p.y;
    }
    public void divide  (Point p){
        x /= p.x;
        y /= p.y;
    }
    // endregion

    public Point copy(){
        return new Point(x, y);
    }
    public void copy(Point p){
        x = p.x;
        y = p.y;
    }
    // endregion
}
