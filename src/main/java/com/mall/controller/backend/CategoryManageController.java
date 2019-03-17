package com.mall.controller.backend;

import com.mall.common.ServerResponse;
import com.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台 分类管理控制类
 * @author panjing
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 新增分类
     * @param categoryName 分类名称
     * @param parentId 父节点id
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId){
        //全部通过拦截器验证是否登录以及权限
        //增加我们处理分类的逻辑
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 更新分类
     * @param categoryId 分类id
     * @param categoryName 分类名称
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(Integer categoryId, String categoryName){
        //全部通过拦截器验证是否登录以及权限
        //更新categoryName
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    /**
     * 获取分类
     * @param categoryId 分类id
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        //全部通过拦截器验证是否登录以及权限
        //查询子节点的category信息，并且不递归，保持平级
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 获取当前分类下所有子分类
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        //全部通过拦截器验证是否登录以及权限
        //查询当前节点的id和递归子节点的id
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
