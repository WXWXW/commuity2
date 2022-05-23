package com.work.commuity2.cache;

import com.work.commuity2.dto.TagDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDto> get(){
        ArrayList<TagDto> tagDtos = new ArrayList<>();

        TagDto program = new TagDto();
        program.setCategoryTag("开发语言");
        program.setTags(Arrays.asList("js","php","java","html","java","node","python"));

        TagDto fragwork = new TagDto();
        fragwork.setCategoryTag("开发框架");
        fragwork.setTags(Arrays.asList("spring","springboot","springcloud","mybatis","redis","vue","rabbitmq"));

        TagDto server = new TagDto();
        server.setCategoryTag("服务器");
        server.setTags(Arrays.asList("linux","nginx","tomcat","apache","centos","unix","hadoop"));

        TagDto database = new TagDto();
        database.setCategoryTag("数据库");
        database.setTags(Arrays.asList("mysql","sqlserver","nosql","redis","h2","sqllite","oracle"));

        TagDto tool = new TagDto();
        tool.setCategoryTag("开发工具");
        tool.setTags(Arrays.asList("git","github","visual","idea","maven","svn","unity"));

        tagDtos.add(program);
        tagDtos.add(fragwork);
        tagDtos.add(server);
        tagDtos.add(database);
        tagDtos.add(tool);

        return tagDtos;
    }

    public static String isValited(String tags){

        String[] split = org.apache.commons.lang3.StringUtils.split(tags, ",");
        List<TagDto> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> StringUtils.isBlank(t) || !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;

    }
}
