package src.drawingHelper.mathBasics;

public interface mathObject {
    mathObject minus(mathObject n);
    mathObject plus(mathObject n);
    mathObject times(mathObject n);
    mathObject over(mathObject n);

    void subtract(mathObject n);
    void add     (mathObject n);
    void multiply(mathObject n);
    void divide  (mathObject n);

    mathObject copy();
    void copy(mathObject p);

    String showValue();
}
