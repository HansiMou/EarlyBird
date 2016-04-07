// A minimal Web Crawler written in Java
// Usage: From command line 
//     java WebCrawler <URL> [N]
//  where URL is the url to start the crawl, and N (optional)
//  is the maximum number of pages to download.

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebCrawler {
	public static int LEVEL_LIMIT = 20; // Absolute max level
	public static boolean DEBUG = true;
	public static final String DISALLOW = "Disallow:";
	public static String path;

	int lv = 0;
	int curNum = 1;
	int last = 0;
	// URLs to be searched
	Queue<URL> newURLs;
	// Known URLs
	Hashtable<URL, Integer> knownURLs;
	HashSet<URL> lastLvResult;
	StartingUrl su;

	// initializes data structures. argv is the command line arguments.

	public void initialize(String path, StartingUrl s, boolean d) {
		URL url;
		this.path = path;

		knownURLs = new Hashtable<URL, Integer>();
		newURLs = new LinkedList<URL>();
		lastLvResult = new HashSet<URL>();
		su = s;

		LEVEL_LIMIT = s.lv;
		this.DEBUG = d;
		try {
			url = new URL(s.link);
		} catch (MalformedURLException e) {
			System.out.println("Invalid starting URL " + s.link);
			return;
		}
		knownURLs.put(url, new Integer(1));
		newURLs.add(url);
		System.out.println("Starting search: Initial URL " + url.toString());
		// System.out.println("Maximum level of search:" + LEVEL_LIMIT);

		/*
		 * Behind a firewall set your proxy and port here!
		 */
		// Properties props = new Properties(System.getProperties());
		// props.put("http.proxySet", "true");
		// props.put("http.proxyHost", "webcache-cup");
		// props.put("http.proxyPort", "8080");
		//
		// Properties newprops = new Properties(props);
		// System.setProperties(newprops);
		/**/
	}

	// Check that the robot exclusion protocol does not disallow
	// downloading url.

	public boolean robotSafe(URL url) {
		if (url.toString().contains("acs"))
			return true;
		String strHost = url.getHost();

		// form URL of the robots.txt file
		String strRobot = "http://" + strHost + "/robots.txt";
		// System.out.println(strRobot);
		URL urlRobot;
		try {
			urlRobot = new URL(strRobot);
		} catch (MalformedURLException e) {
			// something weird is happening, so don't trust it
			return false;
		}

		String strCommands;
		try {
			InputStream urlRobotStream = urlRobot.openStream();

			// read in entire file
			byte b[] = new byte[1000];
			int numRead = urlRobotStream.read(b);
			strCommands = new String(b, 0, numRead);
			while (numRead != -1) {
				numRead = urlRobotStream.read(b);
				if (numRead != -1) {
					String newCommands = new String(b, 0, numRead);
					strCommands += newCommands;
				}
			}
			urlRobotStream.close();
		} catch (IOException e) {
			// if there is no robots.txt file, it is OK to search
			return true;
		}

		// assume that this robots.txt refers to us and
		// search for "Disallow:" commands.
		String strURL = url.getFile();
		int index = 0;
		while ((index = strCommands.indexOf(DISALLOW, index)) != -1) {
			index += DISALLOW.length();
			String strPath = strCommands.substring(index);
			StringTokenizer st = new StringTokenizer(strPath);

			if (!st.hasMoreTokens())
				break;

			String strBadPath = st.nextToken();

			// if the URL starts with a disallowed path, it is not safe
			if (strURL.indexOf(strBadPath) == 0)
				return false;
		}

		return true;
	}

	// adds new URL to the queue. Accept only new URL's that end in
	// htm or html. oldURL is the context, newURLString is the link
	// (either an absolute or a relative URL).

	public void addnewurl(URL oldURL, String newUrlString)

	{
		// System.out.println("Found new URL " + newUrlString);
		URL url;
		// if (DEBUG)
		// System.out.println("URL String " + newUrlString);
		try {
			url = new URL(oldURL, newUrlString);

			// String filename = url.getFile();
			// int iSuffix = filename.lastIndexOf("htm");
			// if ((iSuffix == filename.length() - 3)
			// || (iSuffix == filename.length() - 4)) {
			if (shouldAdded(url)) {
				if (this.lv == 1)
					// System.out.println(url.toString());
					knownURLs.put(url, new Integer(1));
				newURLs.add(url);
				last++;
				if (this.lv == this.LEVEL_LIMIT - 1)
					this.lastLvResult.add(url);
			}
			// }
		} catch (MalformedURLException e) {
			return;
		}
	}

	/**
	 * Description: decide whether a url should be added according to
	 * configuration file
	 * 
	 * @param url
	 * @return boolean
	 */
	private boolean shouldAdded(URL url) {
		// if (knownURLs.containsKey(url)) {
		// return false;
		// }

		switch (this.lv) {
		case 0:
			if (su.flpre.startsWith("http://")) {
				return url.toString().toLowerCase()
						.startsWith(su.flpre.toLowerCase());
			} else {
				return url.toString().toLowerCase()
						.startsWith(su.link + su.flpre)
						&& url.toString().toLowerCase().endsWith(su.flpost);
			}
		case 1:
			if (su.secpre.startsWith("http://")) {
				return url.toString().toLowerCase()
						.startsWith(su.secpre.toLowerCase());
			} else {
				// System.out.println(url);
				return url.toString().toLowerCase()
						.startsWith(su.link + su.secpre)
						&& url.toString().toLowerCase().endsWith(su.secpost);
			}
		case 2:
			if (su.thirdpre.startsWith("http://")) {
				return url.toString().toLowerCase()
						.startsWith(su.thirdpre.toLowerCase());
			} else {
				return url.toString().toLowerCase()
						.startsWith(su.link + su.thirdpre)
						&& url.toString().toLowerCase().endsWith(su.thirdpost);
			}
		}
		return false;
	}

	// Download contents of URL

	/**
	 * Description: download pages
	 * 
	 * @param content
	 */
	private void DownloadPages(URL url, String content) {
		// TODO Auto-generated method stub
		String name = url.getFile().replace("/", "_");
		FileWriter writer;
		try {
			writer = new FileWriter(path + "/" + name);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Go through page finding links to URLs. A link is signalled
	// by <a href=" ... It ends with a close angle bracket, preceded
	// by a close quote, possibly preceded by a hatch mark (marking a
	// fragment, an internal page marker)

	public void processpage(URL url, String page)

	{
		String lcPage = page.toLowerCase(); // Page in lower case
		int index = 0; // position in page

		// while ((index = lcPage.indexOf("href=\"", index)) != -1) {
		// int ustart = index + 6;
		// int uend = lcPage.indexOf("\"", ustart);
		// String newUrlString = page.substring(ustart, uend);
		// System.out.println(newUrlString);
		// addnewurl(url, newUrlString);
		// index = uend;
		// }

		int iEndAngle, ihref, iURL, iCloseQuote, iHatchMark, iEnd;
		while ((index = lcPage.indexOf("<a", index)) != -1) {
			iEndAngle = lcPage.indexOf(">", index);
			ihref = lcPage.indexOf("href", index);
			if (ihref != -1) {
				iURL = lcPage.indexOf("\"", ihref) + 1;
				if ((iURL != -1) && (iEndAngle != -1) && (iURL < iEndAngle)) {
					iCloseQuote = lcPage.indexOf("\"", iURL);
					iHatchMark = lcPage.indexOf("#", iURL);
					if ((iCloseQuote != -1) && (iCloseQuote < iEndAngle)) {
						iEnd = iCloseQuote;
						if ((iHatchMark != -1) && (iHatchMark < iCloseQuote))
							iEnd = iHatchMark;
						String newUrlString = page.substring(iURL, iEnd);
						// System.out.println(newUrlString);
						addnewurl(url, newUrlString);
					}
				}
			}
			index = iEndAngle;
		}
	}

	// Top-level procedure. Keep popping a url off newURLs, download
	// it, and accumulate new URLs

	public void run(String path, StartingUrl s, boolean d) {
		initialize(path, s, d);
		while (this.lv < 2) {
			URL url = newURLs.poll();
			this.curNum--;
			if (DEBUG)
				System.out.println("level " + this.lv + "\t" + url.toString());
			// if (robotSafe(url)) {
			// String page = getpage(url);
			// if (DEBUG)
			// System.out.println(page);
			// if (page.length() != 0)
			// processpage(url, page);
			GetProcessAdd(url);
			if (newURLs.isEmpty())
				break;
			// }
			if (curNum == 0) {
				this.lv++;
				curNum = last;
				last = 0;
			}
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
		try {
			doc = Jsoup.connect(url.toString()).timeout(10000).get();
		} catch (IOException e) {
			if (DEBUG)
			e.printStackTrace();
		}
		Elements es = doc.getElementsByTag("a");
		if (DEBUG)
			System.out.println(es);
		for (int i = 0; i < es.size(); i++) {
			try {
				URL newUrl = new URL(url, es.get(i).attr("href"));
//				if(this.lv == 0)
//				System.out.println(newUrl);
				if (shouldAdded(newUrl)) {
					newURLs.add(newUrl);
					last++;
					if (this.lv == this.LEVEL_LIMIT - 1){
						this.lastLvResult.add(newUrl);
					}
				}
			} catch (MalformedURLException e1) {
				// e1.printStackTrace();
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
			System.out.println(obj.toString());
		}
		return this.lastLvResult;
	}
}