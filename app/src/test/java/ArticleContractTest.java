import static com.studio.mpak.newsby.util.AppUtil.join;

import com.studio.mpak.newsby.data.article.ArticleContract;
import com.studio.mpak.newsby.data.category.CategoryContract;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;
import com.studio.mpak.newsby.parser.VacancyParser;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
}
