package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTileCoordinates
{
    @Test
    public void test()
    {
        assertFunction("bing_tile_coordinates(bing_tile('213')).x", INTEGER, 3);
        assertFunction("bing_tile_coordinates(bing_tile('213')).y", INTEGER, 5);
        assertFunction("bing_tile_coordinates(bing_tile('123030123010121')).x", INTEGER, 21845);
        assertFunction("bing_tile_coordinates(bing_tile('123030123010121')).y", INTEGER, 13506);

        assertCachedInstanceHasBoundedRetainedSize("bing_tile_coordinates(bing_tile('213'))");
    }
}

