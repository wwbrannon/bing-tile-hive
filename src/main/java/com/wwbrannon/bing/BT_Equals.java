package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_Equals extends BT_Base {
    public BooleanWritable evaluate(Text left, Text right) throws BingTileException
    {
        if(left == null || right == null) return null;

        BingTile btl = BingTile.fromQuadKey(left.toString());
        BingTile btr = BingTile.fromQuadKey(right.toString());

        return new BooleanWritable(btl.equals(btr));
    }
}

