package com.wwbrannon.bing;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;

import org.testng.annotations.Test;

import com.esri.hadoop.hive.ST_GeomFromText;
import com.esri.hadoop.hive.ST_Envelope;
import com.esri.hadoop.hive.ST_Point;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestTilesCoveringGeometry
{
    private void assertGeometryToBingTiles(String wkt, int zoomLevel, ArrayList<String> expectedQuadKeys) throws BingTileException, UDFArgumentException
    {
        ArrayList<Text> tmp = new ArrayList<Text>();

        for(String qk : expectedQuadKeys)
            tmp.add(new Text(qk));

        ArrayList<Text> qks = (new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(zoomLevel));

        assertEquals(qks, tmp);
    }

    @Test
    public void test_main() throws BingTileException, UDFArgumentException, IOException
    {
        String wkt;

        assertGeometryToBingTiles("POINT (60 30.12)", 10, new ArrayList<String>(Arrays.asList("1230301230")));
        assertGeometryToBingTiles("POINT (60 30.12)", 15, new ArrayList<String>(Arrays.asList("123030123010121")));
        assertGeometryToBingTiles("POINT (60 30.12)", 16, new ArrayList<String>(Arrays.asList("1230301230101212")));

        assertGeometryToBingTiles("POLYGON ((0 0, 0 10, 10 10, 10 0))", 6, new ArrayList<String>(Arrays.asList("122220", "122222", "122221", "122223")));
        assertGeometryToBingTiles("POLYGON ((0 0, 0 10, 10 10))", 6, new ArrayList<String>(Arrays.asList("122220", "122222", "122221")));

        assertGeometryToBingTiles("POLYGON ((10 10, -10 10, -20 -15, 10 10))", 3, new ArrayList<String>(Arrays.asList("033", "211", "122")));
        assertGeometryToBingTiles("POLYGON ((10 10, -10 10, -20 -15, 10 10))", 6, new ArrayList<String>(Arrays.asList("211102", "211120", "033321", "033323", "211101", "211103", "211121", "033330", "033332", "211110", "211112", "033331", "033333", "211111", "122220", "122222", "122221")));

        assertGeometryToBingTiles("POINT EMPTY", 10, new ArrayList<String>());
        assertGeometryToBingTiles("POLYGON EMPTY", 10, new ArrayList<String>());

        String filePath = this.getClass().getClassLoader().getResource("too_large_polygon.txt").getPath();
        String largeWkt = Files.lines(Paths.get(filePath)).findFirst().get();
        assertEquals( (new BT_TilesCoveringGeometry()).evaluate((new ST_Envelope()).evaluate((new ST_GeomFromText()).evaluate(new Text(largeWkt))), new IntWritable(16)).size(), 19939);
        
        wkt = "LINESTRING (0 0, 80 80)";
        assertEquals( (new BT_TilesCoveringGeometry()).evaluate((new ST_Envelope()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt))), new IntWritable(5)).size(), 104);
        
        wkt = "POLYGON ((0 0, 0 20, 20 20, 0 0))";
        assertEquals( (new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(14)).size(), 428787);
        
        assertEquals((new BT_TilesCoveringGeometry()).evaluate((new BT_AsGeometry()).evaluate((new BT_FromQuadKey()).evaluate(new Text("1230301230"))), new IntWritable(11)),
                     new ArrayList<Text>(Arrays.asList(new Text("12303012300"), new Text("12303012302"),
                                                       new Text("12303012301"), new Text("12303012303"))));

        wkt = "LINESTRING (59.765625 29.84064389983442, 60.2 30.14512718337612)";
        assertEquals((new BT_TilesCoveringGeometry()).evaluate((new ST_Envelope()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt))), new IntWritable(10)),
                     new ArrayList<Text>(Arrays.asList(new Text("1230301230"), new Text("1230301231"))));
        
        // NOTE: ESRI's Hive framework does not support geometrycollection
        // assertGeometryToBingTiles("GEOMETRYCOLLECTION EMPTY", 10, new ArrayList<String>());
        // assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12))", 10, new ArrayList<String>(Arrays.asList("1230301230")));
        // assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12))", 15, new ArrayList<String>(Arrays.asList("123030123010121")));
        // assertGeometryToBingTiles("GEOMETRYCOLLECTION (POLYGON ((10 10, -10 10, -20 -15, 10 10)))", 3, new ArrayList<String>(Arrays.asList("033", "211", "122")));
        // assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12), POLYGON ((10 10, -10 10, -20 -15, 10 10)))", 3, new ArrayList<String>(Arrays.asList("033", "211", "122", "123")));
        // assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12), LINESTRING (61 31, 61.01 31.01), POLYGON EMPTY)", 15, new ArrayList<String>(Arrays.asList("123030123010121", "123030112310200", "123030112310202", "123030112310201")));
    }

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Longitude must be between -180\\.0 and 180\\.0")
    public void test_lon1() throws BingTileException, UDFArgumentException
    {
        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_Point()).evaluate(new org.apache.hadoop.hive.serde2.io.DoubleWritable(600), new org.apache.hadoop.hive.serde2.io.DoubleWritable(30.12)), new IntWritable(10));
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Longitude must be between -180\\.0 and 180\\.0")
    public void test_lon2() throws BingTileException, UDFArgumentException
    {
        String wkt = "POLYGON ((1000 10, -10 10, -20 -15))";

        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(10));
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Latitude must be between -85\\.05112878 and 85\\.05112878")
    public void test_lat1() throws BingTileException, UDFArgumentException
    {
        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_Point()).evaluate(new org.apache.hadoop.hive.serde2.io.DoubleWritable(60), new org.apache.hadoop.hive.serde2.io.DoubleWritable(300.12)), new IntWritable(10));
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Latitude must be between -85\\.05112878 and 85\\.05112878")
    public void test_lat2() throws BingTileException, UDFArgumentException
    {
        String wkt = "POLYGON ((10 1000, -10 10, -20 -15))";

        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(10));
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Zoom level must be > 0")
    public void test_zoom1() throws BingTileException, UDFArgumentException
    {
        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_Point()).evaluate(new org.apache.hadoop.hive.serde2.io.DoubleWritable(60), new org.apache.hadoop.hive.serde2.io.DoubleWritable(30.12)), new IntWritable(0));
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Zoom level must be <= 23")
    public void test_zoom2() throws BingTileException, UDFArgumentException
    {
        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_Point()).evaluate(new org.apache.hadoop.hive.serde2.io.DoubleWritable(60), new org.apache.hadoop.hive.serde2.io.DoubleWritable(30.12)), new IntWritable(40));
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="The number of input tiles is too large \\(more than 1M\\) to compute a set of covering Bing tiles\\.")
    public void test_inputsize() throws BingTileException, UDFArgumentException
    {
        String wkt = "LINESTRING (0 0, 80 80)";

        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_Envelope()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt))), new IntWritable(16));
    }

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="The zoom level is too high or the geometry is too complex to compute a set of covering Bing tiles\\. Please use a lower zoom level or convert the geometry to its bounding box using the ST_Envelope function\\.")
    public void test_zoomtoohigh1() throws BingTileException, UDFArgumentException, IOException
    {
        String filePath = this.getClass().getClassLoader().getResource("too_large_polygon.txt").getPath();
        String largeWkt = Files.lines(Paths.get(filePath)).findFirst().get();
        
        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(largeWkt)), new IntWritable(16));
    }

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="The zoom level is too high to compute a set of covering Bing tiles\\.")
    public void test_zoomtoohigh2() throws BingTileException, UDFArgumentException
    {
        String wkt = "POLYGON ((0 0, 0 20, 20 20, 0 0))";

        ArrayList<Text> tiles = (new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(20));
    }
}

