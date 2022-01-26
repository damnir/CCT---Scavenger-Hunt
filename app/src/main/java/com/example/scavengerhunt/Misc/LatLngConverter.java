package com.example.scavengerhunt.Misc;

/**
 * Utility class for converting a coordinate into its DMS (degrees, minutes,
 * seconds) representation
 *
 * @author marlonlom
 * @url https://gist.github.com/marlonlom/a558df46900bcd54b4db
 */

public final class LatLngConverter {
    /**
     * Constant for latitude/longitude orientations
     */
    private static final String[] ORIENTATIONS = "N/S/E/W".split("/");

    /*
    public static void main(String[] args) throws Exception {
        final float[] coordinates = { -71.3854f, -30.8199f };
        String dmsResult = processCoordinates(coordinates);
        final String coords_txt = coordinates[1] + "," + coordinates[0];
        System.out.println(coords_txt + " converted -> " + dmsResult);
    }*/

    public String latnglToDMS(float lat, float lng) {
        float[] coordinates = {lat, lng};
        String dmsResult = processCoordinates(coordinates);
        return  dmsResult;
    }
    /**
     * Given a array of coordinates [longitude, latitude], returns the dms
     * (degrees, minutes, seconds) representation
     *
     * @param coordinates
     *            array of coordinates, with 2+ elements
     * @return dms representation for given array
     */
    private static String processCoordinates(float[] coordinates) {
        String converted0 = LatLngConverter.decimalToDMS(coordinates[1]);
        final String dmsLat = coordinates[0] > 0 ? ORIENTATIONS[0] : ORIENTATIONS[1];
        converted0 = converted0.concat(" ").concat(dmsLat);

        String converted1 = LatLngConverter.decimalToDMS(coordinates[0]);
        final String dmsLng = coordinates[1] > 0 ? ORIENTATIONS[2] : ORIENTATIONS[3];
        converted1 = converted1.concat(" ").concat(dmsLng);

        return converted0.concat(", ").concat(converted1);
    }

    /**
     * Given a decimal longitudinal coordinate such as <i>-79.982195</i> it will
     * be necessary to know whether it is a latitudinal or longitudinal
     * coordinate in order to fully convert it.
     *
     * @param coord
     *            coordinate in decimal format
     * @return coordinate in D°M′S″ format
     * @see <a href='https://goo.gl/pWVp60'>Geographic coordinate conversion
     *      (wikipedia)</a>
     */
    private static String decimalToDMS(float coord) {

        float mod = coord % 1;
        int intPart = (int) coord;

        String degrees = String.valueOf(intPart);

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;
        if (intPart < 0)
            intPart *= -1;

        String minutes = String.valueOf(intPart);

        coord = mod * 60;
        intPart = (int) coord;
        if (intPart < 0)
            intPart *= -1;

        String seconds = String.valueOf(intPart);
        String output = Math.abs(Integer.parseInt(degrees)) + "°" + minutes + "'" + seconds + "\"";

        return output;
    }

}
