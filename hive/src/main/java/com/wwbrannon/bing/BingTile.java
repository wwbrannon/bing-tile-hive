package com.wwbrannon.bing;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import java.lang.Math.multiplyExact;
import java.lang.Math.toIntExact;
import java.lang.String.format;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.ogc.OGCGeometry;

import com.wwbrannon.bing.exception.BingException;

public final class BingTile
{
    public static final int MAX_ZOOM_LEVEL = 23;

    private static final int TILE_PIXELS = 256;
    private static final double MAX_LATITUDE = 85.05112878;
    private static final double MIN_LATITUDE = -85.05112878;
    private static final double MIN_LONGITUDE = -180;
    private static final double MAX_LONGITUDE = 180;

    private static final String LATITUDE_OUT_OF_RANGE = "Latitude must be between " + MIN_LATITUDE + " and " + MAX_LATITUDE;
    private static final String LONGITUDE_OUT_OF_RANGE = "Longitude must be between " + MIN_LONGITUDE + " and " + MAX_LONGITUDE;
    private static final String QUAD_KEY_EMPTY = "QuadKey must not be empty string";
    private static final String QUAD_KEY_TOO_LONG = "QuadKey must be " + MAX_ZOOM_LEVEL + " characters or less";
    private static final String ZOOM_LEVEL_TOO_SMALL = "Zoom level must be > 0";
    private static final String ZOOM_LEVEL_TOO_LARGE = "Zoom level must be <= " + MAX_ZOOM_LEVEL;
    private static final String ZOOM_LEVEL_OUT_OF_RANGE = "Zoom level must be > 0 and <= " + MAX_ZOOM_LEVEL;

    private final int x;
    private final int y;
    private final int zoomLevel;

    /*
     * Constructors
     */

    private BingTile(int x, int y, int zoomLevel)
    {
        if(x == null || y == null || zoomLevel == null)
            throw new BingException("x, y, zoomLevel must be non-null");
        
        if(zoomLevel <= 0 || zoomLevel > MAX_ZOOM_LEVEL)
            throw new BingException("zoomLevel must be > 0 and < " +
                                    MAX_ZOOM_LEVEL.toString());

        this.x = x;
        this.y = y;
        this.zoomLevel = zoomLevel;
    }

    /*
     * Value-checking methods
     */

    private static void checkCondition(boolean condition, String formatString, Object... args)
    {
        if (!condition) {
            throw new BingException(format(formatString, args));
        }
    }

    private static void checkZoomLevel(long zoomLevel)
    {
        checkCondition(zoomLevel > 0, ZOOM_LEVEL_TOO_SMALL);
        checkCondition(zoomLevel <= MAX_ZOOM_LEVEL, ZOOM_LEVEL_TOO_LARGE);
    }

    private static void checkCoordinate(long coordinate, long zoomLevel)
    {
        checkCondition(coordinate >= 0 && coordinate < (1 << zoomLevel),
                       "XY coordinates for a Bing tile at zoom level %s must be within [0, %s) range",
                       zoomLevel, 1 << zoomLevel);
    }

    private static void checkQuadKey(String quadkey)
    {
        checkCondition(quadkey.length() > 0, QUAD_KEY_EMPTY);
        checkCondition(quadkey.length() <= MAX_ZOOM_LEVEL, QUAD_KEY_TOO_LONG);
    }

    private static void checkLatitude(double latitude)
    {
        checkCondition(latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE, LATITUDE_OUT_OF_RANGE);
    }

    private static void checkLongitude(double longitude)
    {
        checkCondition(longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE, LONGITUDE_OUT_OF_RANGE);
    }

    /*
     * Utility functions for converting to or from latitude/longitude
     */

    private static long mapSize(int zoomLevel)
    {
        return 256L << zoomLevel;
    }

    private static int axisToCoordinates(double axis, long mapSize)
    {
        int tileAxis = (int) clip(axis * mapSize, 0, mapSize - 1);
        return tileAxis / TILE_PIXELS;
    }

    private static int longitudeToTileX(double longitude, long mapSize)
    {
        double x = (longitude + 180) / 360;
        return axisToCoordinates(x, mapSize);
    }

    private static int longitudeToTileY(double latitude, long mapSize)
    {
        double sinLatitude = Math.sin(latitude * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);
        return axisToCoordinates(y, mapSize);
    }

    private static Point tileXYToLatitudeLongitude(int tileX, int tileY, int zoomLevel)
    {
        long mapSize = mapSize(zoomLevel);
        
        double x = (clip(tileX * TILE_PIXELS, 0, mapSize) / mapSize) - 0.5;
        double y = 0.5 - (clip(tileY * TILE_PIXELS, 0, mapSize) / mapSize);

        double lat = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
        double lon = 360 * x;
        
        return new Point(lon, lat);
    }

    /*
     * Misc utility methods
     */

    private static double clip(double n, double minValue, double maxValue)
    {
        return Math.min(Math.max(n, minValue), maxValue);
    }

    // =========================================================================

    /*
     * Overridden methods from Object
     */

    @Override
    public boolean equals(Object other)
    {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        BingTile otherTile = (BingTile) other;
        return this.x == otherTile.x
            && this.y == otherTile.y
            && this.zoomLevel == otherTile.zoomLevel;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, zoomLevel);
    }

    @Override
    public String toString()
    {
        String fmt = "%s{x=%s, y=%s, zoomLevel=%s}";
        return format(fmt, this.getClass().getSimpleName(),
                      x, y, zoomLevel);
    }

    /*
     * Serialization and other representations
     */

    public Envelope toEnvelope()
    {
        Point upperLeftCorner = tileXYToLatitudeLongitude(this.getX(), this.getY(), this.getZoomLevel());
        Point lowerRightCorner = tileXYToLatitudeLongitude(this.getX() + 1, this.getY() + 1, this.getZoomLevel());
        
        return new Envelope(upperLeftCorner.getX(), lowerRightCorner.getY(),
                            lowerRightCorner.getX(), upperLeftCorner.getY());
    }
    
    public String toQuadKey()
    {
        char[] quadKey = new char[this.zoomLevel];
        for (int i = this.zoomLevel; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((this.x & mask) != 0) {
                digit++;
            }
            if ((this.y & mask) != 0) {
                digit += 2;
            }
            quadKey[this.zoomLevel - i] = digit;
        }
        return String.valueOf(quadKey);
    }

    // Encodes Bing tile as a 64-bit long: 23 bits for X, followed by 23 bits for Y,
    // followed by 5 bits for zoomLevel
    public long encode()
    {
        return (((long) x) << 28) + (y << 5) + zoomLevel;
    }

    /*
     * alternative constructors as static methods
     */

    public static BingTile decode(long tile)
    {
        int tileX = (int) (tile >> 28);
        int tileY = (int) ((tile % (1 << 28)) >> 5);
        int zoomLevel = (int) (tile % (1 << 5));

        return new BingTile(tileX, tileY, zoomLevel);
    }
    
    public static BingTile fromCoordinates(int x, int y, int zoomLevel)
    {
        return new BingTile(x, y, zoomLevel);
    }

    public static BingTile fromLatLon(double lat, double lon, int zoomLevel)
    {
        long mapSize = mapSize(zoomLevel);
        
        int tileX = longitudeToTileX(lon, mapSize);
        int tileY = longitudeToTileY(lat, mapSize);
        
        return BingTile.fromCoordinates(tileX, tileY, zoomLevel);
    }

    public static BingTile fromQuadKey(String quadKey) throws BingException
    {
        int zoomLevel = quadKey.length();
        
        checkCondition(zoomLevel > 0, ZOOM_LEVEL_TOO_SMALL);
        checkCondition(zoomLevel <= MAX_ZOOM_LEVEL, ZOOM_LEVEL_TOO_LARGE);
        
        int tileX = 0;
        int tileY = 0;
        for (int i = zoomLevel; i > 0; i--) {
            int mask = 1 << (i - 1);
            switch (quadKey.charAt(zoomLevel - i)) {
                case '0':
                    break;
                case '1':
                    tileX |= mask;
                    break;
                case '2':
                    tileY |= mask;
                    break;
                case '3':
                    tileX |= mask;
                    tileY |= mask;
                    break;
                default:
                    throw new BingException("Invalid QuadKey digit sequence: " + quadKey);
            }
        }

        return new BingTile(tileX, tileY, zoomLevel);
    }

    public static ArrayList<BingTile> tilesAround(double lat, double lon, int zoomLevel)
    {
        checkLatitude(latitude, LATITUDE_OUT_OF_RANGE);
        checkLongitude(longitude, LONGITUDE_OUT_OF_RANGE);
        checkZoomLevel(zoomLevel, ZOOM_LEVEL_OUT_OF_RANGE);

        ArrayList<BingTile> ret = new ArrayList<BingTile>();

        long mapSize = mapSize(toIntExact(zoomLevel));
        long maxTileIndex = (mapSize / TILE_PIXELS) - 1;

        int tileX = longitudeToTileX(longitude, mapSize);
        int tileY = longitudeToTileY(latitude, mapSize);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = tileX + i;
                int y = tileY + j;
                
                if (x >= 0 && x <= maxTileIndex && y >= 0 && y <= maxTileIndex)
                    ret.add(BingTile.fromCoordinates(x, y, toIntExact(zoomLevel)));
            }
        }

        return ret;
    }

    /*
     * Accessor methods
     */

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZoomLevel()
    {
        return zoomLevel;
    }

}
