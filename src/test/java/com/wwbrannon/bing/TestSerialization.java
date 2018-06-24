package com.wwbrannon.bing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestSerialization
{
    @Test
    public void test() throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        BingTile tile = BingTile.fromCoordinates(1, 2, 3);
        String json = objectMapper.writeValueAsString(tile);
        Assert.assertEquals("{\"x\":1,\"y\":2,\"zoom\":3}", json);
        Assert.assertEquals(tile, objectMapper.readerFor(BingTile.class).readValue(json));
    }
}

