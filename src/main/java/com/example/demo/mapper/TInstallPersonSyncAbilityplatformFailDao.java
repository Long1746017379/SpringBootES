package com.example.demo.mapper;

import com.example.demo.entity.abilityplatform.po.TInstallPersonSyncAbilityplatformFail;

public interface TInstallPersonSyncAbilityplatformFailDao {

    int deleteByPrimaryKey(Long installPersonId);

    int insert(TInstallPersonSyncAbilityplatformFail record);

    int insertSelective(TInstallPersonSyncAbilityplatformFail record);

    TInstallPersonSyncAbilityplatformFail selectByPrimaryKey(Long installPersonId);

    int updateByPrimaryKeySelective(TInstallPersonSyncAbilityplatformFail record);

    int updateByPrimaryKey(TInstallPersonSyncAbilityplatformFail record);
}