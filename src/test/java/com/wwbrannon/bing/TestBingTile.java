package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTile
{
    @Test
    public void test()
    {
        assertFunction("bing_tile_quadkey(bing_tile('213'))", VARCHAR, "213");
        assertFunction("bing_tile_quadkey(bing_tile('123030123010121'))", VARCHAR, "123030123010121");

        assertFunction("bing_tile_quadkey(bing_tile(3, 5, 3))", VARCHAR, "213");
        assertFunction("bing_tile_quadkey(bing_tile(21845, 13506, 15))", VARCHAR, "123030123010121");

        // Invalid calls: corrupt quadkeys
        assertInvalidFunction("bing_tile('')", "QuadKey must not be empty string");
        assertInvalidFunction("bing_tile('test')", "Invalid QuadKey digit sequence: test");
        assertInvalidFunction("bing_tile('12345')", "Invalid QuadKey digit sequence: 12345");
        assertInvalidFunction("bing_tile('101010101010101010101010101010100101010101001010')", "QuadKey must be 23 characters or less");

        // Invalid calls: XY out of range
        assertInvalidFunction("bing_tile(10, 2, 3)", "XY coordinates for a Bing tile at zoom level 3 must be within [0, 8) range");
        assertInvalidFunction("bing_tile(2, 10, 3)", "XY coordinates for a Bing tile at zoom level 3 must be within [0, 8) range");

        // Invalid calls: zoom level out of range
        assertInvalidFunction("bing_tile(2, 7, 37)", "Zoom level must be <= 23");
    }
}

