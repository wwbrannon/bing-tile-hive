package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

public class TestDistinctFrom
{
    @Test
    public void test()
    {
        assertFunction("bing_tile(3, 5, 3) IS DISTINCT FROM null", BOOLEAN, true);
        assertFunction("null IS DISTINCT FROM bing_tile(3, 5, 3)", BOOLEAN, true);

        assertFunction("bing_tile(3, 5, 3) IS DISTINCT FROM bing_tile(3, 5, 3)", BOOLEAN, false);
        assertFunction("bing_tile('213') IS DISTINCT FROM bing_tile(3, 5, 3)", BOOLEAN, false);
        assertFunction("bing_tile('213') IS DISTINCT FROM bing_tile('213')", BOOLEAN, false);

        assertFunction("bing_tile(3, 5, 3) IS DISTINCT FROM bing_tile(3, 5, 4)", BOOLEAN, true);
        assertFunction("bing_tile('213') IS DISTINCT FROM bing_tile('2131')", BOOLEAN, true);
    }
}

