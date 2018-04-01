import static com.studio.mpak.newsby.util.AppUtil.join;

import com.studio.mpak.newsby.data.article.ArticleContract;
import com.studio.mpak.newsby.data.category.CategoryContract;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleContractTest {

    private static final String DEFAULT_URL = "http://www.orshanka.by/?m=";
    private static final String USERNAME = "aliona.fiadziuk";
    private static final String PASSWORD = "aspire1";

    @Test
    public void name() {

        DateTime dt = new DateTime();
        print(dt);

        DateTime newdate = dt.plusMonths(1);
        print(newdate);
        System.out.println(CategoryContract.SQL_INIT_ENTRIES);
        System.out.println(CategoryContract.SQL_CREATE_ENTRIES);


    }

    private void print(DateTime dt) {
        DecimalFormat df = new DecimalFormat("00");
        String month = df.format( dt.getMonthOfYear());  // where January is 1 and December is 12
        int year = dt.getYear();

        System.out.println(year + month);
    }


    @Test
    public void testSQL() {
        System.out.println("SELECT * FROM " + ArticleContract.ArticleEntry.TABLE_NAME
                + " INNER JOIN " + ArticleCategoryContract.ArticleCategoryEntry.TABLE_NAME + " ON "
                + join(ArticleContract.ArticleEntry.TABLE_NAME, ArticleContract.ArticleEntry.COLUMN_CATEGORY) + " = " + join(ArticleCategoryContract.ArticleCategoryEntry.TABLE_NAME, ArticleCategoryContract.ArticleCategoryEntry.COLUMN_CATEGORY_ID) +
                " WHERE " + join(ArticleContract.ArticleEntry.TABLE_NAME, ArticleContract.ArticleEntry.COLUMN_CATEGORY) + " = ?");
    }

    @Test
    public void testLogic() {
        int duration = 3;
        int defaultDuration = 2;
        int result = duration == 0 || duration > defaultDuration ? defaultDuration : duration;
        System.out.println(result);
    }

    @Test
    public void doInBackground() {
        int startPage = 2;
        int skipPages = 16;
        int pageSize = 18;

        int fetchSize;

        if (skipPages < 1) {
            System.out.println("add 1st page");
            fetchSize = pageSize - startPage > 1 ? startPage + 2 : pageSize + 1;
        } else {
            startPage += skipPages - 1;
            fetchSize = pageSize - startPage > 1 ? startPage + 3 : pageSize + 1;
        }


        for (int page = startPage; page < fetchSize; page++) {
            System.out.println("&paged=" + page);
        }

    }

    @Test
    public void nameConnect() throws IOException {



        Connection.Response loginForm = Jsoup.connect("https://schools.by/login")
                .method(Connection.Method.GET)
                .execute();

        Document doc = loginForm.parse();
        // eventually get hidden fields generated dinamically by the server
//        Elements urlFwdEl = doc.getElementsByAttributeValue("name", "urlFwd");
//        String forwardingUrl = urlFwdEl.get(0).val();
        Elements tokenEl = doc.getElementsByAttributeValue("name", "csrfmiddlewaretoken");
        String token = tokenEl.get(0).val();

        // 2. POST authentication
        Map<String, String> cookies = new LinkedHashMap<String, String>();
        cookies.put("cookie_id", "514274027446641498");
        cookies.put("csrftoken", "NhMKCYEvrcp9OrmjqnOnRc6GRwVOdYv5");
        Connection.Response auth = Jsoup.connect("https://12vitebsk.schools.by/parent/1139080")
                .timeout(10*1000)
                .header("Content-Type", "application/json")
//                .userAgent("Firefox ...")
//                .referrer("https://12vitebsk.schools.by")
//                .data("username", USERNAME)
//                .data("password", PASSWORD)

//                .data("urlFwd", forwardingUrl)
//                .data("csrfmiddlewaretoken", token)
                .cookies(cookies) // important!
                .method(Connection.Method.POST)
                .execute();



        System.out.println(auth);




    }
}
