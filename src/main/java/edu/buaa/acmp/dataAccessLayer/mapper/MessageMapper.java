package edu.buaa.acmp.dataAccessLayer.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MessageMapper {
    @Insert(" INSERT INTO message (receiver_id, content) " +
            " VALUES(#{uid},#{content}) ")
    void sendMessage(@Param("content") String content, @Param("uid") String uid);
}
