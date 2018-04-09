package com.whalegoods.mapper;


import com.whalegoods.entity.Device;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DeviceMapper extends BaseMapper<Device,String> {

    @Override
    int deleteByPrimaryKey(String id);

    int insert(Device record);

    @Override
    int insertSelective(Device record);

    @Override
    Device selectByPrimaryKey(String id);

    @Override
    int updateByPrimaryKeySelective(Device record);

    @Override
    int updateByPrimaryKey(Device record);

    Device login(@Param("username") String username);

    @Override
    List<Device> selectListByPage(Device Device);

    int count();

    int add(Device user);

    int delById(String id);

    int checkUser(String username);

    int rePass(Device user);
    
    int updateDeviceStatus(Map<String,Object> condition);
    
    int updateClient(Map<String,Object> condition);

    Map<String,Object> getApk(Map<String, Object> condition);

	int getOperateStatus(Map<String, Object> condition);
}