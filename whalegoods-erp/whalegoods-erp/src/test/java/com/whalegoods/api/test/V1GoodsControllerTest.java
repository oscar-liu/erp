/*package com.whalegoods.api.test;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;

import com.whalegoods.entity.Device;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.LogUtil;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


public class V1GoodsControllerTest extends BaseControllerTest {
	
	@Autowired
	DeviceService deviceService;

	*//**
	 * 验证支付密码
	 * @throws Exception
	 *//*
    @Test
    public void getListByDeviceCode() throws Exception {
    	List<Device> listDevice=deviceService.selectListByObjCdt(new Device());
    	if(listDevice.size()>0){
    		Device device=listDevice.get(0);
        	ResultActions getResultAction=mockMvc.perform(MockMvcRequestBuilders.get("/v1/goods/getListByDeviceCode?device_code_wg="+device.getDeviceIdJp()+"&device_code_sup="+device.getDeviceIdSupp()))
        			.andExpect(status().isOk())
        			.andExpect(jsonPath("$.result_code",is(0)));
        	
        	getResultAction.andDo(new ResultHandler() {
    			@Override
    			public void handle(MvcResult result) throws Exception {
    				LogUtil.printInfoLog("单元测试>>根据设备编号获取所有商品信息API>>执行结果:{}",result.getResponse().getContentAsString());
    			}
    		});
    	}

    }

}*/