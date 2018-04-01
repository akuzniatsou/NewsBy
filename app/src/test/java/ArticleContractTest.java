import com.studio.mpak.newsby.data.article.ArticleContract;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;
import com.studio.mpak.newsby.parser.VacancyParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleContractTest {

    @Test
    public void name() throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("example.html");
        Document parse = Jsoup.parse(resourceAsStream, "UTF-8", "www.orshanka.by");
        VacancyParser vacancyParser = new VacancyParser();
        ArrayList<Announcement<Vacancy>> vacancies = vacancyParser.parse(parse);
        System.out.println();
    }

    @Test
    public void testSql() {
        System.out.println(ArticleContract.SQL_CREATE_ENTRIES);
    }
}
