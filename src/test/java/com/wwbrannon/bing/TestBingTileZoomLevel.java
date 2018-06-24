package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTileZoomLevel
{
    @Test
    public void test()
    {
        assertFunction("bing_tile_zoom_level(bing_tile('213'))", TINYINT, (byte) 3);
        assertFunction("bing_tile_zoom_level(bing_tile('123030123010121'))", TINYINT, (byte) 15);
    }
}

