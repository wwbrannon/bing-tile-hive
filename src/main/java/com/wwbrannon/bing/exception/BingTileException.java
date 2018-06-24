package com.wwbrannon.bing.exception;

import java.lang.Class;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;

public class BingTileException extends UDFArgumentException {
    public BingTileException() {
        super();
    }

    public BingTileException(String message) {
        super(message);
    }

    public BingTileException(Throwable cause) {
        super(cause);
    }

    public BingTileException(String message, Class funcClass,
                             List<TypeInfo> argTypeInfos, List<Method> methods) {
        super(message, funcClass, argTypeInfos, methods);
    }
}

