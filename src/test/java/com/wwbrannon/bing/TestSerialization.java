package com.wwbrannon.bing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestSerialization
{
    @Test
    public void test() throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        BingTile tile = BingTile.fromCoordinates(1, 2, 3);
        String json = objectMapper.writeValueAsString(tile);

        assertEquals(json, "{\"x\":1,\"y\":2,\"zoom\":3}");
        assertEquals(objectMapper.readerFor(BingTile.class).readValue(json), tile);
    }
}

