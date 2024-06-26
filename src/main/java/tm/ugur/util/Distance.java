package tm.ugur.util;

import org.springframework.stereotype.Component;

@Component
public class Distance {

    public boolean calculate(double pointX, double pointY, double busX, double busY){
        double distance = Math.sqrt(Math.pow(busX - pointX, 2) + Math.pow(busY - pointY, 2));
        return distance < 0.004;
    }


    public double calculateRadius(double aX, double aY, double bX, double bY) {
        double R = 6371;

        return R * calculateProcess(aX, aY, bX, bY);
    }

    public double calculateRadiusMeter(double aX, double aY, double bX, double bY) {
        double R = 6371e3;

        return R * calculateProcess(aX, aY, bX, bY);
    }

    private double calculateProcess(double aX, double aY, double bX, double bY) {
        double latDistance = Math.toRadians(bX - aX);
        double lonDistance = Math.toRadians(bY - aY);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(aX)) * Math.cos(Math.toRadians(bX))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

       return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
