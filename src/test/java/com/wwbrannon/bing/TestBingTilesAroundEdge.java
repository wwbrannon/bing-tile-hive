package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.google.common.collect.ImmutableList;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTilesAroundEdge
{
    @Test
    public void test()
    {
        // Different zoom Level
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, 0, 1), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("0", "2", "1", "3"));
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, 0, 3), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("231", "233", "320", "322", "321", "323"));
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, 0, 15), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of(
                        "233333333333331",
                        "233333333333333",
                        "322222222222220",
                        "322222222222222",
                        "322222222222221",
                        "322222222222223"));

        // Different Edges
        // Starting Edge 2,3
        assertFunction(
                "transform(bing_tiles_around(-85.05112878, 0, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("21", "23", "30", "32", "31", "33"));
        assertFunction(
                "transform(bing_tiles_around(85.05112878, 0, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("01", "03", "10", "12", "11", "13"));
        assertFunction(
                "transform(bing_tiles_around(0, 180, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("12", "30", "32", "13", "31", "33"));
        assertFunction(
                "transform(bing_tiles_around(0, -180, 2), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("02", "20", "22", "03", "21", "23"));
    }
}

