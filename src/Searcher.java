import java.io.File;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
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
	public static void main(String[] args) {
		try {
			Directory directory = FSDirectory.open(new File(config.indexfolder)
					.toPath());
			DirectoryReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			String[] stringQuery = "news effect".split(" ");
			String[] ff = new String[stringQuery.length];
			Arrays.fill(ff, "abstract");
			Query query = MultiFieldQueryParser.parse(stringQuery, ff,
					new StandardAnalyzer());

			TopDocs rs = searcher.search(query, 30);
			// write to a file
			for (int i = 0; i < rs.scoreDocs.length; i++) {
				Document doc = searcher.doc(rs.scoreDocs[i].doc);
				System.out.println(doc.get("title"));
				System.out.println(doc.get("image"));
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
