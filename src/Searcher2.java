import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		try {
			File file = new File("downloads/_doi_10.1002_asia.201600200_full");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
//				String ss = new String(tempString.getBytes(), "utf-8");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		// try {
		// Directory directory = FSDirectory.open(new File(config.indexfolder)
		// .toPath());
		// DirectoryReader reader = DirectoryReader.open(directory);
		// IndexSearcher searcher = new IndexSearcher(reader);
		//
		// String[] stringQuery = new String[4];
		// Arrays.fill(stringQuery, args[0]);
		// String[] ff = { "title", "author", "keywords", "abstract" };
		// Occur[] oc = new Occur[4];
		// Arrays.fill(oc, Occur.SHOULD);
		// Query query = MultiFieldQueryParser.parse(stringQuery, ff, oc,
		// new StandardAnalyzer());
		//
		// TopDocs rs = searcher.search(query, 50);
		// SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
		// "<span style=\"background:yellow\">", "</span>");
		// Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
		// new QueryScorer(query, "title"));
		// highlighter.setTextFragmenter(new SimpleFragmenter(1024));
		// SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		// for (int i = 0; i < 1; i++) {
		// Document doc = searcher.doc(rs.scoreDocs[i].doc);
		// System.out.print(doc.get("title"));
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.print("---------");
	}
}
