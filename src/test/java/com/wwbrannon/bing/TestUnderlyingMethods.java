package com.wwbrannon.bing;

import java.util.ArrayList;

import org.testng.annotations.Test;

import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;

import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.ST_GeomFromText;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestUnderlyingMethods
{
    @Test
    public void test() throws BingTileException, UDFArgumentException
    {
        ArrayList<Text> qks;
        ArrayList<BingTile> tmp;
        BingTile left, right;
        String wkt;

        assertEquals((new BT_FromCoordinates()).evaluate(new IntWritable(3), new IntWritable(5), new IntWritable(3)).toString(),
                     BingTile.fromCoordinates(3, 5, 3).toQuadKey());
        
        assertEquals((new BT_FromQuadKey()).evaluate(new Text("123030123010121")).toString(),
                     BingTile.fromQuadKey("123030123010121").toQuadKey());
        
        assertEquals((new BT_GetQuadKey()).evaluate((new BT_FromQuadKey()).evaluate(new Text("123030123010121"))).toString(),
                     BingTile.fromQuadKey("123030123010121").toQuadKey());
        
        assertEquals((new BT_GetX()).evaluate((new BT_FromQuadKey()).evaluate(new Text("123030123010121"))).get(),
                     BingTile.fromQuadKey("123030123010121").getX());
        
        assertEquals((new BT_GetY()).evaluate((new BT_FromQuadKey()).evaluate(new Text("123030123010121"))).get(),
                     BingTile.fromQuadKey("123030123010121").getY());
        
        assertEquals((new BT_GetZoomLevel()).evaluate((new BT_FromQuadKey()).evaluate(new Text("123030123010121"))).get(),
                     BingTile.fromQuadKey("123030123010121").getZoomLevel());

        assertEquals((new BT_FromLatLon()).evaluate(new DoubleWritable(-45.05112878), new DoubleWritable(-150), new IntWritable(4)).toString(),
                     BingTile.fromLatLon(-45.05112878, -150, 4).toQuadKey());

        qks = new ArrayList<Text>();
        tmp = BingTile.tilesAround(-45.05112878, -150, 4);
        for(BingTile b : tmp)
            qks.add(new Text(b.toQuadKey()));
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(-45.05112878), new DoubleWritable(-150), new IntWritable(4)), qks);

        left = BingTile.fromQuadKey("123030123010121");
        right = BingTile.fromQuadKey("123030123010121");
        assertEquals((new BT_Equals()).evaluate(new Text(left.toQuadKey()), new Text(right.toQuadKey())).get(), left.equals(right));

        left = BingTile.fromQuadKey("123030123010121");
        right = BingTile.fromLatLon(-45.05112878, -150, 4);
        assertEquals((new BT_Equals()).evaluate(new Text(left.toQuadKey()), new Text(right.toQuadKey())).get(), left.equals(right));
        
        wkt = "POLYGON ((59.996337890625 30.11662158281937, 60.00732421875 30.11662158281937, 60.00732421875 30.12612436422458, 59.996337890625 30.12612436422458, 59.996337890625 30.11662158281937))";
        qks = new ArrayList<Text>();
        tmp = BingTile.tilesCovering(OGCGeometry.fromText(wkt), 17);
        for(BingTile b : tmp)
            qks.add(new Text(b.toQuadKey()));
        assertEquals((new BT_TilesCoveringGeometry()).evaluate((new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(17)), qks);
    }
}

