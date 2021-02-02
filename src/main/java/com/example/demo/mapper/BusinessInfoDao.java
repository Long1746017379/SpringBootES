package com.example.demo.mapper;

import com.example.demo.entity.BusinessInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessInfoDao {

    int deleteByPrimaryKey(String businessId);

    int insert(BusinessInfo record);

    int insertSelective(BusinessInfo record);

    BusinessInfo selectByPrimaryKey(String businessId);

    int updateByPrimaryKeySelective(BusinessInfo record);

    int updateByPrimaryKey(BusinessInfo record);

    Long forEachInsert(List<BusinessInfo> businessInfoList);

}