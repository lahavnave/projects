package src;

import src.drawingHelper.drawer;

public class Main extends drawer {
    public static void main(String[] args) {

        ChangeableSpline s = new ChangeableSpline(new ChangeablePoint[]{
                new ChangeablePoint(100, 100),
                new ChangeablePoint(100, 200),
                new ChangeablePoint(200, 200),
                new ChangeablePoint(300, 200)
        });



        mouseUpdater.start();
        while (!canvas.isClosed()) {
            ChangeManager.update();


            s.fancyDraw();


            showChanges();
        }
    }

    /*

    |p - p1| = r + r1
    |p - p2| = r + r2
    |p - p3| = r + r3


(x - x1)^2 + (y - y1)^2 = x^2 - 2 * x * x1 + x1^2 + y^2 - 2 * y * y1 + y1^2


    (x - x1)^2 + (yi - y1i)^2 = (r + r1)^2

    0 = r^2 + r * 2 * r1 - (x - x1)^2 + (y - y1)^2 + r1^2

    0 = 3r^2 + r * 2(r1 + r2 +r3) + ( r1^2 + r2^2 + r3^2

    x =
−b ± √(b2 − 4ac)
2a



     */
}