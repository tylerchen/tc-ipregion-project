/*******************************************************************************
 * Copyright (c) 2019-01-05 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.app.ipregion;

import org.iff.infra.util.ShutdownHookHelper;
import org.iff.util.SystemHelper;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * IpRegionDb
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-05
 * auto generate by qdp.
 */
public class IpRegionDb implements Closeable {
    public static final String dbName = "conf/ip2region.db";
    public static final String dbNameDev = "src/main/resources/assembly/conf/ip2region.db";
    public static IpRegionDb me = null;
    protected DbSearcher searcher;

    public IpRegionDb() {
    }

    public static IpRegionDb me() {
        if (me == null) {
            try {
                me = new IpRegionDb().init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return me;
    }

    public IpRegionDb init() throws Exception {
        File dbFile = SystemHelper.find("server.path.ipdbfile", new String[]{dbName, dbNameDev});
        String algoName = "B-tree";
        System.out.println("initializing " + algoName + " ... ");
        DbConfig config = new DbConfig();
        searcher = new DbSearcher(config, dbFile.getAbsolutePath());
        ShutdownHookHelper.register(getClass().getName(), this);
        return this;
    }

    public synchronized String search(String ip) {
        if (!Util.isIpAddress(ip)) {
            return "InValidIp";
        }
        try {
            return searcher.btreeSearch(ip).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "IpNotFound";
    }

    public void close() throws IOException {
        searcher.close();
    }
}
