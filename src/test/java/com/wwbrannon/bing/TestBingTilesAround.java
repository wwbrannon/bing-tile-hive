package com.wwbrannon.bing;

import java.util.Arrays;
import java.util.ArrayList;

import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.testng.annotations.Test;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

public class TestBingTilesAround
{
    @Test
    public void test() throws BingTileException
    {
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(30.12), new DoubleWritable(60), new IntWritable(1)),
                     new ArrayList<Text>(Arrays.asList(new Text("0"), new Text("2"), new Text("1"), new Text("3"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(30.12), new DoubleWritable(60), new IntWritable(15)),
                     new ArrayList<Text>(Arrays.asList(new Text("123030123010102"), new Text("123030123010120"), new Text("123030123010122"),
                                                       new Text("123030123010103"), new Text("123030123010121"), new Text("123030123010123"),
                                                       new Text("123030123010112"), new Text("123030123010130"), new Text("123030123010132"))));
        
        assertEquals((new BT_TilesAround()).evaluate(new DoubleWritable(30.12), new DoubleWritable(60), new IntWritable(23)),
                     new ArrayList<Text>(Arrays.asList(new Text("12303012301012121210122"), new Text("12303012301012121210300"),
                                                       new Text("12303012301012121210302"), new Text("12303012301012121210123"),
                                                       new Text("12303012301012121210301"), new Text("12303012301012121210303"),
                                                       new Text("12303012301012121210132"), new Text("12303012301012121210310"),
                                                       new Text("12303012301012121210312"))));
    }
}

