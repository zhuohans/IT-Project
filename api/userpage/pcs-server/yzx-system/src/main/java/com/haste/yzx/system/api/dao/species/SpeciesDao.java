package com.haste.yzx.system.api.dao.species;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haste.yzx.system.api.domain.bo.species.FootprintsRspBo;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SpeciesDao extends BaseMapper<SpeciesInfo> {

    /**
     * @see com.haste.yzx.common.enums.UserActEnum
     */
    @Select("select * from (" +
            "select species_id,  case when vieww=1 then 1 " +
            "when vieww=0 and comment is not null then 4 " +
            "end as act ,create_by uid,create_time act_time,t.comment act_msg " +
            "from t_pcs_species_comment t  where  1=1 " +
            "and t.create_by=  #{uid}  " +
            "UNION  " +
            "select species_id, case when likee=1 then 2 " +
            "  when collect =1 then 3" +
            "  end  " +
            "as act ,a.create_by uid,update_time  act_time,'' act_msg  from t_pcs_species_like a " +
            "where 1=1   " +
            "and a.create_by= #{uid} and (a.likee =1  or a.collect =1 )  ) v " +
            "left join (select id,name,latin_name,img_path,create_by from t_pcs_species_info where visible =1 ) f on v.species_id = f.id " +
            " left join (select user_id speciesUid ,username speciesUserName,avatar speciesUserAvatar from t_pcs_sys_user) d on f.create_by = d.speciesUid" +
            " ORDER BY v.act_time  desc ")
    Page<FootprintsRspBo> getFootprintsList(Page page, @Param("uid") String uid);


    @Select("select * from (select species_id,  case when vieww=1 then 1 when vieww=0 and comment is not null then 4 end as act ,create_by uid,create_time act_time,t.comment act_msg " +
            "from t_pcs_species_comment t  where  1=1 " +
            "and t.create_by=  #{uid} and t.vieww=1 " +
            " ) v  " +
            "left join (select id,name,latin_name,img_path,create_by from t_pcs_species_info where visible =1 ) f on v.species_id = f.id " +
            " left join (select user_id speciesUid ,nick_name speciesUserName,avatar speciesUserAvatar from t_pcs_sys_user) d on f.create_by = d.speciesUid" +

            " ORDER BY v.act_time  desc ")
    Page<FootprintsRspBo> getViewList(Page page, @Param("uid") String uid);

    @Select("select * from (" +

            "select species_id, case when likee=1 then 2 " +
            "  when collect =1 then 3" +
            "  end  " +
            "as act ,a.create_by uid,update_time  act_time,'' act_msg  from t_pcs_species_like a " +
            "where 1=1   " +
            "and a.create_by= #{uid} and  a.likee =1     ) v " +
            "left join (select id,name,latin_name,img_path,create_by from t_pcs_species_info where visible =1 ) f on v.species_id = f.id " +
            " left join (select user_id speciesUid ,nick_name speciesUserName,avatar speciesUserAvatar from t_pcs_sys_user) d on f.create_by = d.speciesUid" +

            " ORDER BY v.act_time  desc ")
    Page<FootprintsRspBo> getLikeList(Page page, @Param("uid") String uid);

    @Select("select * from (" +

            "select species_id, case when likee=1 then 2 " +
            "  when collect =1 then 3" +
            "  end  " +
            "as act ,a.create_by uid,update_time  act_time,'' act_msg  from t_pcs_species_like a " +
            "where 1=1   " +
            "and a.create_by= #{uid} and  a.collect =1     ) v  " +
            "left join (select id,name,latin_name,img_path,create_by from t_pcs_species_info where visible =1 ) f on v.species_id = f.id " +
            " left join (select user_id speciesUid ,nick_name speciesUserName,avatar speciesUserAvatar from t_pcs_sys_user) d on f.create_by = d.speciesUid" +

            " ORDER BY v.act_time  desc ")
    Page<FootprintsRspBo> getCollectList(Page page, @Param("uid") String uid);

    @Select("select * from (select species_id,  case when vieww=1 then 1 when vieww=0 and comment is not null then 4 end as act ,create_by uid,create_time act_time,t.comment act_msg " +
            "from t_pcs_species_comment t  where  1=1 " +
            "and t.create_by=  #{uid} and t.vieww=0 and t.comment is not null  " +
            " ) v " +
            " left join (select id,name,latin_name,img_path,create_by from t_pcs_species_info where visible =1 ) f on v.species_id = f.id " +
            " left join (select user_id speciesUid ,nick_name speciesUserName,avatar speciesUserAvatar from t_pcs_sys_user) d on f.create_by = d.speciesUid" +

            " ORDER BY v.act_time  desc ")
    Page<FootprintsRspBo> getCommentList(Page page, @Param("uid") String uid);


    @Select("select distinct * from (" +
            "select  t.id, t.`name`,t.latin_name,t.img_path,t.create_by ,t.update_time  from  t_pcs_species_info t  where t.create_by=#{uid} " +
            "UNION " +
            "select  t.id, t.`name`,t.latin_name,t.img_path,t.create_by ,a.update_time  from  t_pcs_species_info t " +
            " left join t_pcs_species_like a  on t.id = a.species_id" +
            "  where t.visible = 1 and a.collect = 1 and a.create_by=#{uid}   and t.create_by !=#{uid}   ) ad order by update_time desc")
    Page<SpeciesInfo> getPhotosList(Page page, @Param("uid") String uid);


    @Select(
            "select  t.id, t.`name`,t.latin_name,t.img_path,t.create_by ,a.update_time  from  t_pcs_species_info t " +
                    " join t_pcs_species_like a  on t.id = a.species_id" +
                    "  where t.visible = 1 and a.collect = 1 and a.create_by=#{uid}     order by update_time desc")
    Page<SpeciesInfo> getCollectPhotosList(Page page, @Param("uid") String uid);


    @Select(
            " select sum(collectCnt) collectCnt,sum(likeCnt) likeCnt,sum(viewCnt) viewCnt,sum(commentCnt)  commentCnt from (" +
                    " select count(if(collect =1,1,null)) collectCnt ,  count(if(likee =1,1,null)) likeCnt, 0 as viewCnt,0 as commentCnt  from t_pcs_species_info t " +
                    "    join t_pcs_species_like a  on t.id =  a.species_id where  t.create_by= #{uid}  " +
                    " union     " +
                    " SELECT" +
                    " 0 as collectCnt,0 as likeCnt," +
                    " count( if(vieww = 1 ,1,null)) viewCnt, " +
                    " count( if(vieww != 1 AND `comment` IS NOT NULL,1,null) ) commentCnt  " +
                    " FROM" +
                    " t_pcs_species_info t" +
                    " JOIN t_pcs_species_comment a ON t.id = a.species_id " +
                    " WHERE" +
                    " t.create_by = #{uid} ) a")
    SpeciesInfoBo getUserStatisticNum( @Param("uid") String uid);


}
