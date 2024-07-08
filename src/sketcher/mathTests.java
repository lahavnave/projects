package src.sketcher;

import src.drawingHelper.drawer;

public class mathTests extends drawer{
    public static void main(String[] args){

        mouseUpdater.start();
        Point[] points = new Point[]{
                new Point(),
                new ChangeablePoint(75 , 125),
                new ChangeablePoint(125, 75 ),
                new ChangeablePoint(275, 75 ),
                new ChangeablePoint(275, 75 ),
                new Point()
        };

        while (!canvas.isClosed()) {

            points[0] = points[1].times(2).minus(points[2]);
            points[points.length - 1] = points[points.length - 2].times(2).minus(points[points.length - 3]);


            for (int i = 0; i < points.length - 3; i++){
                for (double t = 0; t <= 1; t += 0.01){
                    calcPoint(points[i], points[i + 1], points[i + 2], points[i + 3], t).draw();
                }
            }
            for (Point p : points){
                p.draw();
            }
            ChangeManager.update();
            showChanges();
        }
    }

    static final double w = 0.5;
    static final Matrix m = new Matrix(new double[][]{
            {   0,     1,       0,  0 },
            {  -w,     0,       w,  0 },
            { 2*w, w - 3, 3 - 2*w, -w },
            {  -w, 2 - w,   w - 2,  w }
    });
    private static Point calcPoint(Point p1, Point p2, Point p3, Point p4, double t){

        Matrix powerMatrix;
        Matrix transformationMatrix;

        powerMatrix = Matrix.generatePowerMatrix(t, 4, false);
        PointMatrix points = new PointMatrix(new Point[][]{
                {p1, p2, p3, p4}
        });
        transformationMatrix = m.times(powerMatrix).sumRows();


        points.multiply(transformationMatrix);
        return points.sumAll();
    }


    static final Matrix derivative = new Matrix(new double[][]{
            {   -w,    w,      0,    0},
            {4*w-6, -4*w,  2*w+6, -2*w},
            {6-3*w,  3*w, -3*w-6,  3*w}
    });
    static final Matrix distMat = new Matrix(new double[][]{
            {     1,    0,    0,  0, -1 },
            {    -w,    w,    0,  0,  0 },
            { 2*w-3, -2*w,  w+3, -w,  0 },
            {   2-w,    w, -w-2,  w,  0 }
    });
    private static double calcDistance(Vector start, Vector end, double t, Point point){

        Matrix powerMatrix;
        Matrix transformationMatrix;

        powerMatrix = Matrix.generatePowerMatrix(t, 4, false);
        PointMatrix points = new PointMatrix(new Point[][]{
                {start.p1, start.p2, end.p1, end.p2}
        });
        transformationMatrix = m.times(powerMatrix).sumRows();


        points.multiply(transformationMatrix);
        return points.sumAll().distance(point);
    }

}



/*
function:
f(t) = [1  ] * [  a1,  a2,  a3,  a4 ] * [p1, p2, p3, p4]
       [t  ]   [  a5,  a6,  a7,  a8 ]
       [t^2]   [  a9, a10, a11, a12 ]
       [t^3]   [ a13, a14, a15, a16 ]

derivative:
f'(t) = [1  ] * [    a5,    a6,    a7,    a8 ] * [p1, p2, p3, p4]
        [t  ]   [  2*a9, 2*a10, 2*a11, 2*a12 ]
        [t^2]   [ 3*a13, 3*a14, 3*a15, 3*a16 ]

derivative of derivative:

f''(t) = [1] * [  2*a9, 2*a10, 2*a11, 2*a12 ] * [p1, p2, p3, p4]
         [t]   [ 6*a13, 6*a14, 6*a15, 6*a16 ]

requirements :
[   0,     1,       0,  0 ]
[  -w,     0,       w,  0 ]
[ 2*w, w - 3, 3 - 2*w, -w ]
[  -w, 2 - w,   w - 2,  w ]


a9 = 2*w
a10 = w - 3
a11 = 3 - 2*w
a12 = -w

a13 = -w
a14 = 2 - w
a15 = w - 2
a16 = w

*/