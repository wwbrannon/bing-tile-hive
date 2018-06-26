package com.wwbrannon.bing;

import org.testng.annotations.Test;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestPointToBingTile
{
    @Test
    public void test_equal() throws BingTileException
    {
        assertEquals((new BT_FromLatLon()).evaluate(new DoubleWritable(30.12), new DoubleWritable(60), new IntWritable(15)).toString(),
                     BingTile.fromCoordinates(21845, 13506, 15).toQuadKey());
        assertEquals((new BT_FromLatLon()).evaluate(new DoubleWritable(0), new DoubleWritable(-0.002), new IntWritable(1)).toString(),
                     BingTile.fromCoordinates(0, 1, 1).toQuadKey());
        assertEquals((new BT_FromLatLon()).evaluate(new DoubleWritable(1e0/512), new DoubleWritable(0), new IntWritable(1)).toString(),
                     BingTile.fromCoordinates(1, 0, 1).toQuadKey());
        assertEquals((new BT_FromLatLon()).evaluate(new DoubleWritable(1e0/512), new DoubleWritable(0), new IntWritable(9)).toString(),
                     BingTile.fromCoordinates(256, 255, 9).toQuadKey());
    }

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Longitude must be between -180\\.0 and 180\\.0")
    public void test_lon() throws BingTileException
    {
        // Longitude out of range
        BingTile bt = BingTile.fromLatLon(30.12, 600, 15);
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Latitude must be between -85\\.05112878 and 85\\.05112878")
    public void test_lat() throws BingTileException
    {
        // Latitude out of range
        BingTile bt = BingTile.fromLatLon(300.12, 60, 15);
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Zoom level must be > 0")
    public void test_zoom1() throws BingTileException
    {
        // Invalid zoom level
        BingTile bt = BingTile.fromLatLon(30.12, 60, 0);
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Zoom level must be <= 23")
    public void test_zoom2() throws BingTileException
    {
        // Invalid zoom level
        BingTile bt = BingTile.fromLatLon(30.12, 60, 40);
    }
}

