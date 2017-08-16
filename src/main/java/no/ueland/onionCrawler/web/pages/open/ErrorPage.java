package no.ueland.onionCrawler.web.pages.open;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import com.britesnow.snow.web.handler.annotation.WebModelHandler;
import com.britesnow.snow.web.param.annotation.WebModel;
import com.google.inject.Singleton;
import no.ueland.onionCrawler.web.pages.Page;

@Singleton
public class ErrorPage extends Page {
	@WebModelHandler(matches = "/error")
	public void doWebGet(@WebModel Map m, HttpServletRequest req) {
		m.put("errorCode", req.getParameter("code"));
	}
}
