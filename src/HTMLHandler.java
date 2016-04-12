import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 * @author Hansi Mou
 * @date Apr 11, 2016
 * @version 1.0
 */

/**
 * @author Hansi Mou
 *
 *         Apr 11, 2016
 */
public class HTMLHandler {
	public static HashMap<String, Integer> month = new HashMap<String, Integer>();

	public int numOfWeeksToNow(File f) {
		long last = 0;
		try {
			intialize();
			org.jsoup.nodes.Document doc = Jsoup.parse(f, "UTF-8", "");
			if (f.getName().contains("_content_")) {
				// Sci
				Elements es = doc.getElementsByAttributeValue("name",
						"article:published_time");
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format1.parse(es.get(0).attr("content"));
				last = date.getTime();
			} else if (f.getName().contains("_doi_")
					&& f.getName().endsWith("_full")) {
				// wiley
				Elements es = doc.getElementsByAttributeValue("class",
						"article-header__meta-info-data");
				for (org.jsoup.nodes.Element e : es) {
					if (e.tagName().equals("time")) {
						DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						Date date = format1.parse(e.attr("datetime"));
						last = date.getTime();
						break;
					}
				}
			} else if (f.getName().contains("_doi_")) {
				// ACS
				Elements es = doc.getElementsByAttributeValue("id", "pubDate");
				String text = es.get(0).text().split(":")[1].trim();
				String ds = text.split(",")[1].trim() + "-";
				ds = ds + month.get(text.split(" ")[0].toLowerCase()) + "-"
						+ text.split(" |,")[1];
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format1.parse(ds);
				last = date.getTime();
			} else {
				// Nature
				String[] text = doc.text().split("Published online");
				if (text.length == 2) {
					String[] tmp = text[1].trim().split(" ", 4);
					String d = tmp[2] + "-" + month.get(tmp[1].toLowerCase())
							+ "-" + tmp[0];
					DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
					Date date = format1.parse(d);
					last = date.getTime();
				} else
					return Integer.MAX_VALUE;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Integer.MAX_VALUE;
		}
		
		return (int) ((new Date().getTime()-last)/(7*24*3600*1000));
	}

	/**
	 * Description:
	 */
	private void intialize() {
		// TODO Auto-generated method stub
		if (month.size() == 12)
			return;
		else {
			month.put("january", 1);
			month.put("february", 2);
			month.put("march", 3);
			month.put("april", 4);
			month.put("may", 5);
			month.put("june", 6);
			month.put("july", 7);
			month.put("august", 8);
			month.put("september", 9);
			month.put("october", 10);
			month.put("november", 11);
			month.put("december", 12);
		}
	}
}
