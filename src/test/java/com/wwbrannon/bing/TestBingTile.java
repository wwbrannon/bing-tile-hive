package com.wwbrannon.bing;

import org.testng.annotations.Test;

import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestBingTile
{
    @Test
    public void test_equal() throws BingTileException
    {
        assertEquals( (new BT_GetQuadKey()).evaluate(new Text("213")).toString(), "213");
        assertEquals( (new BT_GetQuadKey()).evaluate(new Text("123030123010121")).toString(), "123030123010121");
        
        assertEquals( (new BT_GetQuadKey()).evaluate(new Text(BingTile.fromCoordinates(3, 5, 3).toQuadKey())).toString(), "213");
        assertEquals( (new BT_GetQuadKey()).evaluate(new Text(BingTile.fromCoordinates(21845, 13506, 15).toQuadKey())).toString(), "123030123010121");
    }

    /*
     * Invalid calls: corrupt quadkeys
     */

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="QuadKey must not be empty string")
    public void test_empty() throws BingTileException
    {
        BingTile bt = BingTile.fromQuadKey("");
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Invalid QuadKey digit sequence: test")
    public void test_digiseq() throws BingTileException
    {
        BingTile bt = BingTile.fromQuadKey("test");
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Invalid QuadKey digit sequence: 12345")
    public void test_digiseq2() throws BingTileException
    {
        BingTile bt = BingTile.fromQuadKey("12345");
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="QuadKey must be 23 characters or less")
    public void test_qklen() throws BingTileException
    {
        BingTile bt = BingTile.fromQuadKey("101010101010101010101010101010100101010101001010");
    }

    /*
     * Invalid calls: XY out of range
     */

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="XY coordinates for a Bing tile at zoom level 3 must be within \\[0, 8\\) range")
    public void test_badcoords() throws BingTileException
    {
        BingTile bt = BingTile.fromCoordinates(10, 2, 3);
    }
    
    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="XY coordinates for a Bing tile at zoom level 3 must be within \\[0, 8\\) range")
    public void test_badcoords2() throws BingTileException
    {
        BingTile bt = BingTile.fromCoordinates(2, 10, 3);
    }

    /*
     * Invalid calls: zoom level out of range
     */

    @Test(expectedExceptions = BingTileException.class, expectedExceptionsMessageRegExp="Zoom level must be <= 23")
    public void test_badzoom() throws BingTileException
    {
        BingTile bt = BingTile.fromCoordinates(2, 7, 37);
    }
}

