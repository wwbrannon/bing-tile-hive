package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_GetX",
    value = "_FUNC_(BT_GetX) - return the x coordinate of a given Bing tile"
)

public class BT_GetX extends BT_Base {
    public IntWritable evaluate(Text btref) throws BingTileException
    {
        if(btref == null) return null;

        int x = BingTile.fromQuadKey(btref.toString()).getX();
        return new IntWritable(x);
    }
}

