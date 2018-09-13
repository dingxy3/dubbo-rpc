package utils;

/**
 * Created by Administrator on 2017-9-18.
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shuang.gao  Date: 2012/1/28 Time: 12:26
 */
public class CommPageUtils<T> {
    /**
     * 得到分页后的数据
     *
     * @param pageNum 页码
     * @return 分页后结果
     */
    public List<T> getPageData(List<T> data, int pageSize, int pageNum) {
        if(null == data || data.isEmpty()) {
            return Collections.emptyList();
        }

        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= data.size()) {
            return Collections.emptyList();
        }

        int toIndex = pageNum * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }

    public int getPageCount(List<T> data, int pageSize) {
        int count = data.size();
        int quotient = count / pageSize;
        int remainder = count % pageSize;
        int pageCount = quotient;
        if(remainder > 0) {
            ++ pageCount;
        }

        return pageCount;
    }

    public static void main(String[] args) {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        List<Integer> list = Arrays.asList(array);

        CommPageUtils<Integer> pager = new CommPageUtils<Integer>();

        /*List<Integer> page1 = pager.getPageData(list, 10, 10);
        System.out.println(page1);

        List<Integer> page2 = pager.getPageData(list, 5, 4);
        System.out.println(page2);

        List<Integer> page3 = pager.getPageData(list, 3, 3);
        System.out.println(page3);*/


        System.out.println(pager.getPageCount(list, 3 ));
    }
}