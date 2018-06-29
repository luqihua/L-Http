package lu.httpdemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("string===" + i);
        }


//        for (String s : list) {
//            if (s.equals("string===2")) {
//                list.remove(s);
//            }
//        }
        Iterator<String> iterable = list.iterator();

        while (iterable.hasNext()) {
            String s = iterable.next();
            if (s.equals("string===2")) {
                iterable.remove();
            }
        }

        for (String s : list) {
            System.out.println(s);
        }
    }
}