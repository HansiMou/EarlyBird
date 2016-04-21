import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
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
	public static HashSet<String> importtitle = new HashSet<String>();

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
		if (month.size() != 12) {
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

		if (importtitle.size() == 0) {
			importtitle.add("Angewandte Chemie International Edition");
			importtitle.add("Science");
			importtitle.add("Nano Lett.");
			importtitle.add("ACS Nano");
			importtitle.add("J. Am. Chem. Soc.");
			importtitle.add("Nature");
			importtitle.add("Acc. Chem. Res.");
			importtitle.add("Nature Materials");
			importtitle.add("Chem. Rev.");

		}
	}

	/**
	 * Description:
	 * 
	 * @param f
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Document getDocument(File f) {
		intialize();

		String title = "";
		String doi = "";
		String summary = "";
		String date = "";
		String url = "";
		String fullurl = "";
		String type = "";
		StringBuilder image = new StringBuilder("");
		String journaltitle = "";
		String publisher = "";
		StringBuilder author = new StringBuilder();
		StringBuilder keywords = new StringBuilder();

		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(f, "UTF-8", "");
			if (f.getName().contains("_content_")) {
				// Sci
				Elements tt = null;
				try {
					tt = doc.getElementsByAttributeValue("class",
							"section abstract");
					if (tt.size() >= 1) {
						summary = tt.get(0).text();
					}
					// no abstract, use summary
					if (summary == null || summary.length() == 0) {
						summary = doc
								.getElementsByAttributeValue("class",
										"section summary").get(0).text();
						if (summary.contains("Summary")) {
							summary = summary.split("Summary", 2)[1].trim();
						}
					} else {
						summary = summary.split("Abstract", 2)[1].trim();
					}
					doi = doc.getElementsByAttributeValue("class", "meta-line")
							.text().replace("DOI:", "\nDOI:").trim();
				} catch (Exception ex) {
					System.out.println(f.getName());
					System.out.println(tt.size() + "\t" + summary);
					ex.printStackTrace();
				} finally {
					Elements es = doc.getElementsByTag("meta");
					for (Element e : es) {
						try {
							if (e.attr("name").equals("DC.Title"))
								title = e.attr("content").trim();
							else if (e.attr("name").equals("DC.Date"))
								date = e.attr("content").trim();
							else if (e.attr("name").equals(
									"citation_abstract_html_url"))
								url = e.attr("content").trim();
							else if (e.attr("name").equals(
									"citation_full_html_url"))
								fullurl = e.attr("content").trim();
							else if (e.attr("name").equals(
									"citation_journal_title"))
								journaltitle = e.attr("content").trim();
							else if (e.attr("name").equals("DC.Publisher"))
								publisher = e.attr("content").trim();
							else if (e.attr("name").equals("citation_section"))
								type = e.attr("content").trim();
							else if (e.attr("name").equals("DC.Contributor"))
								author.append(e.attr("content").trim() + ", ");
						} catch (Exception ex1) {
							continue;
						}
					}
				}
				// toPrint(title, doi, summary, date, url, fullurl, type, image,
				// journaltitle, publisher, author, keywords);
			} else if (f.getName().contains("_doi_")
					&& f.getName().endsWith("_full")) {
				// wiley
				try {
					// type
					Elements tt = doc.getElementsByAttributeValue("class",
							"article-header__category article-category");
					if (tt.size() >= 1)
						type = tt.get(0).text();

					// summary
					summary = doc.getElementById("abstract").text()
							.split("Abstract", 2)[1].trim();
					// publisher
					publisher = "Wiley";
					// image
					image.append(WebCrawlerWiley.imagecache.get(f.getName()
							.replace("_doi_10.1002_", "").split("_")[0]));
				} catch (Exception ex) {
					System.out.println(f.getName());
					ex.printStackTrace();
				} finally {

					Elements es = doc.getElementsByTag("meta");
					for (Element e : es) {
						try {
							// title
							if (e.attr("name").equals("citation_title"))
								title = e.attr("content").trim();
							// doi
							else if (e.attr("name").equals("citation_doi"))
								doi = e.attr("content").trim();
							// date
							else if (e.attr("name").equals(
									"citation_online_date"))
								date = e.attr("content").trim()
										.replace("/", "-");
							// url
							else if (e.attr("name").equals(
									"citation_abstract_html_url"))
								url = e.attr("content").trim();
							// fullurl
							else if (e.attr("name").equals(
									"citation_fulltext_html_url"))
								fullurl = e.attr("content").trim();
							// j title
							else if (e.attr("name").equals(
									"citation_journal_title"))
								journaltitle = e.attr("content").trim();
							// author
							else if (e.attr("name").equals("citation_author"))
								author.append(e.attr("content").trim() + ", ");
							// keywords
							else if (e.attr("name").equals("citation_keywords"))
								keywords.append(e.attr("content").trim() + ", ");
						} catch (Exception ex1) {
							continue;
						}
					}
				}
				// toPrint(title, doi, summary, date, url, fullurl, type, image,
				// journaltitle, publisher, author, keywords);
			} else if (f.getName().contains("_doi_")) {
				// ACS
				try {
					// url
					Elements es = doc.getElementsByAttributeValue("type",
							"application/atom+xml");
					url = es.get(0).attr("href");

					// journal title
					es = doc.getElementsByAttributeValue("for", "qsTitleButton");
					journaltitle = es.get(0).text();

					// image
					String[] tmp = f.getName().split("_");
					image.append(WebCrawlerACS.imagecache
							.get(tmp[tmp.length - 1]));

					// full url
					es = doc.getElementsByAttributeValueStarting("href",
							"/doi/full");
					fullurl = "http://pubs.acs.org" + es.get(0).attr("href");
				} catch (Exception ex) {
					System.out.println(f.getName());
					ex.printStackTrace();
				} finally {
					Elements es = doc.getElementsByTag("meta");
					for (Element e : es) {
						try {
							// title
							if (title.length() == 0
									&& e.attr("name").equals("dc.Title"))
								title = e.attr("content").trim();
							// doi
							else if (e.attr("name").equals("dc.Identifier"))
								doi = e.attr("content").trim();
							// type
							else if (e.attr("name").equals("dc.Type"))
								type = e.attr("content").trim()
										.replace("-", " ");
							// abstract
							else if (e.attr("name").equals("dc.Description")) {
								if (summary == null || summary.length() == 0)
									summary = e.attr("content").trim();
							}
							// date
							else if (e.attr("name").equals("dc.Date")) {
								String[] tmp = e.attr("content")
										.replace(",", "").trim().split(" ");
								date = tmp[2] + "-"
										+ month.get(tmp[0].toLowerCase()) + "-"
										+ tmp[1];
							}
							// publisher
							else if (e.attr("name").equals("dc.Publisher")) {
								publisher = e.attr("content").trim();
							}
							// author
							else if (e.attr("name").equals("dc.Creator"))
								author.append(e.attr("content").trim() + ", ");
							// keywords
							else if (e.attr("name").equals("citation_keywords"))
								keywords.append(e.attr("content").trim() + ", ");
						} catch (Exception ex1) {
							System.out.println(f.getName());
							ex1.printStackTrace();
							continue;
						}
					}
				}
				// toPrint(title, doi, summary, date, url, fullurl, type, image,
				// journaltitle, publisher, author, keywords);
			} else {
				// Nature
				try {
					// image
					Elements es = doc.getElementsByAttribute("data-media-desc");
					for (Element e : es) {
						image.append("http://www.nature.com" + e.attr("src")
								+ ",");
					}
					if (image.toString().endsWith(","))
						image.deleteCharAt(image.length() - 1);
					// keywords
					es = doc.getElementsByAttributeValueStarting("href",
							"/subjects/");
					for (Element e : es) {
						keywords.append(e.text() + ", ");
					}
					// url
					// full url
					url = "http://www.nature.com"
							+ f.getName().replace("_", "/");
					fullurl = url;
				} catch (Exception ex) {
					System.out.println(f.getName());
					ex.printStackTrace();
				} finally {
					Elements es = doc.getElementsByTag("meta");
					for (Element e : es) {
						try {
							// abstract
							if (e.attr("name").equals("description"))
								summary = e.attr("content").trim();
							// title
							else if (title.length() == 0
									&& e.attr("name").equals("DC.title"))
								title = e.attr("content").trim();
							// date
							else if (e.attr("name").equals("DC.date"))
								date = e.attr("content").trim();
							// doi
							else if (e.attr("name").equals("DC.identifier"))
								doi = e.attr("content").replace("doi:", " ")
										.trim();
							else if (e.attr("name").equals(
									"citation_abstract_html_url"))
								url = e.attr("content").trim();
							else if (e.attr("name").equals(
									"citation_full_html_url"))
								fullurl = e.attr("content").trim();
							// type
							else if (e.attr("name").equals("prism.section"))
								type = e.attr("content").trim();
							// j title
							else if (e.attr("name").equals(
									"citation_journal_title"))
								journaltitle = e.attr("content").trim();
							// publisher
							else if (e.attr("name")
									.equals("citation_publisher"))
								publisher = e.attr("content").trim();
							// author
							else if (e.attr("name").equals("citation_author"))
								author.append(e.attr("content").trim() + ", ");
						} catch (Exception ex1) {
							System.out.println(f.getName());
							ex1.printStackTrace();
						}
					}
				}
				// toPrint(title, doi, summary, date, url, fullurl, type, image,
				// journaltitle, publisher, author, keywords);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long time = sdf.parse(date).getTime();
			if (importtitle.contains(title.trim())) {
				org.apache.lucene.document.Document doc1 = new org.apache.lucene.document.Document();
				Field tf = new TextField("title", title, Store.YES);
				tf.setBoost(1.6f);
				doc1.add(tf);
				
				Field abf = new TextField("abstract", summary, Store.YES);
				abf.setBoost(1.2f);
				doc1.add(abf);

				doc1.add(new StoredField("doi", doi));
				doc1.add(new LongField("date", time, Store.YES));
				doc1.add(new StoredField("url", url));
				doc1.add(new StoredField("fullurl", fullurl));
				doc1.add(new StoredField("type", type));
				doc1.add(new StoredField("image", image.toString()));
				doc1.add(new StoredField("journaltitle", journaltitle));
				doc1.add(new StoredField("publisher", publisher));
				Field af = new TextField("authors",
						author.length() > 2 ? author.substring(0,
								author.length() - 2) : author.toString(),
						Store.YES);
				af.setBoost(1.6f);
				doc1.add(af);
				Field kf = new TextField("keywords",
						keywords.length() > 2 ? keywords.substring(0,
								keywords.length() - 2) : keywords.toString(),
						Store.YES);
				kf.setBoost(1.4f);
				doc1.add(kf);
				// if (image.length() > 0)
				// System.out.println(image);

				return doc1;
			} else {
				org.apache.lucene.document.Document doc1 = new org.apache.lucene.document.Document();
				Field tf = new TextField("title", title, Store.YES);
				tf.setBoost(1.2f);
				doc1.add(tf);
				doc1.add(new StoredField("doi", doi));
				doc1.add(new TextField("abstract", summary, Store.YES));
				doc1.add(new LongField("date", time, Store.YES));
				doc1.add(new StoredField("url", url));
				doc1.add(new StoredField("fullurl", fullurl));
				doc1.add(new StoredField("type", type));
				doc1.add(new StoredField("image", image.toString()));
				doc1.add(new StoredField("journaltitle", journaltitle));
				doc1.add(new StoredField("publisher", publisher));
				Field af = new TextField("authors",
						author.length() > 2 ? author.substring(0,
								author.length() - 2) : author.toString(),
						Store.YES);
				af.setBoost(1.2f);
				doc1.add(af);
				Field kf = new TextField("keywords",
						keywords.length() > 2 ? keywords.substring(0,
								keywords.length() - 2) : keywords.toString(),
						Store.YES);
				kf.setBoost(1.1f);
				doc1.add(kf);
				// if (image.length() > 0)
				// System.out.println(image);

				return doc1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
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
