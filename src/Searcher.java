import java.io.File;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
			String[] ff = { "title", "author", "keywords", "abstract" };
			Occur[] oc = new Occur[4];
			Arrays.fill(oc, Occur.SHOULD);
			Query query = MultiFieldQueryParser.parse(stringQuery, ff, oc,
					new StandardAnalyzer());

			TopDocs rs = searcher.search(query, 50);
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
					"<span style=\"background:yellow\">", "</span>");
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
					new QueryScorer(query));
			highlighter.setTextFragmenter(new SimpleFragmenter(1024));
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			if (rs.scoreDocs.length == 0)
				System.out
						.println("<div class=\"blog-abs\">No results found.</div>");
			else
				System.out.println("<div class=\"blog-abs\">Show the top "
						+ rs.scoreDocs.length + " results.</div>");
			for (int i = 0; i < rs.scoreDocs.length; i++) {
				Document doc = searcher.doc(rs.scoreDocs[i].doc);
				String title = doc.get("title");
				String abs = doc.get("abstract");
				String authors = doc.get("authors");
				String keywords = doc.get("keywords");

				TokenStream tokenStream = new StandardAnalyzer().tokenStream(
						"title", new StringReader(title));
				String titleh = highlighter.getBestFragment(tokenStream, title);

				tokenStream = new StandardAnalyzer().tokenStream("abs",
						new StringReader(abs));
				String absh = highlighter.getBestFragment(tokenStream, abs);

				tokenStream = new StandardAnalyzer().tokenStream("authors",
						new StringReader(authors));
				String authorsh = highlighter.getBestFragment(tokenStream,
						authors);

				System.out
						.println("<article class=\"blog-main\"><span class=\"am-badge am-badge-success am-radius\">"
								+ doc.get("type")
								+ "</span><div class=\"blog-title\"><a href=\""
								+ doc.get("fullurl")
								+ "\">"
								+ titleh
								+ "</a></h3>");

				System.out
						.println("<div class=\"am-article-meta blog-meta blog-authors\">"
								+ doc.get("authors") + "</div>");
				String[] tmp = keywords.split(",");
				for (int i1 = 0; i1 < tmp.length; i1++) {
					System.out
							.print("<span class=\"am-badge am-round am-text-sm\">"
									+ tmp[i1] + "</span>");
					if (i1 != tmp.length - 1)
						System.out.print("&nbsp;");
				}
				java.util.Date dt = new Date(Long.valueOf(doc.get("date")));
				String sDateTime = sdf.format(dt);
				System.out
						.println("<div class=\"am-article-meta blog-meta blog-date\">"
								+ "Online Date: " + sDateTime);
				System.out.println("&nbsp;DOI:" + doc.get("doi") + "</div>");
				String img = doc.get("image");
				if (img != null && img.trim().length() > 0)
					System.out
							.println("<div class=\"am-g blog-content\">"
									+ "<div class=\"am-u-lg-4\">"
									+ "<div data-am-widget=\"slider\" class=\"am-slider am-slider-default\" data-am-slider='{&quot;animation&quot;:&quot;slide&quot;,&quot;slideshow&quot;:false}' >"
									+ "<ul class=\"am-slides\">");
				if (img != null && img.trim().length() > 0) {
					for (String im : img.split(",")) {
						System.out.println("<li><img src=\"" + im + "\"></li>");
					}
				}
				if (img != null && img.trim().length() > 0)
					System.out.println("</ul>" + "</div> </div></div>");
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
}
