package com.qyl.community.controller;


import com.qyl.community.mapper.QuestionMapper;
import com.qyl.community.mapper.UserMapper;
import com.qyl.community.model.Question;
import com.qyl.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by codedrinker on 2019/5/2.
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(Model model) {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }

        if (StringUtils.length(title) > 50) {
            model.addAttribute("error", "标题最多 50 个字符");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user= userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if (user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question record = new Question();
        record.setTitle(title);
        record.setTag(tag);
        record.setDescription(description);
        record.setCreator(user.getId());
        record.setGmtCreate(System.currentTimeMillis());
        record.setGmtModified(record.getGmtCreate());
        questionMapper.insert(record);

        return "redirect:/";

    }
}
