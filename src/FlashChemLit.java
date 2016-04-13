import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Hansi Mou
 * @date Apr 5, 2016
 * @version 1.0
 */

/**
 * @author Hansi Mou
 *
 *         Apr 5, 2016
 */
public class FlashChemLit {
	static HashMap<String, StartingUrl> urls = new HashMap<String, StartingUrl>();
	static HashSet<String> exception = new HashSet<String>();
	static Config config = new Config();
	static ArrayList<String> startings = new ArrayList<String>();
	static HashSet<String> cache = new HashSet<String>();

	public static void main(String[] args) throws Exception {
		// get the cache file
		GetCache();

		// check folder exist or not and delete the outdated
		CheckFolder();

		// get all the starting urls and set-up
		GetUrls();
		/*
		 * // System.out.println(startings); // for (Map.Entry<String,
		 * StartingUrl> entry : urls.entrySet()) { //
		 * System.out.println("Key = " + entry.getKey() + "\n" // +
		 * entry.getValue().print()+"\n"); // }
		 */

		 putCrawlerToWork();

		 createIndex();
		 deleteOutdateIndex();
//		testIndex();

		/*
		 * WebCrawlerNature wc = new WebCrawlerNature(); wc.run(config.folder,
		 * urls.get(startings.get(0)), false, config, driver, cache); try {
		 * wc.DownloadPages(new URL(
		 * "http://www.nature.com/nature/research/chemical-sciences.html?code=npg_subject_638&year=2016&month=04"
		 * )); } catch (MalformedURLException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
	}

	/**
	 * Description:
	 */
	private static void testIndex() {
		try {
			Directory directory = FSDirectory.open(new File(config.indexfolder)
					.toPath());
			DirectoryReader reader = DirectoryReader.open(directory);
			StandardAnalyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig configl = new IndexWriterConfig(analyzer);
//			IndexWriter writer = null;
//			try {
//				writer = new IndexWriter(directory, configl);
//				IndexReader indexReader = DirectoryReader.open(writer, true);
//				System.out.println(indexReader.numDocs());
//				writer.close();
//			} catch (Exception eee) {
//
//			}

			IndexSearcher searcher = new IndexSearcher(reader);
			String[] stringQuery = "chemical".split(" ");
			String[] ff = new String[stringQuery.length];
			Arrays.fill(ff, "title");
			Query query = MultiFieldQueryParser.parse(stringQuery, ff,
					new StandardAnalyzer());

			TopDocs rs = searcher.search(query, 10);

			for (int i = 0; i < rs.scoreDocs.length; i++) {
				Document doc = searcher.doc(rs.scoreDocs[i].doc);
				System.out.println(doc.get("title"));
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Description:
	 */
	private static void deleteOutdateIndex() {
		try {
			Directory directory = FSDirectory.open(new File(config.indexfolder)
					.toPath());
			DirectoryReader reader = DirectoryReader.open(directory);
			StandardAnalyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig configl = new IndexWriterConfig(analyzer);
			IndexWriter writer = null;
			writer = new IndexWriter(directory, configl);
			Query q = NumericRangeQuery.newLongRange("date",
					new Date().getTime() - config.weeknum
							* (7 * 24 * 3600 * 1000), new Date().getTime(),
					true, true);
			writer.deleteDocuments(q);
			IndexReader indexReader = DirectoryReader.open(writer, true);
			indexReader.close();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Description: use the Lucene to create index
	 */
	private static void createIndex() {
		File indexfolder = new File(config.indexfolder);
		if (!indexfolder.exists()) {
			indexfolder.mkdir();
		}

		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig configl = new IndexWriterConfig(analyzer);
		IndexWriter writer = null;
		Directory directory;
		try {
			directory = FSDirectory.open(indexfolder.toPath());
			writer = new IndexWriter(directory, configl);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HTMLHandler hh = new HTMLHandler();
		// TODO Auto-generated method stub
		File dir = new File(config.dnfolder);
		for (File f : dir.listFiles()) {
			Document doc;
			try {
				doc = new HTMLHandler().getDocument(f);
				if (doc != null) {
					writer.addDocument(doc);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				f.delete();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
		}
	}

	/**
	 * /** Description: the method for the Crawler
	 */
	private static void putCrawlerToWork() {
		System.setProperty("webdriver.chrome.driver", config.cdp);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		for (int i = 0; i < startings.size(); i++) {
			if (startings.get(i).contains(".acs.")) {
				WebCrawlerACS wc = new WebCrawlerACS();
				wc.run(config.dnfolder, urls.get(startings.get(i)), false,
						config, driver, cache);
				wc.updateCache();
			} else if (startings.get(i).contains("science.sciencemag.org")) {
				WebCrawlerSci wc = new WebCrawlerSci();
				wc.run(config.dnfolder, urls.get(startings.get(i)), false,
						config, driver, cache);
				wc.updateCache();
			} else if (startings.get(i).contains("/nature/")) {
				WebCrawlerNature wc = new WebCrawlerNature();
				wc.run(config.dnfolder, urls.get(startings.get(i)), false,
						config, driver, cache);
				wc.updateCache();
			} else if (startings.get(i).contains(".nature.")) {
				WebCrawlerNatureXX wc = new WebCrawlerNatureXX();
				wc.run(config.dnfolder, urls.get(startings.get(i)), false,
						config, driver, cache);
				wc.updateCache();
			} else if (startings.get(i).contains("wiley")) {
				WebCrawlerWiley wc = new WebCrawlerWiley();
				wc.run(config.dnfolder, urls.get(startings.get(i)), false,
						config, driver, cache);
				wc.updateCache();
			}
		}
		driver.close();
	}

	/**
	 * Description: get the cache file
	 */
	private static void GetCache() {
		File file = new File(config.cachepath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				cache.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * Description:
	 */
	private static void GetUrls() {
		// TODO Auto-generated method stub
		try {
			// read file content from file
			FileReader reader = new FileReader("config.txt");
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			StartingUrl u = new StartingUrl();

			while ((str = br.readLine()) != null) {
				if (str.startsWith("//"))
					continue;
				else if (str.startsWith("#")) {
					if (!u.name.equals("")) {
						urls.put(u.link, u);
						u = new StartingUrl();
					}
					u.name = str.substring(1);
				} else if (u.name.equals("")) {
					continue;
				} else if (str.startsWith("Link")) {
					u.link = str.split("::")[1].trim();
					if (!u.name.equals("Nature") && !u.name.equals("Science"))
						startings.add(u.link);
				} else if (str.startsWith("Level")) {
					u.lv = Integer.parseInt(str.split("::")[1].trim());
					if (u.name.equals("Science")) {
						AddLastTwoWeekJournal(u);
						u = new StartingUrl();
					}

				} else if (str.startsWith("First_Level_Pre_Filter")) {
					u.flpre = str.split("::")[1].trim();
					if (u.name.equals("Nature")) {
						NAddLastFewWeekJournal(u);
						u = new StartingUrl();
					}
				} else if (str.startsWith("First_Level_Post_Filter")) {
					u.flpost = str.split("::")[1].trim();
				} else if (str.startsWith("Second_Level_Pre_Filter")) {
					u.secpre = str.split("::")[1].trim();
				} else if (str.startsWith("Second_Level_Post_Filter")) {
					u.secpost = str.split("::")[1].trim();
				} else if (str.startsWith("Second_Level_Post_Filter")) {
					u.secpost = str.split("::")[1].trim();
				} else if (str.startsWith("Third_Level_Pre_Filter")) {
					u.thirdpre = str.split("::")[1].trim();
				} else if (str.startsWith("Third_Level_Post_Filter")) {
					u.thirdpost = str.split("::")[1].trim();
				} else if (str.startsWith("Exception")) {
					exception.add(str.split("::")[1].trim());
				}
			}
			if (!u.name.equals(""))
				urls.put(u.link, u);

			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: expecially designed for Nature
	 */
	private static void NAddLastFewWeekJournal(StartingUrl u) {
		// the start date
		Calendar start = new GregorianCalendar();
		start.setTime(new Date());
		start.add(start.DATE, -config.weeknum * 7);
		// up to date
		Calendar now = new GregorianCalendar();
		Date today = new Date();
		now.setTime(today);

		int count;
		count = 12 * (now.get(Calendar.YEAR) - start.get(Calendar.YEAR))
				+ now.get(Calendar.MONTH) - start.get(Calendar.MONTH);

		int curYear = now.get(Calendar.YEAR);
		int curMonth = now.get(Calendar.MONTH) + 1;

		for (int c = 0; c <= count; c++) {
			StartingUrl news = new StartingUrl();
			news.link = u.link + "?year=" + curYear + "&month=" + curMonth;
			news.lv = u.lv;
			news.flpre = u.flpre;
			urls.put(news.link, news);
			startings.add(news.link);
			curMonth--;
			if (curMonth == 0) {
				curYear--;
				curMonth = 12;
			}
		}

		// for (int y = 0; y <= now.get(Calendar.YEAR)-start.get(Calendar.YEAR);
		// y++){
		// for (int m = 0; m <=
		// now.get(Calendar.MONTH)-start.get(Calendar.MONTH); m++) {
		// String yplusm = (start.get(Calendar.YEAR) + y) + "::" +
		// (start.get(Calendar.MONTH) + m + 1);
		// System.out.println(yplusm);
		// }
		// }
	}

	/**
	 * Description: expecially designed for science
	 */
	private static void AddLastTwoWeekJournal(StartingUrl u) {
		// TODO Auto-generated method stub

		// 2015 Jan 02 Vol 347, Iss 6217
		int stdVol = 347;
		int stdIss = 6217;

		for (int i = 1; i <= config.weeknum; i++) {
			long vol = stdVol;
			long iss = stdIss;
			Calendar now2 = new GregorianCalendar();
			Date last = new Date(new Date().getTime() - (i - 1)
					* (7 * 24 * 3600 * 1000));
			now2.setTime(last);
			vol += (now2.get(Calendar.YEAR) - 2015) * 4;
			if (now2.get(Calendar.MONTH) <= 2) {
			} else if (now2.get(Calendar.MONTH) <= 5) {
				vol++;
			} else if (now2.get(Calendar.MONTH) <= 8) {
				vol += 2;
			} else if (now2.get(Calendar.MONTH) <= 11) {
				vol += 3;
			}

			Calendar c = Calendar.getInstance();
			c.set(2015, 0, 2);
			Date standard = c.getTime();
			long tmp = (new Date().getTime() / 86400000 - standard.getTime() / 86400000) / 7;
			iss += tmp - i;
			StartingUrl mr = new StartingUrl();
			mr.name = u.name;
			mr.link = u.link + vol + "/" + iss;
			mr.lv = u.lv;
			mr.flpost = u.flpost;
			urls.put(mr.link, mr);
			startings.add(mr.link);
		}
	}

	/**
	 * Description: check whether fold exists, and whether files in the folder
	 * are out of date
	 * 
	 */
	private static void CheckFolder() {
		// TODO Auto-generated method stub
		File file = new File(config.dnfolder);
		if (!file.exists() && !file.isDirectory()) {
			// if folder does not exist, then make one.
			file.mkdir();
		} else {
			// check date
			File[] files = file.listFiles();
			for (File file2 : files) {
				// create time
				long modifiedTime = file.lastModified();
				// current time
				long now = new Date().getTime();

				// threshold
				long threshold = config.weeknum * 7 * 24 * 3600 * 1000;
				if (now - modifiedTime > threshold)
					file2.delete();
			}
		}
	}
}