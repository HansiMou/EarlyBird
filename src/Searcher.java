import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author Hansi Mou
 * @date Apr 15, 2016
 * @version 1.0
 */

/**
 * @author Hansi Mou
 *
 *         Apr 15, 2016
 */
public class Searcher {
	static Config config = new Config();

	/**
	 * Description:
	 * 
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			Directory directory = FSDirectory.open(new File(config.indexfolder)
					.toPath());
			DirectoryReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);

			String[] stringQuery = new String[4];
			Arrays.fill(stringQuery, args[0]);
			String[] ff = { "title", "authors", "keywords", "abstract" };
			Occur[] oc = new Occur[4];
			Arrays.fill(oc, Occur.SHOULD);
			Query query = MultiFieldQueryParser.parse(stringQuery, ff, oc,
					new StandardAnalyzer());

			TopDocs rs = searcher.search(query, 50);
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
					"<span style=\"background:yellow\">", "</span>");
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
					new QueryScorer(query, "title"));
			highlighter.setTextFragmenter(new SimpleFragmenter(1024));
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			if (rs.scoreDocs.length == 0)
				System.out
						.println("<div class=\"get2\">No results found.</div>");
			else if (rs.scoreDocs.length < 50)
				System.out.println("<div class=\"get2\">Found "
						+ rs.scoreDocs.length + " results.</div>");
			else
				System.out.println("<div class=\"get2\">Showing the top "
						+ rs.scoreDocs.length + " results.</div>");
			for (int i = 0; i < rs.scoreDocs.length; i++) {
				Document doc = searcher.doc(rs.scoreDocs[i].doc);
				String abs = doc.get("abstract");
				String title = doc.get("title");
				String authors = doc.get("authors");
				String keywords = doc.get("keywords");
				TokenStream tokenStream = new StandardAnalyzer().tokenStream(
						"title", new StringReader(title));
				String titleh = "";
				if (!args[0].contains("*") && !args[0].contains("~")) {
					titleh = highlighter.getBestFragment(tokenStream, title);
				} else {
					titleh = title;
				}
				if (titleh == null || titleh.length() == 0)
					titleh = title;
				tokenStream = new StandardAnalyzer().tokenStream("abs",
						new StringReader(abs));
				String absh = "";
				if (!args[0].contains("*") && !args[0].contains("~")) {
					absh = highlighter.getBestFragment(tokenStream, abs);
				} else {
					absh = abs;
				}
				if (absh == null || absh.length() == 0)
					absh = abs;

				tokenStream = new StandardAnalyzer().tokenStream("authors",
						new StringReader(authors));
				String authorsh = "";
				if (!args[0].contains("*") && !args[0].contains("~")) {
					authorsh = highlighter
							.getBestFragment(tokenStream, authors);
				} else {
					authorsh = authors;
				}
				if (authorsh == null || authorsh.length() == 0)
					authorsh = authors;

				tokenStream = new StandardAnalyzer().tokenStream("keywords",
						new StringReader(keywords));
				String keywordsh = "";
				if (!args[0].contains("*") && !args[0].contains("~")) {
					keywordsh = highlighter.getBestFragment(tokenStream,
							keywords);
				} else {
					keywordsh = keywords;
				}
				if (keywordsh == null || keywordsh.length() == 0)
					keywordsh = keywords;

				System.out
						.println("<article class=\"blog-main\"><span class=\"am-badge am-badge-danger am-radius\">"
								+ doc.get("journaltitle").replace("‐", "-")
								+ "</span>&nbsp;"
								+ "<span class=\"am-badge am-badge-success am-radius\">"
								+ doc.get("type")
								+ "</span>"
								+ "<div class=\"blog-title\"><a href=\""
								+ doc.get("fullurl")
								+ "\">"
								+ titleh
								+ "</a></h3>");

				System.out
						.println("<div class=\"am-article-meta blog-meta blog-authors\">"
								+ authorsh.substring(0, authorsh.length()-1) + "</div>");

				java.util.Date dt = new Date(Long.valueOf(doc.get("date")));
				String sDateTime = sdf.format(dt);
				System.out
						.println("<div class=\"am-article-meta blog-meta blog-date\">"
								+ "Online Date: " + sDateTime);
				System.out.println("&nbsp;DOI:" + doc.get("doi") + "</div>");

				String img = doc.get("image");
				if (img != null && img.trim().length() > 0
						&& !img.trim().toLowerCase().equals("null"))
					System.out
							.println("<div class=\"am-g blog-content\"><div class=\"am-u-lg-5\"><div id='ninja-slider"
									+ i
									+ "'><div class=\"slider"
									+ i
									+ "-inner\"><ul>");
				if (img != null && img.trim().length() > 0
						&& !img.trim().toLowerCase().equals("null")) {
					for (String im : img.split(",")) {
						System.out.println("<li><a class=\"ns-img\" href=\""
								+ im + "\"></a></li>");
					}
				}
				if (img != null && img.trim().length() > 0
						&& !img.trim().toLowerCase().equals("null"))
					System.out.println("</ul>" + "</div></div></div> </div>");

				String[] tmp = keywordsh.split(",");
				for (int i1 = 0; i1 < tmp.length - 1; i1++) {
					if (tmp[i1].contains("</span>")) {
						System.out
								.print("<span class=\"am-badge am-badge-warning am-round\">"
										+ tmp[i1]
												.replace(
														"<span style=\"background:yellow\">",
														"").replace("</span>",
														"") + "</span>");
					} else {
						System.out
								.print("<span class=\"am-badge am-round blog-keywords\">"
										+ tmp[i1] + "</span>");
					}
					if (i1 != tmp.length - 1)
						System.out.print("&nbsp;");
				}
				if (absh != null && absh.length() != 0)
					System.out
							.println("<section data-am-widget=\"accordion\" class=\"am-accordion am-accordion-basic\" data-am-accordion='{  }'>"
									+ "<dl class=\"am-accordion-item blog-abs\">"
									+ "<dt class=\"am-accordion-title blog-abs\">"
									+ "Abstract"
									+ "</dt>"
									+ "<dd class=\"am-accordion-bd am-collapse blog-abs\">"
									+ "<div class=\"am-accordion-content blog-abs\">"
									+ absh
									+ "</div>"
									+ "</dd>"
									+ "</dl>"
									+ "</section></div>");

				System.out.println("  </article>");
				if (i != rs.scoreDocs.length - 1) {
					System.out
							.println("<hr class=\"am-article-divider blog-hr\">");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// 用默认字符编码解码字符串。
			byte[] bs = str.getBytes();
			// 用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}
}
