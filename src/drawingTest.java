package src;

import src.drawingHelper.drawer;
// https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md


public class drawingTest extends drawer {
    final static double l1 = 100, l2 = 150;
    public static void main(String[] args) {

        double x, y, a1 = Math.PI / 2, a2 = Math.PI / 2;

        Vector v1 = new Vector(new Point(350, 350), a1, l1);
        Vector v2 = new Vector(v1.p2, a2, l2);

        mouseUpdater.start();
        Button.updater.start();

        while (!canvas.isClosed()) {
            ChangeManager.update();
            x = mouse.x - 350;
            y = mouse.y - 350;











            double l3 = Math.sqrt(Math.pow(l1,2)+Math.pow(l2,2));
            double b1 = Math.acos(Math.pow(l1,2) + Math.pow(l3,2))-(2*l1*l3)-Math.pow(l2,2);
            double b2 = Math.acos(Math.pow(x,2)+Math.pow(l3,2)-(2*x*l3)-Math.pow(y,2));
            a1 = b2 - b1;
            a2 = Math.acos(Math.pow(l2,2)+Math.pow(l1,2) - (2*l2*l1)-Math.pow(l3,2));


            v1.setAngle(a1);
            v2.setAngle(a2);

            v1.fancyDraw();
            v2.fancyDraw();


            showChanges();
        }
    }
}