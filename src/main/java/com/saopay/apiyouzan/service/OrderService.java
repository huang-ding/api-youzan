package com.saopay.apiyouzan.service;

import static com.saopay.apiyouzan.util.http.JsonResultErrorEnum.YZ_VIRTUAL_CODE_ERROR;
import static com.saopay.apiyouzan.util.http.JsonResultErrorEnum.YZ_VIRTUAL_CODE_NULL;

import com.saopay.apiyouzan.data.dao.jpa.AdminJpaDao;
import com.saopay.apiyouzan.data.dao.jpa.VirtualCodeLogJpaDao;
import com.saopay.apiyouzan.data.pojo.dto.OrderDetail;
import com.saopay.apiyouzan.data.pojo.po.Admin;
import com.saopay.apiyouzan.data.pojo.po.VirtualCodeLog;
import com.saopay.apiyouzan.enums.RedisKeyEnum;
import com.saopay.apiyouzan.redis.RedisUtil;
import com.saopay.apiyouzan.util.DateUtil;
import com.saopay.apiyouzan.util.http.JsonResult;
import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;
import com.youzan.open.sdk.exception.KDTException;
import com.youzan.open.sdk.gen.v3_0_0.api.YouzanTradeSelffetchcodeApply;
import com.youzan.open.sdk.gen.v3_0_0.api.YouzanTradeVirtualcodeApply;
import com.youzan.open.sdk.gen.v3_0_0.api.YouzanTradeVirtualticketVerifycode;
import com.youzan.open.sdk.gen.v3_0_0.api.YouzanTradeVirtualticketVerifyticket;
import com.youzan.open.sdk.gen.v3_0_0.api.YouzanUmpCouponConsumeVerify;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeSelffetchcodeApplyParams;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeSelffetchcodeApplyResult;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeVirtualcodeApplyParams;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeVirtualcodeApplyResult;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeVirtualticketVerifycodeParams;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeVirtualticketVerifycodeResult;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeVirtualticketVerifyticketParams;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanTradeVirtualticketVerifyticketResult;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanUmpCouponConsumeVerifyParams;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanUmpCouponConsumeVerifyResult;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationOrderInfoDetail;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationTrade;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationTradeAddressInfoDetail;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationTradeBuyerInfoDetail;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationTradeItemDetail;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationTradeOrderInfo;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult.StructurizationTradeRemarkInfoDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author huangding
 * @description
 * @date 2018/11/16 16:02
 */
@Service
@Slf4j
public class OrderService extends BaseService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AdminJpaDao adminJpaDao;
    @Autowired
    private VirtualCodeLogJpaDao virtualCodeLogJpaDao;


    public JsonResult virtualCode(final String code, String openId) {
        String tagName = redisUtil.hget(RedisKeyEnum.YZ_VERIFICATION_TAG, openId);
        if (tagName == null) {
            Admin admin = adminJpaDao.findByOpenId(openId);
            if (admin == null) {
                return JsonResult.error(JsonResultErrorEnum.YZ_USER_NULL);
            }
        }
        boolean flag = false;
        try {
            //到店提货码
            if (code.indexOf("selfFetch") != -1) {
                flag = selfFetchCode(code, null);
            } else if (code.length() == 12) {
                //TODO 目前12位的为优惠券或者优惠码
                flag = verifyCode(code);
            } else {
                return JsonResult.error(YZ_VIRTUAL_CODE_NULL);
            }
        } catch (KDTException e) {
            if (e.getMessage().indexOf("ErrorResponse") != -1) {
                String errorResponse = e.getMessage().split("ErrorResponse")[1];
                String msg = errorResponse.split(",")[1].split("'")[1];
                return JsonResult.N(YZ_VIRTUAL_CODE_ERROR, msg);
            }
            return JsonResult.N(YZ_VIRTUAL_CODE_ERROR, "核销出错，请退出重试，或者联系管理员");
        }

        if (flag) {
            VirtualCodeLog virtualCodeLog = new VirtualCodeLog();
            virtualCodeLog.setCode(code);
            virtualCodeLog.setOpenId(openId);
            virtualCodeLog.setCreatorTime(LocalDateTime.now());
            virtualCodeLogJpaDao.save(virtualCodeLog);
        }
        return JsonResult.SUCCESS();
    }


    /**
     * 买家端的优惠券/优惠码核销码
     */
    public Boolean verifyCode(String code) {
        YouzanUmpCouponConsumeVerifyParams youzanUmpCouponConsumeVerifyParams = new YouzanUmpCouponConsumeVerifyParams();
        youzanUmpCouponConsumeVerifyParams.setCode(code);
        YouzanUmpCouponConsumeVerify youzanUmpCouponConsumeVerify = new YouzanUmpCouponConsumeVerify();
        youzanUmpCouponConsumeVerify.setAPIParams(youzanUmpCouponConsumeVerifyParams);
        YouzanUmpCouponConsumeVerifyResult result = super.youZanBaseUtil.getClient()
            .invoke(youzanUmpCouponConsumeVerify);
        return result.getIsSuccess();
    }


    /**
     * 到店自提
     */
    public Boolean selfFetchCode(String code, String extraInfo) {
        YouzanTradeSelffetchcodeApplyParams youzanTradeSelffetchcodeApplyParams = new YouzanTradeSelffetchcodeApplyParams();
        youzanTradeSelffetchcodeApplyParams.setCode(code);
        if (StringUtils.isNotBlank(extraInfo)) {
            youzanTradeSelffetchcodeApplyParams.setExtraInfo(extraInfo);
        }

        YouzanTradeSelffetchcodeApply youzanTradeSelffetchcodeApply = new YouzanTradeSelffetchcodeApply();
        youzanTradeSelffetchcodeApply.setAPIParams(youzanTradeSelffetchcodeApplyParams);
        YouzanTradeSelffetchcodeApplyResult result = super.youZanBaseUtil.getClient()
            .invoke(youzanTradeSelffetchcodeApply);
        return result.getIsSuccess();
    }


    /**
     * 电子卡券二维码的码号(扫用户核销的二维码)
     */
    public YouzanTradeVirtualticketVerifycodeResult verifyTicketAllCode(String code) {
        YouzanTradeVirtualticketVerifycodeParams youzanTradeVirtualticketVerifycodeParams = new YouzanTradeVirtualticketVerifycodeParams();
        youzanTradeVirtualticketVerifycodeParams.setCode(code);
        YouzanTradeVirtualticketVerifycode youzanTradeVirtualticketVerifycode = new YouzanTradeVirtualticketVerifycode();
        youzanTradeVirtualticketVerifycode.setAPIParams(youzanTradeVirtualticketVerifycodeParams);
        YouzanTradeVirtualticketVerifycodeResult result = super.youZanBaseUtil.getClient()
            .invoke(youzanTradeVirtualticketVerifycode);
        return result;
    }


    /**
     * 核销电子卡券码券号（每个电子卡券下的12位的码券号）
     */

    public Boolean verifyTicketCode(String code) {
        YouzanTradeVirtualticketVerifyticketParams youzanTradeVirtualticketVerifyticketParams = new YouzanTradeVirtualticketVerifyticketParams();
        youzanTradeVirtualticketVerifyticketParams.setTicketCode(code);
        YouzanTradeVirtualticketVerifyticket youzanTradeVirtualticketVerifyticket = new YouzanTradeVirtualticketVerifyticket();
        youzanTradeVirtualticketVerifyticket
            .setAPIParams(youzanTradeVirtualticketVerifyticketParams);
        YouzanTradeVirtualticketVerifyticketResult result = super.youZanBaseUtil.getClient()
            .invoke(youzanTradeVirtualticketVerifyticket);
        return result.getIsSuccess();
    }


    /**
     * 虚拟商品
     */
    public boolean virtualCode(String code) {
        YouzanTradeVirtualcodeApplyParams youzanTradeVirtualcodeApplyParams = new YouzanTradeVirtualcodeApplyParams();
        youzanTradeVirtualcodeApplyParams.setCode(code);
        YouzanTradeVirtualcodeApply youzanTradeVirtualcodeApply = new YouzanTradeVirtualcodeApply();
        youzanTradeVirtualcodeApply.setAPIParams(youzanTradeVirtualcodeApplyParams);
        YouzanTradeVirtualcodeApplyResult result = super.youZanBaseUtil.getClient()
            .invoke(youzanTradeVirtualcodeApply);
        return result.getIsSuccess();
    }


    public List<OrderDetail> excelOrderDetail(YouzanTradesSoldGetResult tradesSold) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        StructurizationTrade[] fullOrderInfoList = tradesSold.getFullOrderInfoList();
        for (StructurizationTrade structurizationTrade : fullOrderInfoList) {
            OrderDetail orderDetail = new OrderDetail();
            //交易基础信息结构体
            StructurizationTradeOrderInfo fullOrderInfo = structurizationTrade.getFullOrderInfo();
            //交易明细详情
            StructurizationOrderInfoDetail orderInfo = fullOrderInfo.getOrderInfo();
            //订单号
            orderDetail.setTid(orderInfo.getTid());
            //订单类型
            orderDetail.setType(orderInfo.getType());
            //物流类型
            orderDetail.setExpressType(orderInfo.getExpressType());

            //订单买家信息结构体
            StructurizationTradeBuyerInfoDetail buyerInfo = fullOrderInfo.getBuyerInfo();
            //买家id
            orderDetail.setBuyerId(buyerInfo.getBuyerId());
            //买家手机号
            orderDetail.setBuyerPhone(buyerInfo.getBuyerPhone());

            //订单标记信息结构体
            StructurizationTradeRemarkInfoDetail remarkInfo = fullOrderInfo.getRemarkInfo();
            //订单买家留言
            orderDetail.setBuyerMessage(remarkInfo.getBuyerMessage());
            //订单商家备注
            orderDetail.setTradeMemo(remarkInfo.getTradeMemo());

            //订单收货地址信息结构体
            StructurizationTradeAddressInfoDetail addressInfo = fullOrderInfo.getAddressInfo();
            //收货人姓名
            orderDetail.setReceiverName(addressInfo.getReceiverName());
            //收货人手机号
            orderDetail.setReceiverTel(addressInfo.getReceiverTel());
            //省市区 详细地址
            orderDetail.setDeliveryProvince(addressInfo.getDeliveryProvince());
            orderDetail.setDeliveryCity(addressInfo.getDeliveryCity());
            orderDetail.setDeliveryDistrict(addressInfo.getDeliveryDistrict());
            orderDetail.setDeliveryAddress(addressInfo.getDeliveryAddress());
            //到店自提信息 json格式
            orderDetail.setSelfFetchInfo(addressInfo.getSelfFetchInfo());

            //订单明细结构体
            StructurizationTradeItemDetail[] orders = fullOrderInfo.getOrders();
            StructurizationTradeItemDetail order = orders[0];
            //订单明细id
            orderDetail.setOid(order.getOid());
            //商品名称
            orderDetail.setTitle(order.getTitle());
            //商品数量
            orderDetail.setNum(order.getNum());
            //商家编码
            orderDetail.setOuterSkuId(order.getOuterSkuId());
            //商品原价
            orderDetail.setPrice(order.getPrice());
            //商品优惠后总价
            orderDetail.setTotalFee(order.getTotalFee());
            //订单实付款，也即最终支付价格（payment=orders.payment的总和+邮费 post_fee）
            orderDetail.setPayment(order.getPayment());
            //商品id
            orderDetail.setItemId(order.getItemId());
            //规格Id
            orderDetail.setSkuId(order.getSkuId());
            //规格信息
            orderDetail.setSkuPropertiesName(order.getSkuPropertiesName());
            //商品编码
            orderDetail.setOuterItemId(order.getOuterItemId());
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }

    public static void main(String[] args) {

        String s = DateUtil.nowDateTimeString();
        System.out.println(s);
    }
}
