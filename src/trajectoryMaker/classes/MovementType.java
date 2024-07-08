package src.trajectoryMaker.classes;

public enum MovementType {
    Tangent, Constant, Linear, Spline;
    public static MovementType get(String str){
        return switch (str) {
            case "1" -> MovementType.Tangent;
            case "2" -> MovementType.Constant;
            case "3" -> MovementType.Linear;
            case "4" -> MovementType.Spline;
            default -> null;
        };
    }
}

