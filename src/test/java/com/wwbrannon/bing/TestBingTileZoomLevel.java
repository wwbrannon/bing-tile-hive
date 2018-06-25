package com.wwbrannon.bing;

import org.testng.annotations.Test;

import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestBingTileZoomLevel
{
    @Test
    public void test() throws BingTileException
    {
        assertEquals( (new BT_GetZoomLevel()).evaluate(new Text(BingTile.fromQuadKey("213").toQuadKey())).get(), 3);
        assertEquals( (new BT_GetZoomLevel()).evaluate(new Text(BingTile.fromQuadKey("123030123010121").toQuadKey())).get(), 15);
    }
}

