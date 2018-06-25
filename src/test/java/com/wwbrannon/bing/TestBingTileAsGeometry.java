package com.wwbrannon.bing;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.testng.annotations.Test;

import com.esri.hadoop.hive.ST_AsText;
import com.esri.hadoop.hive.ST_Point;
import com.esri.hadoop.hive.ST_Centroid;
import com.esri.hadoop.hive.ST_MaxX;
import com.esri.hadoop.hive.ST_MinX;
import com.esri.hadoop.hive.ST_MaxY;
import com.esri.hadoop.hive.ST_MinY;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestBingTileAsGeometry
{
    @Test
    public void test() throws BingTileException
    {
        assertEquals((new ST_AsText()).evaluate((new BT_AsGeometry()).evaluate((new BT_FromQuadKey()).evaluate(new Text("123030123010121")))).toString(),
                     "POLYGON ((59.996337890625 30.11662158281937, 60.00732421875 30.11662158281937, 60.00732421875 30.12612436422458, 59.996337890625 30.12612436422458, 59.996337890625 30.11662158281937))");
        
        // FIXME
        // assertEquals((new ST_AsText()).evaluate((new ST_Centroid()).evaluate((new BT_AsGeometry()).evaluate((new BT_FromQuadKey()).evaluate(new Text("123030123010121"))))).toString(),
        //              "POINT (60.0018310442288 30.121372968273892)");
        // assertFunction("ST_AsText(ST_Centroid(bing_tile_polygon(bing_tile('123030123010121'))))", VARCHAR, "POINT (60.0018310442288 30.121372968273892)");

        // Check bottom right corner of a stack of tiles at different zoom levels
        BytesWritable bt;

        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(1), new IntWritable(1), new IntWritable(1)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(),
                     "POINT (180 -85.05112877980659)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(3), new IntWritable(3), new IntWritable(2)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(),
                     "POINT (180 -85.05112877980659)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(7), new IntWritable(7), new IntWritable(3)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(),
                     "POINT (180 -85.05112877980659)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(15), new IntWritable(15), new IntWritable(4)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(),
                     "POINT (180 -85.05112877980659)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(31), new IntWritable(31), new IntWritable(5)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(),
                     "POINT (180 -85.05112877980659)");
        // assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(31, 31, 5)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (180 -85.05112877980659)");

        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(0), new IntWritable(0), new IntWritable(1)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(1), new IntWritable(1), new IntWritable(2)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(3), new IntWritable(3), new IntWritable(3)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(7), new IntWritable(7), new IntWritable(4)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(15), new IntWritable(15), new IntWritable(5)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MaxX()).evaluate(bt), (new ST_MinY()).evaluate(bt))).toString(), "POINT (0 0)");

        // // Check top left corner of a stack of tiles at different zoom levels
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(1), new IntWritable(1), new IntWritable(1)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(2), new IntWritable(2), new IntWritable(2)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(4), new IntWritable(4), new IntWritable(3)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(8), new IntWritable(8), new IntWritable(4)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(16), new IntWritable(16), new IntWritable(5)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(), "POINT (0 0)");
        
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(0), new IntWritable(0), new IntWritable(1)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(),
                     "POINT (-180 85.05112877980659)");
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(0), new IntWritable(0), new IntWritable(2)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(),
                     "POINT (-180 85.05112877980659)");
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(0), new IntWritable(0), new IntWritable(3)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(),
                     "POINT (-180 85.05112877980659)");
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(0), new IntWritable(0), new IntWritable(4)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(),
                     "POINT (-180 85.05112877980659)");
        bt = (new BT_AsGeometry()).evaluate((new BT_FromCoordinates()).evaluate(new IntWritable(0), new IntWritable(0), new IntWritable(5)));
        assertEquals((new ST_AsText()).evaluate((new ST_Point()).evaluate((new ST_MinX()).evaluate(bt), (new ST_MaxY()).evaluate(bt))).toString(),
                     "POINT (-180 85.05112877980659)");
    }
}

