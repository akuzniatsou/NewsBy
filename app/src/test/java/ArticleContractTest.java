import com.studio.mpak.newsby.data.relation.ArticleCategoryContract;
import org.joda.time.DateTime;
import org.junit.Test;

import java.text.DecimalFormat;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleContractTest {

    private static final String DEFAULT_URL = "http://www.orshanka.by/?m=";

    @Test
    public void name() {

        DateTime dt = new DateTime();
        print(dt);


        DateTime newdate = dt.plusMonths(1);
        print(newdate);


    }

    private void print(DateTime dt) {
        DecimalFormat df = new DecimalFormat("00");
        String month = df.format( dt.getMonthOfYear());  // where January is 1 and December is 12
        int year = dt.getYear();

        System.out.println(year + month);
    }


    @Test
    public void testSQL() {
        System.out.println(ArticleCategoryContract.SQL_CREATE_ENTRIES);
    }

    @Test
    public void testLogic() {
        int duration = 3;
        int defaultDuration = 2;
        int result = duration == 0 || duration > defaultDuration ? defaultDuration : duration;
        System.out.println(result);
    }
}
