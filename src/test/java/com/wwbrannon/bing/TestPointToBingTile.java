package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

public class TestPointToBingTile
{
    @Test
    public void test()
    {
        assertFunction("bing_tile_at(30.12, 60, 15)", BING_TILE, BingTile.fromCoordinates(21845, 13506, 15));
        assertFunction("bing_tile_at(0, -0.002, 1)", BING_TILE, BingTile.fromCoordinates(0, 1, 1));
        assertFunction("bing_tile_at(1e0/512, 0, 1)", BING_TILE, BingTile.fromCoordinates(1, 0, 1));
        assertFunction("bing_tile_at(1e0/512, 0, 9)", BING_TILE, BingTile.fromCoordinates(256, 255, 9));

        // Invalid calls
        // Longitude out of range
        assertInvalidFunction("bing_tile_at(30.12, 600, 15)", "Longitude must be between -180.0 and 180.0");
        // Latitude out of range
        assertInvalidFunction("bing_tile_at(300.12, 60, 15)", "Latitude must be between -85.05112878 and 85.05112878");
        // Invalid zoom levels
        assertInvalidFunction("bing_tile_at(30.12, 60, 0)", "Zoom level must be > 0");
        assertInvalidFunction("bing_tile_at(30.12, 60, 40)", "Zoom level must be <= 23");
    }
}

