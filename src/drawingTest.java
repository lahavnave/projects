package src;

import src.drawingHelper.drawer;
// https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md


public class drawingTest extends drawer {
    public static void main(String[] args) {

        Vector v = new Vector(0, 0, 100, 100, true);

        mouseUpdater.start();
        Button.updater.start();

        while (!canvas.isClosed()) {
            ChangeManager.update();

            showChanges();
        }
    }
}