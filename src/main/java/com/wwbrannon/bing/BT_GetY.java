package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_GetY",
    value = "_FUNC_(BT_GetY) - return the y coordinate of a given Bing tile"
)

public class BT_GetY extends BT_Base {
    public IntWritable evaluate(Text btref) throws BingTileException
    {
        if(btref == null) return null;

        int y = BingTile.fromQuadKey(btref.toString()).getY();
        return new IntWritable(y);
    }
}

