package src;

import src.drawingHelper.drawer;

import java.lang.invoke.SerializedLambda;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyMath {

    public static void main(String[] args){
        /*
        currentPoint = [1  ] * [ 1 , 0 , 0 , 0 ] * [s1, s2, e1, e2]
                       [t  ]   [-3 , 3 , 0 , 0 ]
                       [t^2]   [ 3 ,-6 , 3 , 0 ]
                       [t^3]   [ 1 ,-1 ,-3 , 1 ]
         */

        final Matrix m = new Matrix(new double[][]{
                { 1 , 0 , 0 , 0 },
                {-3 , 3 , 0 , 0 },
                { 3 ,-6 , 3 , 0 },
                { 1 ,-1 ,-3 , 1 }
        });

        Matrix powerMatrix;

        PointMatrix points = new PointMatrix(new double[][][]{
                {},
                {},
                {},
                {}
        });

        generatePowerMatrix(2, 4, true);
    }

    // region working on it
    public static abstract class Test{
        public String name;
        public String toString() {
            return name;
        }

        public void add     (double n) { name += " + " + n; }
        public void subtract(double n) { name += " - " + n; }
        public void multiply(double n) { name += " * " + n; }
        public void divide  (double n) { name += " / " + n; }
    }

    public static class tester extends Test {
        String name;
        public tester(String name){
            this.name = name;
        }

        public String showValue() { return name; }

    }
    // endregion

    public static class Point  {
        public double x, y;

        // region INITIALIZING
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
        public Point(){}
        // endregion

        // region BASIC OPERATION SHORTCUTS
        // region NUMBERS
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
                str.append("[ ");
                for (int i = 0; i < cols; i++){
                    if (rows[i] % 1 == 0) { str.append((int)rows[i]); }
                    else                  { str.append(rows[i]); }

                    if (rows[i] % 1 == 0) { str.append(" ".repeat(longestNum - String.valueOf((int) rows[i]).length() + 1)); }
                    else                  { str.append(" ".repeat(longestNum - String.valueOf(      rows[i]).length() + 1)); }
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

        public Point get(int row, int col){
            return values[row][col];
        }
        public void set(int row, int col, Point value){
            values[row][col] = value;
        }
    }

    public static Matrix generatePowerMatrix(double t, int length, boolean horizontal) {
        Matrix mat;
        if (horizontal){
            mat = new Matrix(length, 1);
            for (int i = 0; i < length; i++) mat.set(i, 0, Math.pow(t, i));
        } else {
            mat = new Matrix(1, length);
            for (int i = 0; i < length; i++) mat.set(0, i, Math.pow(t, i));
        }
        return mat;
    }
}
/*

shortcuts:
end.p1 = e1
end.p2 = e2

start.p1 = s1
start.p2 = s2

known :

get relative pos {
    p1 + (p2 - p1) * t

    p1 + p2 * t - p1 * t
}
e2 = e1 * 2 - e2

E1 = e2;
E2 = e1;

v1.p1 = s1 + s2 * t - s1 * t
v1.p2 = s2 + e1 * t - s2 * t

v2.p1 = s2 + e1 * t - s2 * t
v2.p2 = e1 + e2 * t - e1 * t

v3.p1 = (s1 + s2 * t - s1 * t) + (s2 + e1 * t - s2 * t) * t - (s1 + s2 * t - s1 * t) * t
v3.p2 = (s2 + e1 * t - s2 * t) + (e1 + e2 * t - e1 * t) * t - (s2 + e1 * t - s2 * t) * t

v3.p1 = s1 + s2 * t - s1 * t + s2 * t + e1 * t*t - s2 * t*t - s1*t - s2 * t*t + s1 * t*t
v3.p2 = s2 + e1 * t - s2 * t + e1 * t + e2 * t*t - e1 * t*t - s2*t - e1 * t*t + s2 * t*t


v3.p1 = [1  ] * [s1]
        [t  ]   [2 * (s2 - s1)]
        [t*t]   [e1 + s1 - s2 * 2]

v3.p2 = [1  ] * [s2]
        [t  ]   [2 * (e1 - s2)]
        [t*t]   [e2 + s2 - e1 * 2]


currentPoint = p1 + p2 * t - p1 * t


currentPoint = [1  ] * [s1]
               [t  ]   [3 * (s2 - s1)]
               [t^2]   [e1 + s1 - s2 * 2 + 2 * (e1 - s2) - 2 * (s2 - s1)]
               [t^3]   [e2 + s2 - e1 * 2 - e1 + s1 - s2 * 2]

currentPoint = [1  ] * [s1                    ]
               [t  ]   [3 * (s2 - s1)         ]
               [t^2]   [3 * (e1 + s1) - s2 * 6]
               [t^3]   [e2 - e1 * 3 + s1 - s2 ]

// answer :
currentPoint = [1  ] * [ 1 , 0 , 0 , 0 ] * [s1, s2, e1, e2]
               [t  ]   [-3 , 3 , 0 , 0 ]
               [t^2]   [ 3 ,-6 , 3 , 0 ]
               [t^3]   [ 1 ,-1 ,-3 , 1 ]

 */
