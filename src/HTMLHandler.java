import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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

		return (int) ((new Date().getTime() - last) / (7 * 24 * 3600 * 1000));
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

	/**
	 * Description:
	 * 
	 * @param f
	 * @return
	 */
	public Document getDocument(File f) {
		intialize();

		String title = "";
		String doi = "";
		String summary = "";
		String date = "";
		String url = "";
		String fullurl = "";
		String type = "";
		String image = "";
		String journaltitle = "";
		String publisher = "";
		ArrayList<String> author = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();

		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(f, "UTF-8", "");
			if (f.getName().contains("_content_")) {
				// Sci
				summary = doc.getElementsByAttributeValue("class",
						"section abstract").text();
				// no abstract, use summary
				if (summary == null || summary.length() == 0) {
					summary = doc.getElementsByAttributeValue("class",
							"section summary").text();
					if (summary.contains("Summary")) {
						summary = summary.split("Summary", 2)[1].trim();
					}
				} else {
					summary = summary.split("Abstract", 2)[1].trim();
				}
				doi = doc.getElementsByAttributeValue("class", "meta-line")
						.text().replace("DOI:", "\nDOI:").trim();

				Elements es = doc.getElementsByTag("meta");
				for (Element e : es) {
					if (e.attr("name").equals("DC.Title"))
						title = e.attr("content").trim();
					else if (e.attr("name").equals("DC.Date"))
						date = e.attr("content").trim();
					else if (e.attr("name")
							.equals("citation_abstract_html_url"))
						url = e.attr("content").trim();
					else if (e.attr("name").equals("citation_full_html_url"))
						fullurl = e.attr("content").trim();
					else if (e.attr("name").equals("og:type"))
						type = e.attr("content").trim();
					else if (e.attr("name").equals("og:image"))
						image = e.attr("content").trim();
					else if (e.attr("name").equals("citation_journal_title"))
						journaltitle = e.attr("content").trim();
					else if (e.attr("name").equals("DC.Publisher"))
						publisher = e.attr("content").trim();
					else if (e.attr("name").equals("DC.Contributor"))
						author.add(e.attr("content"));
				}
				// toPrint(title, doi, summary, date, url, fullurl, type, image,
				// journaltitle, publisher, author, keywords);
			} else if (f.getName().contains("_doi_")
					&& f.getName().endsWith("_full")) {
				// wiley
				// summary
				summary = doc.getElementById("en_main_abstract").text()
						.split("Abstract", 2)[1].trim();
				// publisher
				publisher = "Wiley";
				// image
				Elements ees = doc.getElementsByAttributeValue("class",
						"issue-header__image-wrapper");
				if (ees.size() == 7)
					image = ees.get(4).attr("src");

				Elements es = doc.getElementsByTag("meta");
				for (Element e : es) {
					// title
					if (e.attr("name").equals("citation_title"))
						title = e.attr("content").trim();
					// doi
					else if (e.attr("name").equals("citation_doi"))
						doi = e.attr("content").trim();
					// date
					else if (e.attr("name").equals("citation_online_date"))
						date = e.attr("content").trim().replace("/", "-");
					// url
					else if (e.attr("name")
							.equals("citation_abstract_html_url"))
						url = e.attr("content").trim();
					// fullurl
					else if (e.attr("name")
							.equals("citation_fulltext_html_url"))
						fullurl = e.attr("content").trim();
					// j title
					else if (e.attr("name").equals("citation_journal_title"))
						journaltitle = e.attr("content").trim();
					// author
					else if (e.attr("name").equals("citation_author"))
						author.add(e.attr("content").trim());
					// keywords
					else if (e.attr("name").equals("citation_keywords"))
						author.add(e.attr("content").trim());
				}
				// toPrint(title, doi, summary, date, url, fullurl, type, image,
				// journaltitle, publisher, author, keywords);
			} else if (f.getName().contains("_doi_")) {
				// ACS
				// url
				Elements es = doc.getElementsByAttributeValue("type",
						"application/atom+xml");
				url = es.get(0).attr("href");

				// image
				es = doc.getElementsByAttributeValue("alt", "Abstract Image");
				image = "http://pubs.acs.org" + es.get(0).attr("src");

				// full url
				es = doc.getElementsByAttributeValueStarting("href",
						"/doi/full");
				fullurl = "http://pubs.acs.org" + es.get(0).attr("href");

				es = doc.getElementsByTag("meta");
				for (Element e : es) {
					// title
					if (e.attr("name").equals("dc.Title"))
						title = e.attr("content").trim();
					// doi
					else if (e.attr("name").equals("dc.Identifier"))
						doi = e.attr("content").trim();
					// type
					else if (e.attr("name").equals("dc.Type"))
						type = e.attr("content").trim().replace("-", " ");
					// abstract
					else if (e.attr("name").equals("dc.Description")) {
						if (summary == null || summary.length() == 0)
							summary = e.attr("content").trim();
					}
					// date
					else if (e.attr("name").equals("dc.Date")) {
						String[] tmp = e.attr("content").replace(",", "")
								.trim().split(" ");
						date = tmp[2] + "-" + month.get(tmp[0].toLowerCase())
								+ "-" + tmp[1];
					}
					// publisher
					// journal title
					else if (e.attr("name").equals("dc.Publisher")) {
						publisher = e.attr("content").trim();
						journaltitle = publisher;
					}
					// author
					else if (e.attr("name").equals("dc.Creator"))
						author.add(e.attr("content").trim());
					// keywords
					else if (e.attr("name").equals("citation_keywords"))
						author.add(e.attr("content").trim());
				}
//				toPrint(title, doi, summary, date, url, fullurl, type, image,
//						journaltitle, publisher, author, keywords);
			} else {
				// Nature
				//image
				Elements es = doc.getElementsByAttributeValueStarting("src", "/n");
				image = "http://www.nature.com"+es.get(0).attr("src");
				
				//url
				// full url
				url = "http://www.nature.com"+f.getName().replace("_", "/");
				fullurl = url;
				es = doc.getElementsByTag("meta");
				for (Element e : es) {
					// abstract
					if (e.attr("name").equals("description"))
						summary = e.attr("content").trim();
					// title
					if (e.attr("name").equals("DC.title"))
						title = e.attr("content").trim();
					// date
					else if (e.attr("name").equals("DC.date"))
						date = e.attr("content").trim();
					// doi
					if (e.attr("name").equals("DC.identifier"))
						doi = e.attr("content").replace("doi:"," ").trim();
					else if (e.attr("name")
							.equals("citation_abstract_html_url"))
						url = e.attr("content").trim();
					else if (e.attr("name").equals("citation_full_html_url"))
						fullurl = e.attr("content").trim();
					//type
					else if (e.attr("name").equals("prism.section"))
						type = e.attr("content").trim();
					else if (e.attr("name").equals("og:image"))
						image = e.attr("content").trim();
					// j title
					else if (e.attr("name").equals("citation_journal_title"))
						journaltitle = e.attr("content").trim();
					// publisher
					else if (e.attr("name").equals("citation_publisher"))
						publisher = e.attr("content").trim();
					// author
					else if (e.attr("name").equals("citation_author"))
						author.add(e.attr("content"));
					// keywords
					else if (e.attr("name").equals("keywords")){
						for (String each : e.attr("content").split(", ")){
							keywords.add(each);
						}
					}
				}
				toPrint(title, doi, summary, date, url, fullurl, type, image,
						journaltitle, publisher, author, keywords);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return null;
	}

	public void toPrint(String s1, String s2, String s3, String s4, String s5,
			String s6, String s7, String s8, String s9, String s10,
			ArrayList<String> s11, ArrayList<String> s12) {
		if (s1.length() != 0)
			System.out.print(s1 + "\n");
		if (s2.length() != 0)
			System.out.print(s2 + "\n");
		if (s3.length() != 0)
			System.out.print(s3 + "\n");
		if (s4.length() != 0)
			System.out.print(s4 + "\n");
		if (s5.length() != 0)
			System.out.print(s5 + "\n");
		if (s6.length() != 0)
			System.out.print(s6 + "\n");
		if (s7.length() != 0)
			System.out.print(s7 + "\n");
		if (s8.length() != 0)
			System.out.print(s8 + "\n");
		if (s9.length() != 0)
			System.out.print(s9 + "\n");
		if (s10.length() != 0)
			System.out.print(s10 + "\n");
		if (s12.size() != 0)
			System.out.println(s12.toString());
		if (s11.size() != 0)
			System.out.println(s11.toString() + "\n");
	}
}
