package com.haste.yzx.system.api.domain.bo.species;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.haste.yzx.system.api.domain.po.user.UserPo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 */
@Data
@NoArgsConstructor
public class FootprintsRspBo {

    /**
    * @see com.haste.yzx.common.enums.UserActEnum
    * */
    private Integer act;
    private String  uid;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long speciesId;
    private String name;
    private String latinName;
    private String imgPath;

    private String  speciesUserName;
    private String  speciesUid;
    private String  speciesUserAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date actTime;


}
