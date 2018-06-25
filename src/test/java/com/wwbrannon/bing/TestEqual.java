package com.wwbrannon.bing;

import org.testng.annotations.Test;

import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestEqual
{
    @Test
    public void test() throws BingTileException
    {
        assertTrue( (new BT_Equals()).evaluate(new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey()),
                                               new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey())).get());
        assertTrue( (new BT_Equals()).evaluate(new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey()),
                                               new Text(BingTile.fromQuadKey("213").toQuadKey())).get());
        assertTrue( (new BT_Equals()).evaluate(new Text(BingTile.fromQuadKey("213").toQuadKey()),
                                               new Text(BingTile.fromQuadKey("213").toQuadKey())).get());

        assertFalse( (new BT_Equals()).evaluate(new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey()),
                                                new Text(BingTile.fromCoordinates(3, 5, 4).toQuadKey())).get());
        assertFalse( (new BT_Equals()).evaluate(new Text(BingTile.fromQuadKey("213").toQuadKey()),
                                                new Text(BingTile.fromQuadKey("2131").toQuadKey())).get());
        
        assertTrue( (new BT_Equals()).evaluate(new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey()), null) == null);
        assertTrue( (new BT_Equals()).evaluate(null, new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey())) == null);
        assertTrue( BingTile.fromCoordinates(3, 5, 3).toQuadKey() != null);
    }
}

