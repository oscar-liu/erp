package com.whalegoods.api.test;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.http.MediaType;*/
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;

import com.whalegoods.entity.Device;
import com.whalegoods.service.DeviceService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


public class V1GoodsControllerTest extends BaseControllerTest {
	
	private final static Logger logger=LoggerFactory.getLogger(V1GoodsControllerTest.class);
	
	@Autowired
	DeviceService deviceService;

	/**
	 * 验证支付密码
	 * @throws Exception
	 */
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
    				logger.info("单元测试>>根据设备编号获取所有商品信息API>>执行结果:{}",result.getResponse().getContentAsString());
    			}
    		});
    	}

    }
    
//	/**
//	 * 设置支付密码
//	 * @throws Exception
//	 */
//    @Test
//    public void setPaypwd() throws Exception{
//    	ResultActions postResultAction=mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/v1/set_pay_pwd?memId="+memId+"&pay_pwd=123456"))
//    			.andExpect(status().isOk())
//    			.andExpect(jsonPath("$.status",is(0)));
//    	
//    	postResultAction.andDo(new ResultHandler() {
//			@Override
//			public void handle(MvcResult result) throws Exception {
//				logger.info("单元测试>>设置支付密码API>>执行结果:{}",result.getResponse().getContentAsString());
//
//			}
//		});
//    }
//    
//    /**
//     * 修改支付密码
//     * @throws Exception
//     */
//    @Test
//    public void updatePaypwd() throws Exception{
//    	RequestUpdatePaypwd requestUpdatePaypwd=new RequestUpdatePaypwd();
//    	requestUpdatePaypwd.setMemId(memId);
//    	requestUpdatePaypwd.setOld_pay_pwd("123456");
//    	requestUpdatePaypwd.setNew_pay_pwd("123456");
//    	ResultActions postResultAction=mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/v1/update_pay_pwd")
//    			.contentType(MediaType.APPLICATION_JSON_UTF8)
//    			.content(JsonUtils.beanToJson(requestUpdatePaypwd)))
//    			.andExpect(status().isOk())
//    			.andExpect(jsonPath("$.status",is(0)));
//    	
//    	postResultAction.andDo(new ResultHandler() {
//			@Override
//			public void handle(MvcResult result) throws Exception {
//				logger.info("单元测试>>修改支付密码API>>正确的旧密码>>执行结果:{}",result.getResponse().getContentAsString());;
//			}
//		});
//    	
//    	requestUpdatePaypwd.setMemId(memId);
//    	requestUpdatePaypwd.setOld_pay_pwd("1233456");
//    	requestUpdatePaypwd.setNew_pay_pwd("123456");
//        postResultAction=mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/v1/update_pay_pwd")
//    			.contentType(MediaType.APPLICATION_JSON_UTF8)
//    			.content(JsonUtils.beanToJson(requestUpdatePaypwd)))
//    			.andExpect(status().isOk())
//    			.andExpect(jsonPath("$.status",is(7017)));
//    	
//    	postResultAction.andDo(new ResultHandler() {
//			@Override
//			public void handle(MvcResult result) throws Exception {
//				logger.info("单元测试>>修改支付密码API>>不正确的旧密码>>执行结果:{}",result.getResponse().getContentAsString());;
//			}
//		});
//    }

}