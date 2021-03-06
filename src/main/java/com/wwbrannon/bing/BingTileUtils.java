package com.wwbrannon.bing;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryCursor;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiVertexGeometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.core.geometry.ogc.OGCPoint;
import com.esri.core.geometry.ogc.OGCPolygon;

import java.util.HashSet;
import java.util.Set;

public final class BingTileUtils
{
    private BingTileUtils() {}

    /**
     * Copy of com.esri.core.geometry.Interop.translateFromAVNaN
     *
     * deserializeEnvelope needs to recognize custom NAN values generated by
     * ESRI's serialization of empty geometries.
     */
    private static double translateFromAVNaN(double n)
    {
        return n < -1.0E38D ? (0.0D / 0.0) : n;
    }

    /**
     * Copy of com.esri.core.geometry.Interop.translateToAVNaN
     *
     * JtsGeometrySerde#serialize must serialize NaN's the same way ESRI library does to achieve binary compatibility
     */
    public static double translateToAVNaN(double n)
    {
        return (Double.isNaN(n)) ? -Double.MAX_VALUE : n;
    }

    public static boolean isEsriNaN(double d)
    {
        return Double.isNaN(d) || Double.isNaN(translateFromAVNaN(d));
    }

    public static int getPointCount(OGCGeometry ogcGeometry)
    {
        GeometryCursor cursor = ogcGeometry.getEsriGeometryCursor();
        int points = 0;
        while (true) {
            com.esri.core.geometry.Geometry geometry = cursor.next();
            if (geometry == null) {
                return points;
            }

            if (geometry.isEmpty()) {
                continue;
            }

            if (geometry instanceof Point) {
                points++;
            }
            else {
                points += ((MultiVertexGeometry) geometry).getPointCount();
            }
        }
    }

    public static Envelope getEnvelope(OGCGeometry ogcGeometry)
    {
        GeometryCursor cursor = ogcGeometry.getEsriGeometryCursor();
        Envelope overallEnvelope = new Envelope();
        while (true) {
            Geometry geometry = cursor.next();
            if (geometry == null) {
                return overallEnvelope;
            }

            Envelope envelope = new Envelope();
            geometry.queryEnvelope(envelope);
            overallEnvelope.merge(envelope);
        }
    }

    public static boolean disjoint(Envelope envelope, OGCGeometry ogcGeometry)
    {
        GeometryCursor cursor = ogcGeometry.getEsriGeometryCursor();
        while (true) {
            Geometry geometry = cursor.next();
            if (geometry == null) {
                return true;
            }

            if (!GeometryEngine.disjoint(geometry, envelope, null)) {
                return false;
            }
        }
    }

    public static boolean contains(OGCGeometry ogcGeometry, Envelope envelope)
    {
        GeometryCursor cursor = ogcGeometry.getEsriGeometryCursor();
        while (true) {
            Geometry geometry = cursor.next();
            if (geometry == null) {
                return false;
            }

            if (GeometryEngine.contains(geometry, envelope, null)) {
                return true;
            }
        }
    }

    public static boolean isPointOrRectangle(OGCGeometry ogcGeometry, Envelope envelope)
    {
        if (ogcGeometry instanceof OGCPoint) {
            return true;
        }

        if (!(ogcGeometry instanceof OGCPolygon)) {
            return false;
        }

        Polygon polygon = (Polygon) ogcGeometry.getEsriGeometry();
        if (polygon.getPathCount() > 1) {
            return false;
        }

        if (polygon.getPointCount() != 4) {
            return false;
        }

        Set<Point> corners = new HashSet<>();
        corners.add(new Point(envelope.getXMin(), envelope.getYMin()));
        corners.add(new Point(envelope.getXMin(), envelope.getYMax()));
        corners.add(new Point(envelope.getXMax(), envelope.getYMin()));
        corners.add(new Point(envelope.getXMax(), envelope.getYMax()));

        for (int i = 0; i < 4; i++) {
            Point point = polygon.getPoint(i);
            if (!corners.contains(point)) {
                return false;
            }
        }

        return true;
    }
}

