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
public class Searcher2 {
	static Config config = new Config();
	/**
	 * Description:
	 * 
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
//		try {
//			Directory directory = FSDirectory.open(new File(config.indexfolder)
//					.toPath());
//			DirectoryReader reader = DirectoryReader.open(directory);
//			IndexSearcher searcher = new IndexSearcher(reader);
//
//			String[] stringQuery = new String[4];
//			Arrays.fill(stringQuery, args[0]);
//			String[] ff = { "title", "author", "keywords", "abstract" };
//			Occur[] oc = new Occur[4];
//			Arrays.fill(oc, Occur.SHOULD);
//			Query query = MultiFieldQueryParser.parse(stringQuery, ff, oc,
//					new StandardAnalyzer());
//			
//			TopDocs rs = searcher.search(query, 50);
//			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
//					"<span style=\"background:yellow\">", "</span>");
//			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
//					new QueryScorer(query, "title"));
//			highlighter.setTextFragmenter(new SimpleFragmenter(1024));
//			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//			for (int i = 0; i < 1; i++) {
//				Document doc = searcher.doc(rs.scoreDocs[i].doc);
//				System.out.print(doc.get("title"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.print("---------");
	}
}
