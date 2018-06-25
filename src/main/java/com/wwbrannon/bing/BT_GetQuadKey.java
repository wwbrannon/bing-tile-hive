package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_GetQuadKey",
    value = "_FUNC_(BT_GetQuadKey) - return the quadkey of a given Bing tile\n"
)

public class BT_GetQuadKey extends BT_Base {
    public Text evaluate(Text btref) throws BingTileException
    {
        if(btref == null) return null;

        // create this object as a way of checking validity
        BingTile bt = BingTile.fromQuadKey(btref.toString());
        return new Text(bt.toQuadKey());
    }
}

