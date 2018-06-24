package com.wwbrannon.bing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_GetX extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_GetX.class.getName());

    public IntWritable evaluate(Text btref) throws BingTileException
    {
        if(btref == null) return null;

        int x = BingTile.fromQuadKey(btref.toString()).getX();
        return new IntWritable(x);
    }
}

