package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.JingdiangoupiaoEntity;
import com.entity.view.JingdiangoupiaoView;

import com.service.JingdiangoupiaoService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 景点购票
 * 后端接口
 * @author 
 * @email 
 * @date 2021-05-10 13:20:20
 */
@RestController
@RequestMapping("/jingdiangoupiao")
public class JingdiangoupiaoController {
    @Autowired
    private JingdiangoupiaoService jingdiangoupiaoService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JingdiangoupiaoEntity jingdiangoupiao, 
		HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			jingdiangoupiao.setYonghuming((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JingdiangoupiaoEntity> ew = new EntityWrapper<JingdiangoupiaoEntity>();
		PageUtils page = jingdiangoupiaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jingdiangoupiao), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JingdiangoupiaoEntity jingdiangoupiao, 
		HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			jingdiangoupiao.setYonghuming((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JingdiangoupiaoEntity> ew = new EntityWrapper<JingdiangoupiaoEntity>();
		PageUtils page = jingdiangoupiaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jingdiangoupiao), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JingdiangoupiaoEntity jingdiangoupiao){
       	EntityWrapper<JingdiangoupiaoEntity> ew = new EntityWrapper<JingdiangoupiaoEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jingdiangoupiao, "jingdiangoupiao")); 
        return R.ok().put("data", jingdiangoupiaoService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JingdiangoupiaoEntity jingdiangoupiao){
        EntityWrapper< JingdiangoupiaoEntity> ew = new EntityWrapper< JingdiangoupiaoEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jingdiangoupiao, "jingdiangoupiao")); 
		JingdiangoupiaoView jingdiangoupiaoView =  jingdiangoupiaoService.selectView(ew);
		return R.ok("查询景点购票成功").put("data", jingdiangoupiaoView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JingdiangoupiaoEntity jingdiangoupiao = jingdiangoupiaoService.selectById(id);
        return R.ok().put("data", jingdiangoupiao);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JingdiangoupiaoEntity jingdiangoupiao = jingdiangoupiaoService.selectById(id);
        return R.ok().put("data", jingdiangoupiao);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JingdiangoupiaoEntity jingdiangoupiao, HttpServletRequest request){
    	jingdiangoupiao.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jingdiangoupiao);

        jingdiangoupiaoService.insert(jingdiangoupiao);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JingdiangoupiaoEntity jingdiangoupiao, HttpServletRequest request){
    	jingdiangoupiao.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jingdiangoupiao);
    	jingdiangoupiao.setUserid((Long)request.getSession().getAttribute("userId"));

        jingdiangoupiaoService.insert(jingdiangoupiao);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JingdiangoupiaoEntity jingdiangoupiao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jingdiangoupiao);
        jingdiangoupiaoService.updateById(jingdiangoupiao);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        jingdiangoupiaoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<JingdiangoupiaoEntity> wrapper = new EntityWrapper<JingdiangoupiaoEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			wrapper.eq("yonghuming", (String)request.getSession().getAttribute("username"));
		}

		int count = jingdiangoupiaoService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
