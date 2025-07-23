package org.kobus.spring.domain.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    String getKusIDById(@Param("id") String id);
}