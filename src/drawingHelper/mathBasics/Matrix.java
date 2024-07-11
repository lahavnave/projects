package src.drawingHelper.mathBasics;

import java.util.function.Function;

public class Matrix {
    protected final double[][] values;
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
