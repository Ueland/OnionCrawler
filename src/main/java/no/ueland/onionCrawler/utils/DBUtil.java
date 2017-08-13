package no.ueland.onionCrawler.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import no.ueland.onionCrawler.objects.exception.OnionCrawlerException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

public class DBUtil {
	static Logger logger = Logger.getLogger(DBUtil.class);

	public static int getIntValue(QueryRunner qr, String SQL, Object... params) throws OnionCrawlerException {
		try {
			Connection con = qr.getDataSource().getConnection();
			PreparedStatement st = con.prepareStatement(SQL);
			qr.fillStatement(st, params);
			ResultSet data = st.executeQuery(SQL);
			try {
				data.next();
				return data.getInt(1);
			} catch (Exception _ex) {
				return 0;
			} finally {
				st.close();
				con.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(), ex);
			throw new OnionCrawlerException(ex);
		}
	}

	public static String getStringValue(QueryRunner qr, String SQL) throws OnionCrawlerException {
		try {
			Connection con = qr.getDataSource().getConnection();
			Statement st = con.createStatement();
			ResultSet data = st.executeQuery(SQL);
			try {
				data.next();
				return data.getString(1);
			} catch (Exception _ex) {
				return null;
			} finally {
				st.close();
				con.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(), ex);
			throw new OnionCrawlerException(ex);
		}
	}
}
