import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class WebCrawlerNatureXX extends WebCrawler {
	static HashMap<String, String> imagecache = new HashMap<String, String>();

	public void run(String path, StartingUrl s, boolean d, Config c,
			WebDriver wd, HashSet<String> cache) {
		initialize(path, s, d, c, wd, cache);
		while (this.lv < this.LEVEL_LIMIT) {
			URL url = newURLs.poll();
			this.curNum--;
			// System.out.println(curNum);
			if (DEBUG)
				System.out.println("level " + this.lv + "\t" + url.toString());
			if (robotSafe(url)) {
				GetProcessAdd(url);
			}
			if (curNum == 0) {
				this.lv++;
				curNum = last;
				last = 0;
			}
			// System.out.println(tmpnewURLs.size());
			if (newURLs.isEmpty())
				break;
		}
		if (DEBUG)
			System.out.println("Search complete.");
		GetResult();
	}

	/**
	 * Description:
	 * 
	 * @param url
	 */

	private void GetProcessAdd(URL url) {
		Document doc = null;
		Elements es = null;
		try {
			doc = Jsoup.connect(url.toString()).data("query", "Java")
					.userAgent("Mozilla").cookie("auth", "token").timeout(3000)
					.post();
			if (this.lv == this.LEVEL_LIMIT - 1) {
				AddImageUrl(doc);
			}
			es = doc.getAllElements();
			// System.out.println(es.html());
		} catch (IOException e) {
			if (DEBUG)
				e.printStackTrace();
			return;
		}
		for (int i = 0; i < es.size(); i++) {
			try {
				URL newUrl = new URL(url, es.get(i).attr("href"));
				if (DEBUG)
					System.out.println(newUrl);
				if (shouldAdded(newUrl)) {
					// System.out.println(newUrl);
					newURLs.add(newUrl);
					last++;
					// System.out.println(this.lv);
					// System.out.println(this.LEVEL_LIMIT);
					if (this.lv == this.LEVEL_LIMIT - 1) {
						// System.out.println(newUrl);
						this.lastLvResult.add(newUrl);
					}
				}
			} catch (Exception e1) {
				// e1.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * Description:
	 */
	public HashSet<URL> GetResult() {
		// TODO Auto-generated method stub
		Iterator it = this.lastLvResult.iterator();
		URL obj = null;
		while (it.hasNext()) {
			obj = (URL) it.next();
			if (DEBUG)
				System.out.println(obj.toString());
			DownloadPagesHtmlUnit(obj);
		}
		return this.lastLvResult;
	}

	/**
	 * Description:
	 * 
	 * @param doc
	 */
	private void AddImageUrl(Document doc) {
		// TODO Auto-generated method stub
		Elements es = doc.getElementsByTag("img");
		for (Element e : es) {
			String src = e.attr("src");
			String[] tmp = src.split("/|-");
			if (tmp == null || tmp.length < 2)
				continue;
			String name = tmp[tmp.length - 2].trim();
			imagecache.put(name, "http://www.nature.com" + src);
		}
	}
}