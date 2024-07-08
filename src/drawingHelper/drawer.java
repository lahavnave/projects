package src.drawingHelper;

import codedraw.*;

import java.util.ArrayList;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static src.drawingHelper.Colors.*;

/**
 * A class that has access to its own canvas and all the drawable classes
 */
public abstract class drawer {

    // region VARIABLES
    public static final CodeDraw canvas = new CodeDraw(700, 700);
    public static final int WIDTH = canvas.getWidth();
    public static final int HEIGHT = canvas.getHeight();
    public static final Color backgroundColor = new Color(255, 255, 255, 75);

    static {
        setLineWidth(3);
        canvas.getTextFormat().setTextOrigin(TextOrigin.CENTER);
        canvas.getTextFormat().setBold(true);
    }

    public static final Point mouse = new Point();
    public static boolean mouseIsPressed = false;
    // endregion

    // region FUNCTIONS
    public static void setLineWidth(double width){
        canvas.setLineWidth(Math.max(1, width));
    }
    public static void setColor(int r, int g, int b, int a) { canvas.setColor(new Color(r, g, b, a)); }
    public static void setColor(Color color) { canvas.setColor(color); }
    public static void showChanges() {
        canvas.show();
        setColor(backgroundColor);
        canvas.fillRectangle(0, 0, WIDTH, HEIGHT);
    }

    public static final Thread mouseUpdater = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()){
            refreshMouse();
        }
    });

    public static void refreshMouse(){
        for (var e : canvas.getEventScanner()) {
            if (e instanceof MouseMoveEvent a) {
                mouse.x = a.getX();
                mouse.y = a.getY();
            } else if (e instanceof MouseDownEvent){
                mouseIsPressed = true;
            } else if (e instanceof MouseUpEvent){
                mouseIsPressed = false;
            }
        }
    }
    // endregion

    /**
     * an object that can be drawn onto the canvas
     */
    public static abstract class drawable {
        // region VARIABLES
        public Color defaultColor = BLACK;
        public Color color        = BLACK;
        // endregion

        abstract void draw();
    }

    public static class Point  extends drawable {
        public double x, y;

        // region INITIALIZING
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
        public Point(){}
        // endregion

        public void draw(){
            drawer.setColor(color);
            canvas.fillCircle(x, y, 4);
        }

        // region CHANGEABLE REQUIRED
        public double distance(Point p) {
            return Vector.getLength(p, this);
        }

        public void move(Point p) {
            copy(p);
        }

        public Point getOrigin() {
            return this;
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

        public String showValue() {
            return "(" + ((0 == x % 1) ? Integer.toString((int)x): x) + ", " + ((0 == y % 1) ? Integer.toString((int)y): y) + ")";
        }
    }
    public static class Vector extends drawable {
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

        // region DRAWING
        public void draw(){
            drawer.setColor(color);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        public static void draw(Point p1, Point p2){
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        public void draw(double width){
            drawer.setColor(color);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        public static void draw(Point p1, Point p2, double width){
            setLineWidth(width);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        public void fancyDraw(){
            // draw the main "stick"
            draw();

            // the two points needed for the arrow head
            Point newP1 = new Point();
            Point newP2 = new Point();

            // region CALCULATING THE POINT CORDS
            newP1.x = p2.x + Math.sin(getAngle() - 0.75 * Math.PI) * getLength() / 6;
            newP1.y = p2.y - Math.cos(getAngle() - 0.75 * Math.PI) * getLength() / 6;

            newP2.x = p2.x + Math.sin(getAngle() - 0.25 * Math.PI) * getLength() / 6;
            newP2.y = p2.y - Math.cos(getAngle() - 0.25 * Math.PI) * getLength() / 6;
            // endregion

            // drawing the arrow head
            draw(newP1, p2);
            draw(newP2, p2);
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
        // endregion

        // region CHANGEABLE REQUIRED
        public double distance(Point p) {
            if      ((getAngle() - Math.atan2(p.y - p1.y, p.x - p1.x) + Math.PI * 3.5) % (Math.PI * 2) < Math.PI) return p1.distance(p);
            else if ((getAngle() - Math.atan2(p.y - p2.y, p.x - p2.x) + Math.PI * 3.5) % (Math.PI * 2) > Math.PI) return p2.distance(p);
            else return Math.abs(Math.sin(getAngle() - getAngle(p1, p)) * getLength(p1, p));
        }

        public void move(Point p) {
            Point dif = p2.minus(p1);

            p1.copy(p.minus(ChangeManager.startingPoint));

            p2.copy(p1.plus(dif));
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
    public static class Circle extends drawable {
        public double r;
        public Point center;

        // region INITIALIZING
        public Circle(Point center, double r){
            this.r = r;
            this.center = center;
        }
        public Circle(double centerX, double centerY, double r){
            this.r = r;
            this.center = new Point(centerX, centerY);
        }
        // endregion

        public void draw(){
            drawer.setColor(color);
            canvas.drawCircle(center.x, center.y, r);
        }

        // region CHANGEABLE REQUIRED
        public double distance(Point p) {
            return Math.abs(Vector.getLength(p, center) - r);
        }

        public void move(Point p) {
            r = center.distance(p);
        }

        public Point getOrigin() {
            return center;
        }
        // endregion
    }
    public static class Spline extends drawable {
        enum splineType {
            pointOriented,
            vectorOriented
        }
        public splineType type;
        private int jumpSize;
        public boolean isLoopAble; // TODO
        public Point[] directors;
        public Matrix characteristicsMatrix;
        public double resolution = 1.0 / 50;
        // TODO
        /*
        public **** isClosed = false;
        public Type type;
        enum Type{
            vectorGuided,
            pointGuided
        }
         */

        // region INITIALIZING
        public Spline(Vector[] directors){
            this.directors = new Point[directors.length * 2];
            for (int i = 0; i < directors.length; i++){
                this.directors[i * 2] = directors[i].p1;
                this.directors[i*2+1] = directors[i].p2;
            }
            type = splineType.vectorOriented;
            characteristicsMatrix = generateCharacteristicsMatrix(0.5);
        }
        public Spline(Point[] directors){
            this.directors = directors;
            type = splineType.pointOriented;
            characteristicsMatrix = generateCharacteristicsMatrix(0.5);
        }

        private Matrix generateCharacteristicsMatrix(double s){
            if        (type == splineType.pointOriented) {
                jumpSize = 1;
                return new Matrix(new double[][]{
                        {   0,     1,       0,  0 },
                        {  -s,     0,       s,  0 },
                        { 2*s, s - 3, 3 - 2*s, -s },
                        {  -s, 2 - s,   s - 2,  s }
                });
            } else if (type == splineType.vectorOriented) {
                jumpSize = 2;
                return new Matrix(new double[][]{
                        {      1,     0,      0,    0 },
                        {   -6*s,   6*s,      0,    0 },
                        { 12*s-3, -12*s,  6*s+3, -6*s },
                        {  2-6*s,   6*s, -6*s-2,  6*s }
                });
            } else {
                return null;
            }
        }
        // endregion

        // region DRAWING
        public void fancyDraw(){
            draw();

            if (type == splineType.vectorOriented){
                for (int i = 0; i < directors.length; i += 2){
                    new Vector(directors[i], directors[i + 1]).fancyDraw();
                }
            }
            if (type == splineType.pointOriented){
                for (Point director : directors) {
                    director.draw();
                }
            }
        }

        public void draw(){
            drawer.setColor(color);
            for (int i = 0; i < directors.length - 3; i += jumpSize) {
                drawSegment(directors[i], directors[i + 1], directors[i + 2], directors[i + 3], resolution);
            }
        }

        public void drawSegment(Point p1, Point p2, Point p3, Point p4, double resolution){
            Point lastPoint;
            if (type == splineType.pointOriented) lastPoint = p2;
            else                                  lastPoint = p1;

            Point currentPoint;

            for (double t = 0; t <= 1; t += resolution) {
                currentPoint = calculatePointPosition(p1, p2, p3, p4, t);

                canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
                lastPoint = currentPoint;
            }

            canvas.drawLine(lastPoint.x, lastPoint.y, p3.x, p3.y);
        }

        public Point calculatePointPosition(Point p1, Point p2, Point p3, Point p4, double t){

            Matrix powerMatrix;
            Matrix transformationMatrix;

            powerMatrix = Matrix.generatePowerMatrix(t, 4, false);
            PointMatrix points = new PointMatrix(new Point[][]{
                    {p1, p2, p3, p4}
            });
            transformationMatrix = characteristicsMatrix.times(powerMatrix).sumRows();


            points.multiply(transformationMatrix);
            return points.sumAll();
        }
        // endregion

        // region CHANGEABLE REQUIRED
        public double distance(Point p1, Point p2, Point p3, Point p4, double resolution, Point p){
            double shortestDist = p1.distance(p);

            for (double t = 0; t <= 1; t += resolution) {
                shortestDist = Math.min(calculatePointPosition(p1, p2, p3, p4, t).distance(p), shortestDist);
            }

            return shortestDist;
        }

        public double distance(Point p) {
            double shortestDist = distance(directors[0], directors[1], directors[2], directors[3], resolution, p);
            for (int segment = jumpSize; segment < directors.length - 3; segment += jumpSize) {
                shortestDist = Math.min(distance(directors[segment], directors[segment + 1], directors[segment + 2], directors[segment + 3], resolution, p), shortestDist);
            }
            return shortestDist;
        }

        public void move(Point p) {
            for (int i = 1; i < directors.length; i++) {
                directors[i].x += -getOrigin().x + p.x - ChangeManager.startingPoint.x;
                directors[i].y += -getOrigin().y + p.y - ChangeManager.startingPoint.y;
            }
            directors[0].x += -getOrigin().x + p.x - ChangeManager.startingPoint.x;
            directors[0].y += -getOrigin().y + p.y - ChangeManager.startingPoint.y;
        }

        public Point getOrigin() {
            return directors[0];
        }
        // endregion
    }


    /**
     * the class responsible for keeping track of the changeable objects and the mouses interaction
     */
    public static abstract class ChangeManager {
        // region VARIABLES
        protected final static double range = 30;

        public final static ArrayList<Changeable> instances = new ArrayList<Changeable>();
        public static Changeable currentInstance;
        public static final Point startingPoint = new Point();
        // endregion

        private static int chooseClosestInstance(){
            // if there are no changeable objects on the screen
            if (instances.size() == 0) return -1;

            // region GET THE CLOSEST CHANGEABLE
            int closestIndex = 0;
            Changeable closest = instances.get(0);
            Changeable current;
            for(int i = 0; i < instances.size(); i++){
                current = instances.get(i);
                if (current.distance(mouse) < closest.distance(mouse) ||
                        (current.distance(mouse) == closest.distance(mouse) && current.getClass() == ChangeablePoint.class)) {
                    closest = current;
                    closestIndex = i;
                }
            }
            // endregion

            // if the closest point is close enough return it, else return -1
            if (closest.distance(mouse) < range) return closestIndex;
            else                                 return -1;
        }

        public static void update() {
            // if we are not moving anything currently
            if (currentInstance == null) {
                // the closest point that is in range
                int chosenInstance = chooseClosestInstance();
                // if there is a point
                if (chosenInstance != -1) {
                    // make it red and show it
                    Color chosenInstanceColor = instances.get(chosenInstance).getColor();
                    instances.get(chosenInstance).setCurrentColor(RED);
                    instances.get(chosenInstance).draw();
                    instances.get(chosenInstance).setCurrentColor(chosenInstanceColor);

                    // if the mouse is pressed "choose" the point
                    if (mouseIsPressed) {
                        currentInstance = instances.get(chosenInstance);
                        currentInstance.setCurrentColor(RED);

                        startingPoint.copy(mouse.minus(currentInstance.getOrigin()));
                    }
                }
            } else {
                // if we still want to move it
                if (mouseIsPressed){
                    // move it and show it
                    currentInstance.move(mouse);
                    currentInstance.draw();
                } else {
                    // release the point
                    currentInstance.resetColor();
                    currentInstance = null;
                }
            }
        }
    }


    /**
     * an object that can be interacted with using the mouse
     */
    public interface Changeable {
        void setDefaultColor(Color color);
        void setCurrentColor(Color color);
        void resetColor();
        Color getColor();

        void draw();

        void move(Point p);

        Point getOrigin();

        double distance(Point p);

    }

    public static class ChangeablePoint  extends Point  implements Changeable {
        public ChangeablePoint(double x, double y){
            super(x, y);
            ChangeManager.instances.add(this);
        }


        public void setDefaultColor(Color color) {
            this.defaultColor = color;
        }
        public void setCurrentColor(Color color) {
            this.color = color;
        }
        public void resetColor() {
            color = defaultColor;
        }
        public Color getColor(){
            return color;
        }
    }
    public static class ChangeableVector extends Vector implements Changeable {
        public ChangeableVector(double x1, double y1, double x2, double y2){
            super(new ChangeablePoint(x1, y1), new ChangeablePoint(x2, y2));
            if (p1.getClass() == ChangeablePoint.class) ChangeManager.instances.add((ChangeablePoint) p1);
            if (p2.getClass() == ChangeablePoint.class) ChangeManager.instances.add((ChangeablePoint) p2);
            ChangeManager.instances.add(this);
        }
        public ChangeableVector(ChangeablePoint p1, ChangeablePoint p2){
            super(p1, p2);
            ChangeManager.instances.add(this);
        }
        public ChangeableVector(ChangeablePoint p1, double angle, double l){
            super(p1, new ChangeablePoint(p1.x + l * Math.cos(angle), p1.y + l * Math.sin(angle)));
            ChangeManager.instances.add(this);
        }
        public ChangeableVector(Point p1, Point p2){
            super(p1, p2);
        }
        public ChangeableVector(Point p1, double angle, double l){
            super(p1, new Point(p1.x + l * Math.cos(angle), p1.y + l * Math.sin(angle)));
            ChangeManager.instances.add((Changeable) p2);
        }

        public void setDefaultColor(Color color) {
            this.defaultColor = color;
        }
        public void setCurrentColor(Color color) {
            this.color = color;
        }
        public void resetColor() {
            color = defaultColor;
        }
        public Color getColor(){
            return color;
        }
    }
    public static class ChangeableCircle extends Circle implements Changeable {
        public ChangeableCircle(ChangeablePoint center, double r){
            super(center, r);
            ChangeManager.instances.add(this);
        }
        public ChangeableCircle(Point center, double r){
            super(center, r);
            ChangeManager.instances.add(this);
        }
        public ChangeableCircle(double centerX, double centerY, double r){
            super(new ChangeablePoint(centerX, centerY), r);
            ChangeManager.instances.add(this);
        }

        public void setDefaultColor(Color color) {
            this.defaultColor = color;
        }
        public void setCurrentColor(Color color) {
            this.color = color;
        }
        public void resetColor() {
            color = defaultColor;
        }
        public Color getColor(){
            return color;
        }
    }
    public static class ChangeableSpline extends Spline implements Changeable {
        public ChangeableSpline(Vector[] directors){
            super(directors);
            for (Vector v : directors){
                if (v.getClass() != ChangeableVector.class){
                    return;
                }
            }
            ChangeManager.instances.add(this);
        }
        public ChangeableSpline(Point[] directors){
            super(directors);
            for (Point p : directors){
                if (p.getClass() != ChangeablePoint.class){
                    return;
                }
            }
            ChangeManager.instances.add(this);
        }

        public void setDefaultColor(Color color) {
            this.defaultColor = color;
        }
        public void setCurrentColor(Color color) {
            this.color = color;
        }
        public void resetColor() {
            color = defaultColor;
        }
        public Color getColor(){
            return color;
        }
    }

    public static class Button extends Circle {

        public Thread function = new Thread(() -> {});
        public String text = "";

        public static ArrayList<Button> buttons = new ArrayList<Button>();
        public static boolean mouseWasPressedLastTime = false;

        public static Thread updater = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                if (mouseIsPressed){
                    if (!mouseWasPressedLastTime){
                        for (Button b : buttons){
                            if (b.contains(mouse)){
                                b.function.run();
                                b.color = RED;
                            } else {
                                b.color = b.defaultColor;
                            }
                        }
                    }
                }
                mouseWasPressedLastTime = mouseIsPressed;
            }
        });

        // region INITIALIZE
        public Button(Point center, double r) {
            super(center, r);
            buttons.add(this);
        }
        public Button(double centerX, double centerY, double r) {
            super(centerX, centerY, r);
            buttons.add(this);
        }

        public Button(Point center, double r, String text) {
            super(center, r);
            this.text = text;
            buttons.add(this);
        }
        public Button(double centerX, double centerY, double r, String text) {
            super(centerX, centerY, r);
            this.text = text;
            buttons.add(this);
        }

        public Button(Point center, double r, Color color, String text) {
            super(center, r);
            this.text = text;
            this.defaultColor = color;
            this.color = color;
            buttons.add(this);
        }
        public Button(double centerX, double centerY, double r, Color color, String text) {
            super(centerX, centerY, r);
            this.text = text;
            this.defaultColor = color;
            this.color = color;
            buttons.add(this);
        }
        // endregion

        public void draw(){
            drawer.setColor(color);
            canvas.fillCircle(center.x, center.y, r);
            drawer.setColor(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
            canvas.drawText(center.x, center.y, text);
        }

        public boolean contains(Point p){
            return center.distance(p) < r;
        }
    }


    public static class Matrix {
        private final double[][] values;
        public int rows, cols;

        // region INITIALIZATION
        public Matrix(int rows, int cols){
            this.rows = rows;
            this.cols = cols;
            values = new double[rows][cols];
        }
        public Matrix(double[][] arr){
            rows = arr.length;
            cols = arr[0].length;

            values = new double[rows][cols];

            for (int row = 0; row < rows; row++){
                System.arraycopy(arr[row], 0, values[row], 0, arr[0].length);
            }
        }
        public Matrix(Matrix m){
            rows = m.rows;
            cols = m.cols;

            values = new double[rows][cols];

            for (int row = 0; row < rows; row++){
                System.arraycopy(m.values[row], 0, values[row], 0, m.values[0].length);
            }
        }
        // endregion

        // region MATH
        // region NUMBERS
        public void add     (double n) { applyToAll((Double number) -> number + n); }
        public void subtract(double n) { applyToAll((Double number) -> number - n); }
        public void multiply(double n) { applyToAll((Double number) -> number * n); }
        public void divide  (double n) { applyToAll((Double number) -> number / n); }

        private void applyToAll(Function<Double, Double> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    values[row][col] = action.apply(values[row][col]);
                }
            }
        }
        // endregion

        // region MATRICES
        public void add     (Matrix m) { applyFunction(m, (Double[] n) -> n[0] + n[1]); }
        public void subtract(Matrix m) { applyFunction(m, (Double[] n) -> n[0] - n[1]); }
        public void multiply(Matrix m) { applyFunction(m, (Double[] n) -> n[0] * n[1]); }
        public void divide  (Matrix m) { applyFunction(m, (Double[] n) -> n[0] / n[1]); }

        public Matrix plus (Matrix m) {
            Matrix tempMat = copy();
            tempMat.add(m);
            return tempMat;
        }
        public Matrix minus(Matrix m) {
            Matrix tempMat = copy();
            tempMat.subtract(m);
            return tempMat;
        }
        public Matrix times(Matrix m) {
            Matrix tempMat = copy();
            tempMat.multiply(m);
            return tempMat;
        }
        public Matrix over (Matrix m) {
            Matrix tempMat = copy();
            tempMat.divide(m);
            return tempMat;
        }

        private void applyFunction(Matrix m, Function<Double[], Double> action){
            if   (rows == m.rows && cols == m.cols) applyToAll (m, action);
            else if (rows == m.rows && m.cols == 1) applyToRows(m, action);
            else if (cols == m.cols && m.rows == 1) applyToCols(m, action);
            else System.out.println("i cant do it");
        }

        private void applyToAll (Matrix m, Function<Double[], Double> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    values[row][col] = action.apply(new Double[] {values[row][col], m.values[row][col]});
                }
            }
        }
        private void applyToRows(Matrix m, Function<Double[], Double> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    values[row][col] = action.apply(new Double[] {values[row][col], m.values[row][0]});
                }
            }
        }
        private void applyToCols(Matrix m, Function<Double[], Double> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    values[row][col] = action.apply(new Double[] {values[row][col], m.values[0][col]});
                }
            }
        }
        // endregion
        // endregion

        public String showValue(){
            int longestNum = 0;
            for (double[] rows : values){
                for (double value : rows){
                    longestNum = Math.max(longestNum, String.valueOf(value % 1 == 0 ? String.valueOf((int)value) : value).length());
                }
            }

            StringBuilder str = new StringBuilder();
            for (double[] rows : values){
                str.append("[");
                if (rows[0] >= 0){
                    str.append(" ");
                }
                for (int i = 0; i < cols; i++){
                    if (rows[i] % 1 == 0) { str.append((int)rows[i]); }
                    else                  { str.append(rows[i]); }

                    if (rows[i] % 1 == 0) { str.append(" ".repeat(longestNum - String.valueOf(Math.abs((int) rows[i])).length() + ((i + 1 != cols && rows[i + 1] < 0) ? 0: 1))); }
                    else                  { str.append(" ".repeat(longestNum - String.valueOf(Math.abs(      rows[i])).length() + ((i + 1 != cols && rows[i + 1] < 0) ? 0: 1))); }
                }
                str.append("]\n");
            }
            return str.toString();
        }

        public double sumAll(){
            double sum = 0;
            for (int row = 0; row < rows; row++) for (int col = 0; col < cols; col++) sum += values[row][col];
            return sum;
        }
        public Matrix sumRows(){
            Matrix mat = new Matrix(1, cols);
            for(int col = 0; col < cols; col++){
                mat.set(0, col, 0);
                for (int row = 0; row < rows; row++){
                    mat.set(0, col, get(row, col) + mat.get(0, col));
                }
            }
            return mat;
        }
        public Matrix sumCols(){
            Matrix mat = new Matrix(rows, 1);
            for(int row = 0; row < rows; row++){
                mat.set(row, 0, 0);
                for (int col = 0; col < cols; col++){
                    mat.set(row, 0, get(row, col) + mat.get(row, 0));
                }
            }
            return mat;
        }

        public double get(int row, int col){
            return values[row][col];
        }
        public void set(int row, int col, double value){
            values[row][col] = value;
        }

        public Matrix copy(){
            return new Matrix(this);
        }

        public static Matrix generatePowerMatrix(double t, int length, boolean horizontal) {
            Matrix mat;
            if (horizontal){
                mat = new Matrix(1, length);
                for (int i = 0; i < length; i++) mat.set(0, i, Math.pow(t, i));
            } else {
                mat = new Matrix(length, 1);
                for (int i = 0; i < length; i++) mat.set(i, 0, Math.pow(t, i));
            }
            return mat;
        }
    }
    public static class PointMatrix {
        private final Point[][] values;
        public int rows, cols;

        // region INITIALIZATION
        public PointMatrix(int rows, int cols){
            this.rows = rows;
            this.cols = cols;
            values = new Point[rows][cols];
        }
        public PointMatrix(double[][][] arr){
            rows = arr.length;
            cols = arr[0].length;

            values = new Point[rows][cols];

            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    values[row][col] = new Point(arr[row][col][0], arr[row][col][1]);
                }
            }
        }
        public PointMatrix(Point[][] arr){
            rows = arr.length;
            cols = arr[0].length;

            values = new Point[rows][cols];

            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    values[row][col] = arr[row][col].copy();
                }
            }
        }
        public PointMatrix(PointMatrix m){
            rows = m.rows;
            cols = m.cols;

            values = new Point[rows][cols];

            for (int row = 0; row < rows; row++){
                System.arraycopy(m.values[row], 0, values[row], 0, m.values[0].length);
            }
        }
        // endregion

        // region MATH
        // region NUMBERS
        public void add     (double n) { applyToAll((Point point) -> point.add     (n)); }
        public void subtract(double n) { applyToAll((Point point) -> point.subtract(n)); }
        public void multiply(double n) { applyToAll((Point point) -> point.multiply(n)); }
        public void divide  (double n) { applyToAll((Point point) -> point.divide  (n)); }

        private void applyToAll(Consumer<Point> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    action.accept(values[row][col]);
                }
            }
        }
        // endregion

        // region MATRICES
        static class tempCouple{
            Point point;
            double num;

            tempCouple(Point point, double num){
                this.point  = point;
                this.num = num;
            }
        }

        public void add     (Matrix m) { applyFunction(m, (tempCouple couple) -> couple.point.add     (couple.num)); }
        public void subtract(Matrix m) { applyFunction(m, (tempCouple couple) -> couple.point.subtract(couple.num)); }
        public void multiply(Matrix m) { applyFunction(m, (tempCouple couple) -> couple.point.multiply(couple.num)); }
        public void divide  (Matrix m) { applyFunction(m, (tempCouple couple) -> couple.point.divide  (couple.num)); }

        private void applyFunction(Matrix m, Consumer<tempCouple> action){
            if   (rows == m.rows && cols == m.cols) applyToAll (m, action);
            else if (rows == m.rows && m.cols == 1) applyToRows(m, action);
            else if (cols == m.cols && m.rows == 1) applyToCols(m, action);
            else System.out.println("i cant do it");
        }

        private void applyToAll (Matrix m, Consumer<tempCouple> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    action.accept(new tempCouple(values[row][col], m.values[row][col]));
                }
            }
        }
        private void applyToRows(Matrix m, Consumer<tempCouple> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    action.accept(new tempCouple(values[row][col], m.values[row][0]));
                }
            }
        }
        private void applyToCols(Matrix m, Consumer<tempCouple> action){
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    action.accept(new tempCouple(values[row][col], m.values[0][col]));
                }
            }
        }
        // endregion
        // endregion

        public String showValue(){
            int longestPoint = 0;
            for (Point[] rows : values){
                for (Point value : rows){
                    longestPoint = Math.max(longestPoint, value.showValue().length());
                }
            }

            StringBuilder str = new StringBuilder();
            for (Point[] rows : values){
                str.append("[ ");
                for (int i = 0; i < cols; i++){
                    str.append(rows[i].showValue());

                    str.append(" ".repeat(longestPoint - rows[i].showValue().length() + 1));
                }
                str.append("]\n");
            }
            return str.toString();
        }

        public Point sumAll(){
            Point sum = new Point(0, 0);
            for (int row = 0; row < rows; row++) for (int col = 0; col < cols; col++) sum.add(values[row][col]);
            return sum;
        }
        public PointMatrix sumRows(){
            PointMatrix mat = new PointMatrix(1, cols);
            for(int col = 0; col < cols; col++){
                mat.set(0, col, new Point(0, 0));
                for (int row = 0; row < rows; row++){
                    mat.get(0, col).add(get(row, col));
                }
            }
            return mat;
        }
        public PointMatrix sumCols(){
            PointMatrix mat = new PointMatrix(rows, 1);
            for(int row = 0; row < rows; row++){
                mat.set(row, 0, new Point(0, 0));
                for (int col = 0; col < cols; col++){
                    mat.get(row, 0).add(get(row, col));
                }
            }
            return mat;
        }

        public Point get(int row, int col){
            return values[row][col];
        }
        public void set(int row, int col, Point value){
            values[row][col] = value;
        }
    }
}