package com.whalegoods.mapper;


import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DeviceRoadMapper extends BaseMapper<SysUser,String> {

    @Override
    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    @Override
    int insertSelective(SysUser record);

    @Override
    SysUser selectByPrimaryKey(String id);

    @Override
    int updateByPrimaryKeySelective(SysUser record);

    @Override
    int updateByPrimaryKey(SysUser record);

    SysUser login(@Param("username") String username);

    @Override
    List<SysUser> selectListByPage(SysUser sysUser);

    int count();

    int add(SysUser user);

    int delById(String id);

    int checkUser(String username);

    int rePass(SysUser user);

    List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> condition);
    
    ResDeviceGoodsInfo selectByPathOrGoodsCode(Map<String,Object> condition);
    
    int updateDeviceRoad(DeviceRoad model);
}