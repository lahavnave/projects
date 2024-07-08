package src;

import src.drawingHelper.drawer;
// https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md


public class robotSim extends drawer {
    public static void main(String[] args) {
        while (!canvas.isClosed()){
            Robot.angle += 0.01;
            Robot.draw();
            showChanges();
        }
    }
    static class Robot{
        // region POSITION VARIABLES
        public static Point center = new Point(WIDTH / 2.0, HEIGHT / 2.0);
        public static double angle = 0;
        // endregion

        // region CONSTANTS
        public static final int sideLength = 100;
        // endregion

        public static void draw(){
            // keeps the coordinates of the robots corners
            Point[] cornerCords = { new Point(), new Point(), new Point(), new Point() };

            // region CALCULATING THE CORDS
            cornerCords[0].x = center.x + Math.sin(Math.PI / 2.0 * 0.5 + angle) * sideLength * Math.sqrt(0.5);
            cornerCords[0].y = center.y - Math.cos(Math.PI / 2.0 * 0.5 + angle) * sideLength * Math.sqrt(0.5);

            cornerCords[1].x = center.x + Math.sin(Math.PI / 2.0 * 1.5 + angle) * sideLength * Math.sqrt(0.5);
            cornerCords[1].y = center.y - Math.cos(Math.PI / 2.0 * 1.5 + angle) * sideLength * Math.sqrt(0.5);

            cornerCords[2].x = center.x + Math.sin(Math.PI / 2.0 * 2.5 + angle) * sideLength * Math.sqrt(0.5);
            cornerCords[2].y = center.y - Math.cos(Math.PI / 2.0 * 2.5 + angle) * sideLength * Math.sqrt(0.5);

            cornerCords[3].x = center.x + Math.sin(Math.PI / 2.0 * 3.5 + angle) * sideLength * Math.sqrt(0.5);
            cornerCords[3].y = center.y - Math.cos(Math.PI / 2.0 * 3.5 + angle) * sideLength * Math.sqrt(0.5);
            // endregion

            // set the size of the wheels
            setLineWidth(20);

            // region DRAW THE WHEELS
            cornerCords[0].draw();
            cornerCords[1].draw();
            cornerCords[2].draw();
            cornerCords[3].draw();
            // endregion

            // the arrow that represents the front of the robot
            Vector facing = new Vector();

            // region CALCULATING THE CORDS
            facing.p1 = center;

            facing.p2.x = center.x + Math.sin(angle) * sideLength;
            facing.p2.y = center.y - Math.cos(angle) * sideLength;
            // endregion

            // set the width for all the lines in the drawing
            setLineWidth(5);

            // draw the line that represents the front of the robot
            facing.fancyDraw();

            // region DRAW THE ROBOT OUTLINE
            Vector.draw(cornerCords[0], cornerCords[3]);
            Vector.draw(cornerCords[1], cornerCords[0]);
            Vector.draw(cornerCords[2], cornerCords[1]);
            Vector.draw(cornerCords[3], cornerCords[2]);
            // endregion
        }
        public static void applyForce(Vector force, Point pos){

        }
    }
}


