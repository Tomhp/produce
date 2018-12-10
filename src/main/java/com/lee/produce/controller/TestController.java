package com.lee.produce.controller;

import com.lee.produce.entity.Label;
import com.lee.produce.entity.User;
import com.lee.produce.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lee.produce.ProduceApplication.KEY_LEE_TEST;

@Api(tags = "消息中间件测试")
@RestController
public class TestController {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Qualifier(value = "exchangeEventTest")
    @Autowired
    private DirectExchange directExchange;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiOperation(value = "测试")
    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String test(@RequestBody User user) {
      /*  User user = new User();
        user.setId(1);
        user.setName("lee");
        user.setSex(0);
        user.setAge(18);
        user.setHeight(1.65d);*/
        rabbitMessagingTemplate.convertAndSend(directExchange.getName(), KEY_LEE_TEST, user);
        logger.info("test");
        return "hello";
    }

    @ApiOperation(value = "excel 导入测试")
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public void importExcel(MultipartFile importFile) throws Exception {

        try {
            List<List> resultList = ExcelUtil.exportFromExcel(importFile.getInputStream(),
                    FilenameUtils.getExtension(importFile.getOriginalFilename()), 0);
            logger.info(resultList.toString());
            processExcelList(resultList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processExcelList(List<List> dataList) throws Exception {

        String msg = "";
        String msgs = "";

        Map result = new HashMap();
        // excel中有效的字段
        List titleList = dataList.get(0);

        Map<String, String> englishToChineseExcelColumnMap = new HashMap<>();
        englishToChineseExcelColumnMap.putAll(importColumnMapping);
        // 转换成key为中文，value为英文的map
        Map<String, String> chineseToEnglishExcelColumnMap = new HashMap<>();
        for (Map.Entry<String, String> entry : englishToChineseExcelColumnMap.entrySet()) {
            chineseToEnglishExcelColumnMap.put(entry.getValue(), entry.getKey());
        }
        // 字段的顺序
        int columnIndex = 0;

        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (Object obj : titleList) {
            String columnName = (String) obj;
            // if (cnEnExcelColumnMap.containsKey(columnName)) {
            columnIndexMap.put(columnName, columnIndex);
            // }
            columnIndex++;
        }
        Label label = null;
        List<Label> labelList = new ArrayList<>();
        for (int k = 1; k < dataList.size(); k++) {
            label = new Label();
            List row = dataList.get(k);
            String labelId = getValue("labelId", columnIndexMap, row);
            String name = getValue("name", columnIndexMap, row);
            String isDelete = getValue("isDelete", columnIndexMap, row);
            String sortIndex = getValue("sortIndex", columnIndexMap, row);
            String createTime = getValue("createTime", columnIndexMap, row);
            String updateTime = getValue("updateTime", columnIndexMap, row);

            label.setLabelId(Integer.parseInt(labelId));
            label.setName(name);
            if ("是".equals(isDelete)) {
                label.setIsDelete(1);
            } else if ("否".equals(isDelete)) {
                label.setIsDelete(0);
            }
            label.setSortIndex(Integer.parseInt(sortIndex));
            label.setCreatedTime(simpleDateFormat.parse(createTime));
            label.setUpdateTime(simpleDateFormat.parse(updateTime));

            labelList.add(label);
            logger.info(name);
            logger.info(label.toString());
        }
        //校验数据
        for (int i = 0; i < labelList.size(); i++) {
            Label label1 = labelList.get(i);
            if (StringUtils.isEmpty(label1.getName())) {
                msgs = "第" + (i + 2) + "的标签编名称不能为空\r\n";
                msg += msgs;
            }

        }
        if ("".equals(msg)) {
            //入库操作
        }
    }


    /**
     * 导入字段关系映射
     */
    private static Map<String, String> importColumnMapping = new HashMap<String, String>() {
        {
            put("labelId", "标签编号");
            put("name", "标签名称");
            put("isDelete", "是否显示");
            put("sortIndex", "排序");
            put("createTime", "创建时间");
            put("updateTime", "更新时间");

        }
    };

    /**
     * @Description: 获取每一行中的每个列名对应的值
     * @Author: Lee
     * @DateTime: 2018/12/10 12:32
     * @return：
     */
    private String getValue(String englishColumnName, Map<String, Integer> columnIndexMap, List row)
            throws Exception {

        String cnColumnName = importColumnMapping.get(englishColumnName);
        Integer index = columnIndexMap.get(cnColumnName);
        if (index == null) {
            return null;
        }
        return row.get(index) == null ? null : row.get(index).toString();

    }
}
