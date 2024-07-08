package src.sketcher;

import src.drawingHelper.drawer;

public class pathFollowingConcept extends drawer {
    public static void main(String[] args){
        mouseUpdater.start();

        ChangeableSpline s1 = new ChangeableSpline(
                new Vector[]{
                        new ChangeableVector(400, 400, 475, 400),
                        new ChangeableVector(500, 500, 500, 575),
                        new ChangeableVector(400, 600, 325, 600),
                        new ChangeableVector(300, 500, 300, 425),
                        new ChangeableVector(400, 400, 475, 400)
                }
        );

        ChangeableSpline s2 = new ChangeableSpline(
                new Point[]{
                    new ChangeablePoint(75 , 125),
                    new ChangeablePoint(125, 75 ),
                    new ChangeablePoint(275, 75 ),
                    new ChangeablePoint(350, 75 )
            }
        );

        while (!canvas.isClosed()) {
            s1.fancyDraw();
            s2.fancyDraw();
            ChangeManager.update();
            showChanges();
        }
    }
}
