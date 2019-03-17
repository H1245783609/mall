package com.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.service.IOrderService;
import com.mall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台 订单管理控制类
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    /**
     * 查看订单列表
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //全部通过拦截器验证是否登录以及权限
        //查看订单列表
        return iOrderService.manageList(pageNum,pageSize);

    }

    /**
     * 查看订单详情
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(Long orderNo){
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageDetail(orderNo);
    }


    /**
     * 搜索订单
     * @param orderNo 订单号
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageSearch(orderNo,pageNum,pageSize);
    }

    /**
     * 订单发货
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(Long orderNo){
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageSendGoods(orderNo);
    }


}
