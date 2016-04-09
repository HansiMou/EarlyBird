// A minimal Web Crawler written in Java
// Usage: From command line 
//     java WebCrawler <URL> [N]
//  where URL is the url to start the crawl, and N (optional)
//  is the maximum number of pages to download.

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebCrawlerSci extends WebCrawler {

	public void run(String path, StartingUrl s, boolean d, Config c, WebDriver wd, HashSet<String> cache) {
		initialize(path, s, d, c, wd, cache);
		while (this.lv < this.LEVEL_LIMIT) {
			URL url = newURLs.poll();
			this.curNum--;
			// System.out.println(curNum);
			if (DEBUG)
				System.out.println("level " + this.lv + "\t" + url.toString());
			// if (robotSafe(url)) {
			GetProcessAdd(url);

			// }
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
		driver.get(url.toString());

		String res = driver.getPageSource();
		String[] tmp = url.toString().split("/");
		String vol = tmp[tmp.length - 2];
		String iss = tmp[tmp.length - 1];
		Pattern p = Pattern.compile(vol + "\\\\/" + iss + "\\\\/" + "\\d+");
		Matcher m = p.matcher(res);
		while (m.find()) {
			String s = su.link+"/"+m.group().split("\\/")[2];
			try {
				lastLvResult.add(new URL(s));
			} catch (MalformedURLException e) {
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
			DownloadPagesWebDriver(obj);
		}
		return this.lastLvResult;
	}
}