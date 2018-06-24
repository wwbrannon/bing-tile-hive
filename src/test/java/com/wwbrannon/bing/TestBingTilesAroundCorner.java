package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.google.common.collect.ImmutableList;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTilesAroundCorner
{
    @Test
    public void test()
    {
        // Different zoom Level
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, -180, 1), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("0", "2", "1", "3"));
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, -180, 3), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("220", "222", "221", "223"));
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, -180, 15), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("222222222222220", "222222222222222", "222222222222221", "222222222222223"));

        // Different Corners
        // Starting Corner 0,3
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, -180, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("20", "22", "21", "23"));
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, 180, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("30", "32", "31", "33"));
        assertFunction(
                "transform(bing_tiles_around(85.05112878, -180, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("00", "02", "01", "03"));
        assertFunction(
                "transform(bing_tiles_around(85.05112878, 180, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("10", "12", "11", "13"));
    }
}

