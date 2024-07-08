package src;

import src.drawingHelper.drawer;
// https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md


public class drawingExperiments extends drawer {
    public static void main(String[] args) {


        mouseUpdater.start();
        Button.updater.start();

        while (!canvas.isClosed()) {
            ChangeManager.update();



            showChanges();
        }
    }
}