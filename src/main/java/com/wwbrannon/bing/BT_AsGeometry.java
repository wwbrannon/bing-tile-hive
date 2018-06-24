package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.hadoop.hive.GeometryUtils;
import com.esri.core.geometry.ogc.OGCGeometry;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_AsGeometry",
    value = "_FUNC(BT_AsGeometry) - return ST_Geometry representation of Bing tile\n",
    extended = "Example:\n"
)

public class BT_AsGeometry extends BT_Base {
	public BytesWritable evaluate(Text btref) throws BingTileException {
		return evaluate(btref, 0);
	}

    public BytesWritable evaluate(Text btref, int wkid) throws BingTileException {
        BingTile bt = BingTile.fromQuadKey(btref.toString());
        Envelope env = bt.toEnvelope();
        
        try {
			SpatialReference spref = null;
			if (wkid != GeometryUtils.WKID_UNKNOWN) {
				spref = SpatialReference.create(wkid);
			}
			
            OGCGeometry ogc = OGCGeometry.createFromEsriGeometry(env, spref);
			return GeometryUtils.geometryToEsriShapeBytesWritable(ogc);
		} catch (Exception e) {  // IllegalArgumentException, GeometryException
			return null;
		}
    }
}

