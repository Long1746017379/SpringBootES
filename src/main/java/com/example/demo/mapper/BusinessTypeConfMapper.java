package com.example.demo.mapper;

import com.example.demo.entity.BusinessTypeConf;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessTypeConfMapper {

    int deleteByPrimaryKey(Long businessTypeId);

    int insert(BusinessTypeConf record);

    int insertSelective(BusinessTypeConf record);

    BusinessTypeConf selectByPrimaryKey(Long businessTypeId);

    int updateByPrimaryKeySelective(BusinessTypeConf record);

    int updateByPrimaryKey(BusinessTypeConf record);

    List<BusinessTypeConf> queryAll();

    Long writeMysql(@Param("businessTypeConfList") List<BusinessTypeConf> businessTypeConfList);

}