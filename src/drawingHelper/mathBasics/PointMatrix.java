package src.drawingHelper.mathBasics;

import java.util.function.Consumer;

public class PointMatrix {
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

    public void add     (Matrix m) { applyFunction(m, (PointMatrix.tempCouple couple) -> couple.point.add     (couple.num)); }
    public void subtract(Matrix m) { applyFunction(m, (PointMatrix.tempCouple couple) -> couple.point.subtract(couple.num)); }
    public void multiply(Matrix m) { applyFunction(m, (PointMatrix.tempCouple couple) -> couple.point.multiply(couple.num)); }
    public void divide  (Matrix m) { applyFunction(m, (PointMatrix.tempCouple couple) -> couple.point.divide  (couple.num)); }

    private void applyFunction(Matrix m, Consumer<PointMatrix.tempCouple> action){
        if   (rows == m.rows && cols == m.cols) applyToAll (m, action);
        else if (rows == m.rows && m.cols == 1) applyToRows(m, action);
        else if (cols == m.cols && m.rows == 1) applyToCols(m, action);
        else System.out.println("i cant do it");
    }

    private void applyToAll (Matrix m, Consumer<PointMatrix.tempCouple> action){
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                action.accept(new PointMatrix.tempCouple(values[row][col], m.values[row][col]));
            }
        }
    }
    private void applyToRows(Matrix m, Consumer<PointMatrix.tempCouple> action){
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                action.accept(new PointMatrix.tempCouple(values[row][col], m.values[row][0]));
            }
        }
    }
    private void applyToCols(Matrix m, Consumer<PointMatrix.tempCouple> action){
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                action.accept(new PointMatrix.tempCouple(values[row][col], m.values[0][col]));
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
