import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;

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

	public static void main(String[] args) {
		// System.out.println(config.folder);
		// System.out.println(config.weeknum);

		CheckFolder();

		// get all the starting urls and set-up
		GetUrls();

//		for (Map.Entry<String, StartingUrl> entry : urls.entrySet()) {
//			System.out.println("Key = " + entry.getKey() + "\n"
//					+ entry.getValue().print());
//		}
		WebCrawler wc = new WebCrawler();
		wc.run(config.folder, urls.get("http://pubs.acs.org/"), false);
//		try {
//			System.out.println(new URL(new URL("http://www.nature.com/nchembio/"), "/nchembio/research/index.html"));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
				} else if (str.startsWith("Level")) {
					u.lv = Integer.parseInt(str.split("::")[1].trim());
				} else if (str.startsWith("First_Level_Pre_Filter")) {
					u.flpre = str.split("::")[1].trim();
				} else if (str.startsWith("First_Level_Post_Filter")) {
					u.flpost = str.split("::")[1].trim();
					if (u.name.equals("Science")) {
						AddLastTwoWeekJournal(u);
						u = new StartingUrl();
					}
				} else if (str.startsWith("Second_Level_Pre_Filter")) {
					u.secpre = str.split("::")[1].trim();
				} else if (str.startsWith("Second_Level_Post_Filter")) {
					u.secpost = str.split("::")[1].trim();
				} else if (str.startsWith("Second_Level_Post_Filter")) {
					u.secpost = str.split("::")[1].trim();
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
		}
	}

	/**
	 * Description: check whether fold exists, and whether files in the folder
	 * are out of date
	 * 
	 * @param folder
	 * @param weeknum
	 */
	private static void CheckFolder() {
		// TODO Auto-generated method stub
		File file = new File(config.folder);
		if (!file.exists() && !file.isDirectory()) {
			// if folder does not exist, then make one.
			file.mkdir();
		} else {
			// check date
			// File[] files = file.listFiles();
			// for (File file2 : files) {
			// // create time
			// long modifiedTime = file.lastModified();
			//
			// Date d = new Date();
			// System.out.println(format.format(d));
			// }
		}
	}
}