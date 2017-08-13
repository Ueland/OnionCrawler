package no.ueland.onionCrawler.web.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.britesnow.snow.web.renderer.freemarker.FreemarkerUtil;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.utils.ServletUtil;

@Singleton
public class AllPages extends Page {

    private final FreemarkerUtil freemarkerUtil;

    public AllPages() {
        this.freemarkerUtil = new FreemarkerUtil();
    }

    @WebModelHandler(startsWith = "/")
    public void doWebGet(@WebModel Map m, HttpServletRequest req, HttpServletResponse res) throws Exception {
        boolean isInstalled = configurationService.isInstalled();
        m.put("isInstalled", isInstalled);
        if (!isInstalled && !ServletUtil.getRequestedPath(req).equals("/admin/configuration")) {
            res.sendRedirect("/admin/configuration");
        }
        if (isInstalled) {
            //Load data used on all pages
            m.put("freemarkerUtil", freemarkerUtil);
        }
        m.put("isAdmin", ServletUtil.getRequestedPath(req).startsWith("/admin"));
    }
}
