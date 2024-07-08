package src;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class trajMaker {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\matib\\Desktop\\robot software\\roadrunnerProject");

        for (File trajectoryFile : file.listFiles()) {
            if (trajectoryFile.getName().charAt(0) != '_'){
                String[][] arr = convertFileToValueArray(trajectoryFile);

                Position[] poses = new Position[arr.length -1];

                for (int i = 0; i < arr.length - 1; i++){
                    poses[i] = new Position(
                            Double.parseDouble(arr[i][0]),
                            Double.parseDouble(arr[i][1]),
                            Double.parseDouble(arr[i][2]),
                            Double.parseDouble(arr[i][3]),
                            i,
                            arr[i][4],
                            trajectoryFile.getName().split(".yaml")[0].split("_to_")[0],
                            trajectoryFile.getName().split(".yaml")[0].split("_to_")[1]);
                }

                poses[arr.length - 2].index = -1;

                String txt = "// region " + trajectoryFile.getName().split(".yaml")[0] + "\n";
                for (Position pose : poses){
                    txt += pose.getDeclaration() + "\n";
                }
                txt += "\n";
                for (Position pose : poses){
                    txt += pose.getUsage();
                }
                txt += "// endregion\n";
                System.out.println(txt);
            }
        }
    }

    private static String getText(File file) {
        int length = (int) file.length();

        char[] array = new char[length];
        try {
            FileReader reader = new FileReader(file);
            reader.read(array, 0, length);
        } catch (IOException e) {}

        return convertArrayToText(array);
    }

    private static String convertArrayToText(char[] array){
        String txt = "";
        for (char letter : array){
            txt += letter;
        }
        return txt;
    }

    private static String[][] convertFileToValueArray(File file){
        String txt = getText(file);
        String[][] organizedTxt = new String[txt.split("    x: ").length][5];

        String[] splitTxt = txt.split("- position:\n");

        organizedTxt[0][0] = splitTxt[0].split(": ")[1].split("\n")[0];
        organizedTxt[0][1] = splitTxt[0].split(": ")[2].split("\n")[0];
        organizedTxt[0][2] = splitTxt[0].split(": ")[3].split("\n")[0];
        organizedTxt[0][3] = splitTxt[0].split(": ")[4].split("\n")[0];

        for (int i = 1; i < splitTxt.length; i++){
            organizedTxt[i][0] = splitTxt[i].split(": ")[1].split("\n")[0];
            organizedTxt[i][1] = splitTxt[i].split(": ")[2].split("\n")[0];
            organizedTxt[i][2] = splitTxt[i].split(": ")[3].split("\n")[0];
            organizedTxt[i][3] = splitTxt[i].split(": ")[4].split("\n")[0];
            organizedTxt[i][4] = splitTxt[i].split(": ")[5].split("\n")[0];
        }
        return organizedTxt;
    }

    static class Position{

        public double x;
        public double y;
        public double heading;
        public double tangent;
        public String moveType;
        public String from;
        public String to;
        public int index;
        public Position(double x, double y, double heading, double tangent, int index, String moveType, String from, String to){
            this.x = x;
            this.y = y;
            this.heading = heading;
            this.tangent = tangent;
            this.moveType = moveType;
            this.from = from;
            this.to = to;
            this.index = index;
        }

        public String getDeclaration(){
            if (index == 0){
                return "Pose2d " + from + "Pose = new Pose2d(" + x + ", " + y + ", Math.toRadians(Math.toRadians(" + toDegrees(heading) + ")));";
            }
            else if (Objects.equals(moveType, "Constant") || Objects.equals(moveType, "Tangent")){
                if (index != -1){
                    return "Vector2d " + from + "_to_" + to + "_tempPose_" + index +" = new Vector2d(" + x + ", " + y + ");";
                } else {
                    return "Vector2d " + to + "Pose = new Vector2d(" + x + ", " + y + ");";
                }
            }
            else {
                if (index != -1) {
                    return "Pose2d " + from + "_to_" + to + "_tempPose_" + index + " = new Pose2d(" + x + ", " + y + ", Math.toRadians(" + toDegrees(tangent) + "));";
                } else {
                    return "Pose2d " + to + "Pose = new Pose2d(" + x + ", " + y + ", Math.toRadians(" + toDegrees(tangent) + "));";
                }
            }
        }

        public String getUsage(){
            String txt = "";
            if (index != -1){
                if (index == 0){
                    txt += "TrajectorySequence " + from + "_to_" + to + "_trajectory = drive.trajectorySequenceBuilder(" + from + "Pose).\nsetTangent(Math.toRadians(" + toDegrees(tangent) + ")).";
                }
                else if (Objects.equals(moveType, "Spline")){
                    txt += "splineToSplineHeading(" + from + "_to_" + to + "_tempPose_" + index + ", Math.toRadians(" + toDegrees(heading) + ")).";
                }
                else if (Objects.equals(moveType, "Constant")){
                    txt += "splineToConstantHeading(" + from + "_to_" + to + "_tempPose_" + index + ", Math.toRadians(" + toDegrees(tangent) + ")).";
                }
                else if (Objects.equals(moveType, "Tangent")){
                    txt += "splineTo(" + from + "_to_" + to + "_tempPose_" + index + ", " + tangent + ").";
                }
                else {
                    txt += "splineToLinearHeading(" + from + "_to_" + to + "_tempPose_" + index + ", Math.toRadians(" + toDegrees(heading) + ")).";
                }
            }
            else {
                if (Objects.equals(moveType, "Spline")){
                    txt += "splineToSplineHeading(" + to + "Pose, Math.toRadians(" + toDegrees(heading) + ")).";
                }
                else if (Objects.equals(moveType, "Constant")){
                    txt += "splineToConstantHeading(" + to + "Pose, Math.toRadians(" + toDegrees(tangent) + ")).";
                }
                else if (Objects.equals(moveType, "Tangent")){
                    txt += "splineTo(" + to + "Pose, Math.toRadians(" + toDegrees(tangent) + ")).";
                }
                else {
                    txt += "splineToLinearHeading(" + to + "Pose, Math.toRadians(" + toDegrees(heading) + ")).";
                }
                txt += "build();";
            }
            return txt += "\n";
        }

        public static double toDegrees(double value){
            if (value == 0){
                return 0;
            }
            double v = Math.abs(Math.toDegrees(value));

            if ((v % 1) > 0.9){
                v += 1 - (v % 1);
            }
            return v * value / Math.abs(value);
        }
    }
}