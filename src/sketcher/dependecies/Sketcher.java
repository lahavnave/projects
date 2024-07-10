package src.sketcher.dependecies;

import src.drawingHelper.drawer;

public class Sketcher extends drawer{

    Point origin;
    public double l, r, a1, a2, minAngle;

    public Sketcher(double x, double y, double l, double r, double a1, double a2, double minAngle){
        this.origin = new Point(x, y, true);
        this.l  = l ;
        this.r  = r ;
        this.a1 = a1;
        this.a2 = a2;
        this.minAngle = minAngle;
    }

    public void fullDraw(){
        Vector l1 = new Vector(origin, a1, l, true);
        Vector l2 = new Vector(origin, a2, l, true);

        double d = Vector.getLength(l1.p2, l2.p2);
        double avgAngle = ((l1.getAngle() + l2.getAngle()) % (Math.PI * 2)) / 2;

        Vector r1 = new Vector(l1.p2, avgAngle + Math.acos(d * 0.5 / r) - Math.PI / 2 * (l1.getAngle() > l2.getAngle() ? 1:-1), r, true);
        Vector r2 = new Vector(l2.p2, avgAngle - Math.acos(d * 0.5 / r) + Math.PI / 2 * (l1.getAngle() > l2.getAngle() ? 1:-1), r, true);
        l1.draw();
        l2.draw();
        r1.draw();
        r2.draw();

        r2.p2.draw();
    }

    public void drawEndPoint(){
        Vector l1 = new Vector(origin, a1, l, true);
        Vector l2 = new Vector(origin, a2, l, true);

        double d = Vector.getLength(l1.p2, l2.p2);
        double avgAngle = ((l1.getAngle() + l2.getAngle()) % (Math.PI * 2)) / 2;

        Vector r2 = new Vector(l2.p2, avgAngle - Math.acos(d * 0.5 / r) + Math.PI / 2 * (l1.getAngle() > l2.getAngle() ? 1:-1), r, true);

        r2.p2.draw();
    }

    public void showAccessibleArea(){
        for(double i = 0; i < Math.PI; i += 0.05) {
            this.a1 = -i + minAngle;
            this.a2 = -i - minAngle;
            this.drawEndPoint();
        }
        for(double i = 0; i < Math.PI; i += 0.1) {
            this.a1 = -i + Math.PI / 2 - minAngle;
            this.a2 = -i - Math.PI / 2 + minAngle;
            this.drawEndPoint();
        }
    }
}