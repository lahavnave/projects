package src.sketcher;

import src.drawingHelper.drawer;
import src.sketcher.dependecies.Sketcher;

import static src.drawingHelper.Colors.*;

public class mathTheoryCheck extends drawer {

    // region CONSTANTS
    static double w = 277; // 297 with a two centimeter buffer
    static double h = 190; // 210 with a two centimeter buffer
    static double d = 65;
    static double l = 150;
    static double r = 150;

    static double minAngle = Math.PI / 20;
    // endregion

    public static void main(String[] args) {

        // region INITIALIZE THE ARM OBJECTS
        Sketcher arm = new Sketcher(WIDTH * 0.5, HEIGHT * 0.5 + d + h / 2.0, l, r, -Math.PI / 2 + minAngle, -Math.PI / 2 - minAngle, minAngle);
        // endregion

        while (!canvas.isClosed()){

            // region REFRESH MOUSE POSITIONS
            refreshMouse();

            // translate the x and y values from screen position to a feasible input
            double mouseX = mouse.x - WIDTH * 0.5 + w / 2;
            double mouseY = HEIGHT - mouse.y - HEIGHT * 0.5 + h / 2;

            // endregion

            // region CALCULATE NEW SERVO ANGLES USING MY FORMULA
            double ySquared = Math.pow(mouseY + d, 2);
            double xSquared = Math.pow(mouseX - w / 2.0, 2);

            double a = Math.atan2(mouseX - w / 2.0, mouseY + d); // alpha
            double da = Math.acos((l*l + xSquared + ySquared - r*r) / (2 * l * Math.sqrt(xSquared + ySquared))); // delta alpha


            arm.a1 = a + da; // the right servo
            arm.a2 = a - da; // the left  servo

            // offset in order to counteract the field orientation
            arm.a1 -= Math.PI * 0.5;
            arm.a2 -= Math.PI * 0.5;
            // endregion

            // region SHOW EVERYTHING TO THE SCREEN
            try{
                arm.fullDraw();
            } catch (IllegalArgumentException ignored){}

            showPaper();
            showChanges();
            // endregion */

            /* region SHOW ACCESSIBLE AREA
            arm.showAccessibleArea();
            showPaper();
            showChanges();
            // endregion */

        }
    }

    public static void showPaper(){
        setColor(BLACK);
        canvas.drawRectangle(WIDTH * 0.5 - w / 2, HEIGHT * 0.5 - h / 2, w, h);
    }
}
