package com.looperX.ssm.controller;

import com.looperX.ssm.entity.User;
import com.looperX.ssm.lucene.LuceneIndex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by apple on 17/4/14.
 */

@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping("/q")
    public String search(@RequestParam(value = "q", required = false, defaultValue = "") String q,
                         @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                         Model model,
                         HttpServletRequest request) throws Exception {
        LuceneIndex luceneIndex = new LuceneIndex();
        List<User> userList = luceneIndex.searchBlog(q);
        /**
         * 关于查询之后的分页我采用的是每次分页发起的请求都是将所有的数据查询出来，
         * 具体是第几页再截取对应页数的数据，典型的拿空间换时间的做法，如果各位有什么
         * 高招欢迎受教。
         */
        Integer toIndex = userList.size() >= Integer.parseInt(page) * 5 ? Integer.parseInt(page) * 5 : userList.size();
        List<User> newList = userList.subList((Integer.parseInt(page) - 1) * 5, toIndex);
        model.addAttribute("userList", newList);
        String s = this.genUpAndDownPageCode(Integer.parseInt(page), userList.size(), q, 5, request.getServletContext().
                getContextPath());
        model.addAttribute("pageHtml", s);
        model.addAttribute("q", q);
        model.addAttribute("resultTotal", userList.size());
        model.addAttribute("pageTitle", "搜索关键字'" + q + "'结果页面");

        return "queryResult";
    }

    /**
     * 查询之后的分页
     *
     * @param page
     * @param totalNum
     * @param q
     * @param pageSize
     * @param projectContext
     * @return
     */
    private String genUpAndDownPageCode(int page, Integer totalNum, String q, Integer pageSize, String projectContext) {
        //分页代码，总页数=总的记录数比上每页显示的记录数，如果总的记录数余上每页显示的记录数为0时候，则为两者相除，否则为两者相除+1
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        if (totalPage == 0) {
            return "";
        } else {
            pageCode.append("<nav>");
            pageCode.append("<ul class='pager' >");
            if (page > 1) {
                pageCode.append("<li><a href='" + projectContext + "/q?page=" + (page - 1) + "&q=" + q + "'>上一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
            }
            if (page < totalPage) {
                pageCode.append("<li><a href='" + projectContext + "/q?page=" + (page + 1) + "&q=" + q + "'>下一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
            }
            pageCode.append("</ul>");
            pageCode.append("</nav>");
        }
        return pageCode.toString();
    }
}
