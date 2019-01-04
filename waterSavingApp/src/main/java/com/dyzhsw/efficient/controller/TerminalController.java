package com.dyzhsw.efficient.controller;

/**
 * @Author: pjx
 * @Date: 2018/12/12 11:59
 * @Version 1.0
 */

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.constant.ManureUrl;
import com.dyzhsw.efficient.constant.MicroControlUrl;
import com.dyzhsw.efficient.dto.*;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.HttpClientTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 终端控制设备接口（设备的开启、关闭、阀值）
 */
@RestController
@RequestMapping("/terminal")
@Api(value = "终端控制关接口")
public class TerminalController {
    /**
     * 施肥机接口地址
     */
    @Value("${jxwater.manureservice}")
    private String manureHost;
    /**
     * 阀控器接口地址
     */
    @Value("${jxwater.microcontrol}")
    private String microcontrolHost;

    @ResponseBody
    @RequestMapping(value = "/isOnline", method = RequestMethod.POST)
    @ApiOperation(value = "检查设备是否在线", notes = "检查设备是否在线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "terminalId ", value = "类型1：终端设备编号，类型3：设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "equType ", value = "设备类型(1:水肥机  3：阀控器)", required = true, dataType = "int")
    })
    public BaseResponse isOnline(@RequestParam(value="terminalId") String terminalId,
                                  @RequestParam(value = "equType") int equType   ) {
        try {
            if (StringUtils.isEmpty(terminalId)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "终端编号不能为空");
            }
            if (equType<=0) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "设备类型不能为空");
            }
            if(equType ==1){//水肥机 需要：终端设备编号
                HashMap<String, String> map = new HashMap<>();
                map.put("terminalId", terminalId);
                String currentUrl = manureHost + ManureUrl.isOnline;
                String url = getUrl(currentUrl, map);
                String result = HttpClientTools.get(url);
                System.out.println(result);
                if (StringUtils.isEmpty(result))
                {
                    return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "施肥机接口返回为空");
                }
                ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
                if (data.getStateCode() == 200) {
                    return   BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
                } else {
                    return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
                }
            }
            if(equType ==3){//阀控器
                String url = microcontrolHost + MicroControlUrl.readRealData;
//                ReadDataReqDTO req=new ReadDataReqDTO();//设备id
//                req.setEquipmentId(terminalId);
//                req.setType(0);
                Map<String, String> map =new HashMap<>(); //this.objectToMap(req);
                map.put("equipmentId",terminalId);
                map.put("type","0");
                String result = HttpClientTools.postForm(url, map);
                ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
                if (data.getStateCode() == 200) {
                    return   BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
                } else {
                    return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
                }
                 }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/terminalControlReq", method = RequestMethod.POST)
    @ApiOperation(value = "水肥机控制", notes = "终端设备控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "terminalId ", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "switchState ", value = "开关状态，0-关1-开", required = true, dataType = "String")
    })
    public BaseResponse terminalControlReq(@RequestParam(value = "terminalId") String terminalId,
                                           @RequestParam(value = "switchState") String switchState  ) {
        try {
            if (StringUtils.isEmpty(terminalId)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "终端编号不能为空");
            }
            String url = manureHost + ManureUrl.terminalControlReq;
            Map<String, String> map =new HashMap<>();//this.objectToMap(req);
            map.put("terminalId",terminalId);
            map.put("switchState",switchState);
            String result = HttpClientTools.postForm(url, map);
//            String newResult = HttpUtils.post(url, map, null);
//            System.out.println("响应为："+result);
//            System.out.println("请求地址："+url);
//            System.out.println("newResult响应为："+newResult);

            if (StringUtils.isEmpty(result))
            {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "施肥机接口返回为空");
            }
            ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
            if (data.getStateCode() == 200) {
             return    BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/remoteControlReq", method = RequestMethod.POST)
    @ApiOperation(value = "远程手动控制", notes = "远程手动控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "terminalId ", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "paramStr ", value = "{'duration': '时长0-65535分钟', 'waterYield': '施肥量0-65535升'}", required = true, dataType = "String")
    })
    public BaseResponse remoteControlReq(@RequestParam(value = "terminalId") String terminalId,
                                         @RequestParam(value = "paramStr") String paramStr  ) {
        try {
            if (StringUtils.isEmpty(terminalId)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "终端编号不能为空");
            }
            String url = manureHost + ManureUrl.remoteControlReq;
            Map<String, String> map =new HashMap<>();//this.objectToMap(req);
            map.put("terminalId",terminalId);
            map.put("paramStr",paramStr);
            String result = HttpClientTools.postForm(url, map);
            System.out.println(result);
            if (StringUtils.isEmpty(result))
            {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "施肥机接口返回为空");
            }
            ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
            if (data.getStateCode() == 200) {
               return BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/terminalRebootReq", method = RequestMethod.POST)
    @ApiOperation(value = "重启系统", notes = "重启系统")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "terminalId ", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "rebootTime ", value = "重启时间", required = true, dataType = "String")
    })
    public BaseResponse terminalRebootReq(@RequestParam(value = "terminalId") String terminalId,
                                         @RequestParam(value = "rebootTime") String rebootTime ) {
        try {
            if (StringUtils.isEmpty(terminalId)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "终端编号不能为空");
            }
            String url = manureHost + ManureUrl.terminalRebootReq;
            Map<String, String> map =new HashMap<>();//this.objectToMap(req);
            map.put("terminalId",terminalId);
            map.put("rebootTime",rebootTime);
            String result = HttpClientTools.postForm(url, map);
            if (StringUtils.isEmpty(result))
            {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "施肥机接口返回为空");
            }
            System.out.println(result);
            ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
            if (data.getStateCode() == 200) {
             return    BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/control", method = RequestMethod.POST)
    @ApiOperation(value = "阀控器--阀门开启与关闭", notes = "阀控器--控制阀门开启与关闭")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentId ",value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "control1Status ", value = "一路执行开关状态（2为开,1为关）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "control2Status ", value = "二路执行开关状态（2为开,1为关）", required = true, dataType = "String")
    })
    public BaseResponse control(@RequestParam(value="equipmentId") String equipmentId,
                                @RequestParam(value = "control1Status") String control1Status,
                                @RequestParam(value = "control2Status") String control2Status
                                ) {
        try {
            if (StringUtils.isEmpty(equipmentId)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "终端编号不能为空");
            }
            String url = microcontrolHost + MicroControlUrl.control;
            Map<String, String> map = new HashMap<>();//this.objectToMap(req);
            map.put("equipmentId",equipmentId);
            map.put("control1Status",control1Status);
            map.put("control2Status",control2Status);
            String result = HttpClientTools.postForm(url, map);
            System.out.println(result);
            ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
            if (data.getStateCode() == 200) {
              return   BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/readRealData", method = RequestMethod.POST)
    @ApiOperation(value = "阀控器--查询控制阀门状态和阀控器通讯质量电压", notes = "阀控器--查询控制阀门状态和阀控器通讯质量电压")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "terminalId ", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type ", value = "0为查询阀控器状态,1为查询通讯质量和电压", required = true, dataType = "String")
    })
    public BaseResponse readRealData(@RequestParam(value="equipmentId") String equipmentId,
                                     @RequestParam(value = "type") String type ) {
        try {
            if (StringUtils.isEmpty(equipmentId)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "终端编号不能为空");
            }
            String url = microcontrolHost + MicroControlUrl.readRealData;
//            Map<String, String> map = this.objectToMap(req);
            Map<String, String> map =new HashMap<>();//this.objectToMap(req);
            map.put("equipmentId",equipmentId);
            map.put("type",type);
            String result = HttpClientTools.postForm(url, map);
            ResultObjectDTO data =  new ObjectMapper().readValue(result, ResultObjectDTO.class);
            if (data.getStateCode() == 200) {
              return   BaseResponseUtil.success(data.getStateCode(), data.getMessage(), data.getObject());
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), data.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }


    private String getUrl(String url, HashMap<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    public static Map<String, String> objectToMap(Object obj) throws Exception {
        if (obj == null)
            return null;
        Map<String, String> map = new HashMap<String, String>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, (String) value);
        }

        return map;
    }
}
