package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.google.common.collect.ImmutableList;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestArrayOfBingTiles
{
    @Test
    public void test() throws Exception
    {
        assertFunction("array [bing_tile(1, 2, 10), bing_tile(3, 4, 11)]",
                new ArrayType(BING_TILE),
                ImmutableList.of(BingTile.fromCoordinates(1, 2, 10),
                                 BingTile.fromCoordinates(3, 4, 11)));
    }
}

