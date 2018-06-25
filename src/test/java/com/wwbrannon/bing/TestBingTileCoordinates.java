package com.wwbrannon.bing;

import org.testng.annotations.Test;

import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestBingTileCoordinates
{
    @Test
    public void test() throws BingTileException
    {
        assertEquals( (new BT_GetX()).evaluate(new Text("213")).get(), 3);
        assertEquals( (new BT_GetY()).evaluate(new Text("213")).get(), 5);
        
        assertEquals( (new BT_GetX()).evaluate(new Text("123030123010121")).get(), 21845);
        assertEquals( (new BT_GetY()).evaluate(new Text("123030123010121")).get(), 13506);
    }
}

