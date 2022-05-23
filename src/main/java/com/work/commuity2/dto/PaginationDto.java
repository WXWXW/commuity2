package com.work.commuity2.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDto<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer totalPage;

    private Integer nowPage;

    private List<Integer> pages = new ArrayList<>();




    public void setPagination(Integer totalPage, Integer nowPage) {

        this.totalPage=totalPage;

        this.nowPage=nowPage;




            pages.add(nowPage);
            for(int i=1;i<=2;i++){

                if(nowPage-i>0){
                    pages.add(0,nowPage-i);
                }

                if(nowPage+i<=totalPage){
                    pages.add(nowPage+i);
                }

            }



            if(nowPage==1){                 //是否展示上一页
                showPrevious=false;
            }else{
                showPrevious=true;
            }

            if(nowPage==totalPage){          //是否展示下一页
                showNext=false;
            }else {
                showNext=true;
            }

            if(pages.contains(1)){           //是否展示第一页
                showFirstPage=false;
            }else {
                showFirstPage=true;
            }

            if(pages.contains(totalPage)){    //是否展示最后一页
                showEndPage=false;
            }else {
                showEndPage=true;
            }
    }


}
