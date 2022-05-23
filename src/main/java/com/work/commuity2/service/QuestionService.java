package com.work.commuity2.service;


import com.work.commuity2.dto.PaginationDto;
import com.work.commuity2.dto.QuestionDto;
import com.work.commuity2.dto.QusetionQueryDto;
import com.work.commuity2.expection.CustomErrorCode;
import com.work.commuity2.expection.CustomException;
import com.work.commuity2.mapper.QuestionExtMapper;
import com.work.commuity2.mapper.QuestionMapper;
import com.work.commuity2.mapper.UserMapper;
import com.work.commuity2.model.Question;
import com.work.commuity2.model.QuestionExample;
import com.work.commuity2.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public void incView(Long id) {

        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }


    public PaginationDto List(String search, Integer nowPage, Integer size) {


        Integer totalPage;      //总页数



        if(!StringUtils.isEmptyOrWhitespace(search)){
            String[] tags = StringUtils.split(search,",");
            search = Arrays
                    .stream(tags)
                    .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));

        }

        QusetionQueryDto qusetionQueryDto = new QusetionQueryDto();
        qusetionQueryDto.setSearch(search);




        PaginationDto paginationDto=new PaginationDto();

        Integer totalCount=(int)questionMapper.countByExample(new QuestionExample());  //总记录数

        if(totalCount%size==0){          //计算总页数
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }


        if(nowPage<1){                //对当前页面做边界处理
            nowPage=1;
        }

        if(nowPage>totalPage){
            nowPage=totalPage;
        }




        paginationDto.setPagination(totalPage,nowPage);

        Integer offset=size*(nowPage-1);               //计算差值


        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");

        qusetionQueryDto.setPage(offset);
        qusetionQueryDto.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(qusetionQueryDto);
        List<QuestionDto> questionDtoList=new ArrayList<>();



        for(Question question:questions ){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        paginationDto.setData(questionDtoList);



       /* System.out.println(paginationDto.toString());*/


        return paginationDto;
    }

    public PaginationDto list(Long userId, Integer nowPage, Integer size) {

        Integer totalPage;


        PaginationDto paginationDto=new PaginationDto();

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount=(int)questionMapper.countByExample(example);



        if(totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }


        if(nowPage<1){
            nowPage=1;
        }

        if(nowPage>totalPage){
            nowPage=totalPage;
        }


        paginationDto.setPagination(totalPage,nowPage);

        Integer offset=size*(nowPage-1);


        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);



        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1,new RowBounds(offset,size));
        List<QuestionDto> questionDtoList=new ArrayList<>();



        for(Question question:questions ){


            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);

        }

        paginationDto.setData(questionDtoList);





        return paginationDto;
    }

    public QuestionDto getById(Long id) {

        Question question = questionMapper.selectByPrimaryKey(id);

        if(question==null){
            throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
        }

        User user=userMapper.selectByPrimaryKey(question.getCreator());

        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        questionDto.setUser(user);
        return questionDto;

    }


    public void createOrUpdate(Question question) {

        System.out.println(question);
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());

            int updated = questionMapper.updateByExampleSelective(updateQuestion,questionExample);
            if(updated!=1){
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public List<QuestionDto> selectRelated(QuestionDto queryDto) {

        if(StringUtils.isEmptyOrWhitespace(queryDto.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDto.getTag(),",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(queryDto.getId());
        question.setTag(regexpTag);

        List<Question> relatedQuestion = questionExtMapper.selectRelated(question);

        List<QuestionDto> questionDtos = relatedQuestion.stream().map(q -> {
            QuestionDto questionDTO = new QuestionDto();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return  questionDtos;

    }
}
