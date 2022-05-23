package com.work.commuity2.mapper;



import com.work.commuity2.dto.QusetionQueryDto;
import com.work.commuity2.model.Question;

import java.util.List;


public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QusetionQueryDto qusetionQueryDto);

    List<Question> selectBySearch(QusetionQueryDto questionQueryDto);

    List<Question> selectSticky();

}