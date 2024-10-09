package com.haste.yzx.system.api.domain.bo.species;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor

public class SpeciesRspInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String imgPath;
    private String name;
    private String latinName;
    private Integer type;


    private long likeCnt;

    private long commentCnt;

    private long viewCnt;

    private long collectCnt;




    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createTime;



    public void  setCnt(SpeciesInfoBo infoBo){
        this.collectCnt = infoBo.getCollectCnt();
        this.viewCnt = infoBo.getViewCnt();
        this.commentCnt = infoBo.getCommentCnt();
        this.likeCnt = infoBo.getLikeCnt();
    }
}
