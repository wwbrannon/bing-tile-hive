package com.wwbrannon.bing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import com.esri.hadoop.hive.GeometryUtils;

import com.wwbrannon.bing.BingTile;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_BingTileToGeometry extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTileToGeometry.class.getName());

    public BytesWritable evaluate(Text btref) throws UDFArgumentException {
        BingTile bt = BingTile.fromQuadKey(btref.toString());
        
        return GeometryUtils.geometryToEsriShapeBytesWritable(bt.toEnvelope());
    }
}

