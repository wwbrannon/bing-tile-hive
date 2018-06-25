package com.wwbrannon.bing;

import java.util.Arrays;
import java.util.ArrayList;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.testng.annotations.Test;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestBingTilesAroundCorner
{
    @Test
    public void test() throws BingTileException
    {
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(-85.05112878), new DoubleWritable(-180), new IntWritable(1)),
                     new ArrayList<Text>(Arrays.asList(new Text("0"), new Text("2"), new Text("1"), new Text("3"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(-85.05112878), new DoubleWritable(-180), new IntWritable(3)),
                     new ArrayList<Text>(Arrays.asList(new Text("220"), new Text("222"), new Text("221"), new Text("223"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(-85.05112878), new DoubleWritable(-180), new IntWritable(15)),
                     new ArrayList<Text>(Arrays.asList(new Text("222222222222220"), new Text("222222222222222"),
                                                       new Text("222222222222221"), new Text("222222222222223"))));

        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(-85.05112878), new DoubleWritable(-180), new IntWritable(2)),
                     new ArrayList<Text>(Arrays.asList(new Text("20"), new Text("22"), new Text("21"), new Text("23"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(-85.05112878), new DoubleWritable(180), new IntWritable(2)),
                     new ArrayList<Text>(Arrays.asList(new Text("30"), new Text("32"), new Text("31"), new Text("33"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(85.05112878), new DoubleWritable(-180), new IntWritable(2)),
                     new ArrayList<Text>(Arrays.asList(new Text("00"), new Text("02"), new Text("01"), new Text("03"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(85.05112878), new DoubleWritable(180), new IntWritable(2)),
                     new ArrayList<Text>(Arrays.asList(new Text("10"), new Text("12"), new Text("11"), new Text("13"))));
    }
}

