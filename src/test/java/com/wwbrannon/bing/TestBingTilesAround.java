package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.google.common.collect.ImmutableList;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTilesAround
{
    @Test
    public void test()
    {
        assertFunction(
                "transform(bing_tiles_around(30.12, 60, 1), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of("0", "2", "1", "3"));
        assertFunction(
                "transform(bing_tiles_around(30.12, 60, 15), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of(
                        "123030123010102",
                        "123030123010120",
                        "123030123010122",
                        "123030123010103",
                        "123030123010121",
                        "123030123010123",
                        "123030123010112",
                        "123030123010130",
                        "123030123010132"));
        assertFunction(
                "transform(bing_tiles_around(30.12, 60, 23), x -> bing_tile_quadkey(x))",
                new ArrayType(VARCHAR),
                ImmutableList.of(
                        "12303012301012121210122",
                        "12303012301012121210300",
                        "12303012301012121210302",
                        "12303012301012121210123",
                        "12303012301012121210301",
                        "12303012301012121210303",
                        "12303012301012121210132",
                        "12303012301012121210310",
                        "12303012301012121210312"));
    }
}

