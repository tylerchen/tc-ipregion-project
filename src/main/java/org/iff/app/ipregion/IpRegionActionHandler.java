/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.app.ipregion;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.PreRequiredHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.ActionHandler;
import org.iff.netty.server.handlers.BaseActionHandler;
import org.iff.util.SystemHelper;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     rest path = /rest/:Namespace/:ModelName/:RestUri
 *     GET    /articles                       -> articles#index
 *     GET    /articles/find/:conditions      -> articles#find
 *     GET    /articles/:id                   -> articles#show
 *     POST   /articles                       -> articles#create
 *     PUT    /articles/:id                   -> articles#update
 *     DELETE /articles/:id                   -> articles#destroy
 *     GET    /articles/ex/name/:conditions   -> articles#extra
 *     POST   /articles/ex/name               -> articles#extra
 *     PUT    /articles/ex/name/:conditions   -> articles#extra
 *     DELETE /articles/ex/name/:conditions   -> articles#extra
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class IpRegionActionHandler extends BaseActionHandler {

    public static final String URI_SPLIT = "uriSplit";
    public static final String uriPrefix = "/ip";
    public static final String RES_RECYCLE = "RES_RECYCLE";

    private String[] getRestUri(String[] uriSplit) {
        PreRequiredHelper.requireMinLength(uriSplit, 1);
        if (uriSplit.length == 1) {
            return new String[0];
        }
        String[] restUri = new String[uriSplit.length - 1];
        System.arraycopy(uriSplit, 1, restUri, 0, restUri.length);
        return restUri;
    }

    public boolean execute(ProcessContext ctx) {
        String[] restUri = null;
        {
            String[] uris = StringUtils.split(ctx.getRequestPath(), "/");
            if (uris.length == 1) {
                //TODO print rest path info.
                return true;
            }
            restUri = getRestUri(uris);
        }
        StringBuilder sb = new StringBuilder();
        List<String> ips = new ArrayList<>();
        for (String uri : restUri) {
            String[] split = StringUtils.split(uri, ',');
            for (String ip : split) {
                if (StringUtils.isNotBlank(ip)) {
                    ips.add(ip);
                }
            }
        }
        for (String ip : ips) {
            sb.append(ip).append('|').append(IpRegionDb.me().search(ip)).append('\n');
        }
        ctx.getOutputBuffer().writeCharSequence(sb.toString(), SystemHelper.UTF8);
        ctx.outputText();
        return true;
    }

    public boolean done() {
        Map<String, Closeable> close = ThreadLocalHelper.get(Closeable.class.getName());
        if (MapUtils.isNotEmpty(close)) {
            for (Map.Entry<String, Closeable> entry : close.entrySet()) {
                SocketHelper.closeWithoutError(entry.getValue());
            }
        }
        ThreadLocalHelper.remove();
        return super.done();
    }

    public boolean matchUri(String uri) {
        return uriPrefix.equals(uri) || (uri.startsWith(uriPrefix)
                && (uri.charAt(uriPrefix.length()) == '/' || uri.charAt(uriPrefix.length()) == '?'));
    }

    public int getOrder() {
        return 100;
    }

    public ActionHandler create() {
        return new IpRegionActionHandler();
    }

}
