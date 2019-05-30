package com.saopay.apiyouzan.controller;

import com.saopay.apiyouzan.data.pojo.dto.OrderDetail;
import com.saopay.apiyouzan.data.pojo.dto.TradesSoldGetParams;
import com.saopay.apiyouzan.enums.OrderStatusEnum;
import com.saopay.apiyouzan.service.OrderService;
import com.saopay.apiyouzan.util.BaseUtil;
import com.saopay.apiyouzan.util.DateUtil;
import com.saopay.apiyouzan.util.file.ExcelExportUtil;
import com.youzan.open.sdk.gen.v4_0_0.api.YouzanTradesSoldGet;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangding
 * @description
 * @date 2018/11/15 9:55
 */
@Api("有赞订单处理")
@RestController
@RequestMapping("order")
@Slf4j
public class OrderController extends YouZanBaseController {

    @Autowired
    private OrderService orderService;

    /**
     * youzan.trades.sold.get 接口
     */
    @GetMapping("trades-sold")
    @ApiOperation(value = "查询卖家已卖出的交易列表", notes = "https://open.youzan.com/v3/apicenter/doc-api-main/1/2/trade/youzan.trades.sold.get")
    public YouzanTradesSoldGetResult getTradesSold(TradesSoldGetParams tradesSoldGetParams) {
        //交易创建结束时间，例:2017-01-01 12:00:00; 开始时间和结束时间的跨度不能大于3个月; 结束时间必须大于开始时间; 开始时间和结束时间必须成对出现
        if (StringUtils.isNotBlank(tradesSoldGetParams.getNewEndCreated()) && StringUtils
            .isNotBlank(tradesSoldGetParams.getNewStartCreated())) {
            LocalDateTime endCreated = DateUtil
                .parseStringToDateTime(tradesSoldGetParams.getNewEndCreated());
            LocalDateTime startCreated = DateUtil
                .parseStringToDateTime(tradesSoldGetParams.getNewStartCreated());
            tradesSoldGetParams.setEndCreated(DateUtil.parseDateTimeToDate(endCreated));
            tradesSoldGetParams.setStartCreated(DateUtil.parseDateTimeToDate(startCreated));
        }

        YouzanTradesSoldGet youzanTradesSoldGet = new YouzanTradesSoldGet();
        youzanTradesSoldGet.setAPIParams(tradesSoldGetParams);
        YouzanTradesSoldGetResult result = super.youZanBaseUtil.getClient()
            .invoke(youzanTradesSoldGet);
        return result;
    }

    @ApiOperation(value = "获取时间段待发货的订单数据Excel文件", notes = "开始时间和结束时间不可以大于超过3个月")
    @GetMapping("bill-of-sales")
    public String billOfSalesExcel(
        @ApiParam(value = "结束时间") @RequestParam(required = false) String endCreated,
        @ApiParam("开始时间") @RequestParam(required = false) String startCreated,
        @ApiParam(value = "物流类型搜索 同城送订单：LOCAL_DELIVERY 自提订单：SELF_FETCH 快递配送：EXPRESS") @RequestParam(required = false) String expressType
        , HttpServletResponse response) {
        TradesSoldGetParams tradesSoldGetParams = new TradesSoldGetParams();
        tradesSoldGetParams.setStatus(OrderStatusEnum.WAIT_SELLER_SEND_GOODS.getCode());
        tradesSoldGetParams.setNewEndCreated(endCreated);
        tradesSoldGetParams.setNewStartCreated(startCreated);
        tradesSoldGetParams.setExpressType(expressType);
        YouzanTradesSoldGetResult tradesSold = getTradesSold(tradesSoldGetParams);
        Long totalResults = tradesSold.getTotalResults();
        if (totalResults == null || totalResults == 0) {
            return "无待发货订单！！";
        }
        List<OrderDetail> orderDetails = orderService.excelOrderDetail(tradesSold);

//        // 导出文件模板
        File tempFile = BaseUtil.getTemplateExcelFile("order.xlsx");
//        // 导出文件名称
        String exportFileName = "订单" + DateUtil.nowDateTimeString() + ".xls";
//
//        // 生成excel文件
        ExcelExportUtil
            .exportByBeans(2, tempFile, exportFileName, false, null, orderDetails, request,
                response);
        return null;
    }


}
