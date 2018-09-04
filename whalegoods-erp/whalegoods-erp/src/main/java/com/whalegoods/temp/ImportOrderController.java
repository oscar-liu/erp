package com.whalegoods.temp;

import com.whalegoods.entity.OrderList;
import com.whalegoods.service.OrderListService;
import com.whalegoods.temp.TempEntity;
import com.whalegoods.util.DateUtil;
import com.whalegoods.util.StringUtil;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/order")
public class ImportOrderController {
	
	private static Logger logger = LoggerFactory.getLogger(ImportOrderController.class);
	
	@Autowired
	private OrderListService orderListService;
	
		@PostMapping("excelImport.do")
		public void excelImport(@RequestParam("file") MultipartFile file) throws IOException, Exception {
			ImportParams importParams = new ImportParams();
			// 数据处理
			IExcelDataHandler<TempEntity> handler = new UserExcelHandler();
			handler.setNeedHandlerFields(new String[] { "姓名" });// 注意这里对应的是excel的列名。也就是对象上指定的列名。
			importParams.setDataHanlder(handler);
			ExcelImportResult<TempEntity> result = ExcelImportUtil.importExcelMore(file.getInputStream(), TempEntity.class,
					importParams);
			List<TempEntity> successList = result.getList();
			List<OrderList> listOrderList=new ArrayList<>();
			for (TempEntity tempEntity : successList) {
				String deviceIdSwg=null;
				switch (tempEntity.getDeviceIdSupp()) {
				case "szsjpzn0012":
					deviceIdSwg="wgd37416";
					break;
				case "szsjpzn0015":
					deviceIdSwg="wgd93471";
					break;
				case "szsjpzn0024":
					deviceIdSwg="wgd10542";
					break;
				case "szsjpzn0003":
					deviceIdSwg="wgd77107";
					break;
				case "szsjpzn0011":
					deviceIdSwg="wgd20424";
					break;
				case "szsjpzn0021":
					deviceIdSwg="wgd22685";
					break;
				case "szsjpzn0006":
					deviceIdSwg="wgd52910";
					break;
				case "szsjpzn0020":
					deviceIdSwg="wgd82446";
					break;
				case "szsjpzn0010":
					deviceIdSwg="wgd67597";
					break;
				case "szsjpzn0009":
					deviceIdSwg="wgd52623";
					break;
				case "szsjpzn0004":
					deviceIdSwg="wgd75438";
					break;
				case "szsjpzn0001":
					deviceIdSwg="wgd74230";
					break;
				case "szsjpzn0022":
					deviceIdSwg="wgd71332";
					break;
				case "szsjpzn0013":
					deviceIdSwg="wgd18002";
					break;
				case "szsjpzn0016":
					deviceIdSwg="wgd82825";
					break;
				case "szsjpzn0023":
					deviceIdSwg="wgd16951";
					break;
				case "szsjpzn0018":
					deviceIdSwg="wgd41847";
					break;
				case "szsjpzn0007":
					deviceIdSwg="wgd90474";
					break;
				case "szsjpzn0014":
					deviceIdSwg="wgd78823";
					break;
				case "szsjpzn0017":
					deviceIdSwg="wgd22433";
					break;
				case "szsjpzn0005":
					deviceIdSwg="wgd47741";
					break;
				case "szsjpzn0008":
					deviceIdSwg="wgd36304";
					break;
				default:
					break;
				}
				OrderList orderList=new OrderList();
				orderList.setId(StringUtil.getUUID());
				orderList.setDeviceIdSupp(tempEntity.getDeviceIdSupp());
				orderList.setDeviceIdJp(deviceIdSwg);
				orderList.setGoodsCode(tempEntity.getGoodsCode());
				orderList.setOrderId(tempEntity.getOrderId());
				orderList.setOrderTime(DateUtil.formatDateTime(tempEntity.getOrderTime()));
				orderList.setGoodsName(tempEntity.getGoodsName().split(" ")[0]);
				switch (tempEntity.getPayWay()) {
				case "微支付":
					orderList.setPayType((byte) 1);
					break;
				case "支付宝二维码":
					orderList.setPayType((byte) 2);
					break;
				default:
					break;
				}
				switch (tempEntity.getOrderStatus()) {
				case "未退款":
					orderList.setOrderStatus((byte) 2);
					break;
				case "退款成功":
					orderList.setOrderStatus((byte) 4);
					break;
				default:
					break;
				}
				orderList.setSalePrice(Double.valueOf(tempEntity.getSalePrice()));
				orderList.setCtn((byte) 1);
				orderList.setPathCode((byte) 2);
				orderList.setFloor((byte) 3);
				orderList.setOrderType((byte) 1);
				orderList.setPayTime(DateUtil.formatDateTime(tempEntity.getOrderTime()));
				orderList.setCreateBy("henry易丰导入");
				orderList.setUpdateBy("henry易丰导入");
				orderList.setPrefix("201805");
				listOrderList.add(orderList);
			}
			orderListService.insertBatch(listOrderList);
			logger.info("数量：{}",successList.size());
		}
		
/*		@PostMapping("changeCost.do")
		public void changeCost(@RequestParam("file") MultipartFile file) throws IOException, Exception {
			ImportParams importParams = new ImportParams();
			// 数据处理
			IExcelDataHandler<TempEntityCost> handler = new UserExcelHandler();
			handler.setNeedHandlerFields(new String[] { "姓名" });// 注意这里对应的是excel的列名。也就是对象上指定的列名。
			importParams.setDataHanlder(handler);
			ExcelImportResult<TempEntityCost> result = ExcelImportUtil.importExcelMore(file.getInputStream(), TempEntityCost.class,importParams);
			List<TempEntityCost> successList = result.getList();
			logger.info("数量：{}",successList.size());
			for (TempEntityCost tempEntityCost : successList) {
				if(tempEntityCost.getSalePrice()==""||tempEntityCost.getSalePrice()==null){
					continue;
				}
				GoodsSku goodsSku=new GoodsSku();
				goodsSku.setGoodsCode(tempEntityCost.getGoodsCode());
				goodsSku.setOneCost(Double.valueOf(tempEntityCost.getSalePrice()));
				goodsSkuService.updateByObjCdt(goodsSku);
			}
		}*/

}
