package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_FromQuadKey",
    value = "_FUNC_(BT_FromQuadKey) - return the Bing tile corresponding to a given quadkey"
)

public class BT_FromQuadKey extends BT_Base {
    public Text evaluate(Text btref) throws BingTileException
    {
        if(btref == null) return null;

        // create this object as a way of checking validity
        BingTile bt = BingTile.fromQuadKey(btref.toString());
        return new Text(bt.toQuadKey());
    }
}

