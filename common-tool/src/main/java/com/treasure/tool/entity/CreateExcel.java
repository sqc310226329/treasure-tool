package com.treasure.tool.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月03日
 * @see
 * @since 1.0
 */

public class  CreateExcel {
    static net.sf.json.JSONArray jsonArray;

    /**
     * String path = getClass().getClassLoader().getResource("test.json").toString();
     *         System.out.println(path);
     *         path = path.replace("\\", "/");
     *         if (path.contains(":")) {
     *             //path = path.substring(6);// 1
     *             path = path.replace("file:/", "");// 2
     *         }
     * @param templateParams
     */
    public static void createExcelTemplate2007(ExcelTemplateParams templateParams){

        try {
            String input = FileUtils.readFileToString(new File(templateParams.getReadFilepath()), "UTF-8");
            jsonArray = net.sf.json.JSONArray.fromObject(input);
            System.out.println(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* templateParams.setFileName("D:/mali-Template01.xlsx");
        templateParams.setFirstRow(1);
        templateParams.setLastRow(20);
        templateParams.setFlag(true);
        templateParams.setRowIndex(0);
        templateParams.setSheetName("test");
        templateParams.setParamsList(Arrays.asList(new ExcelTemplateParams.MenuParams("a",2),
                new ExcelTemplateParams.MenuParams("d",2)));*/

        List<String> maps = new ArrayList<>();
        maps.add("<必填>发货省");
        maps.add("发货市");
        maps.add("<必填>发货区");
        maps.add("<必填>收货省");
        maps.add("<必填>收货省");
        maps.add("省");
        // 创建一个excel
        Workbook book = new XSSFWorkbook();
        Font font2 = book.createFont();
        //font2.setFontName("微软雅黑");
        font2.setColor(Font.COLOR_RED);

        Sheet sheet1 = book.createSheet(templateParams.getSheetName());
        Row row0 = sheet1.createRow(templateParams.getRowIndex());

        for (int i = 0; i <maps.size() ; i++) {

            Cell cell0 = row0.createCell(i);
            RichTextString text0 = new XSSFRichTextString(maps.get(i));
            text0.applyFont(0, maps.get(i).length(), font2); // 0, 1 表示应用字体的范围
            cell0.setCellValue(text0); // 设置单元格的值
            sheet1.setColumnWidth(i, maps.get(i).length()*2 * 256+184);//一个字符为256，汉字*2
        }

        //制作省市区的下拉菜单
        if (templateParams.isFlag()) {
            //得到第一级省名称，放在列表里
            List<String> list =  makeProvNameList();
            String[] provinceArr =list.toArray(new String[]{});
            Sheet hideSheet = book.createSheet("area");
            //这一行作用是将此sheet隐藏，功能未完成时注释此行,可以查看隐藏sheet中信息是否正确
            book.setSheetHidden(book.getSheetIndex(hideSheet), true);

            // 设置第n行，存省的信息(0-第一行，1-第二行)
            int rowIndex = templateParams.getRowIndex();
            Row provinceRow = hideSheet.createRow(rowIndex++);
            provinceRow.createCell(0).setCellValue("列表");
            for(int i = 0; i < provinceArr.length; i ++){
                Cell provinceCell = provinceRow.createCell(i + 1);
                provinceCell.setCellValue(provinceArr[i]);
            }
            // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
            Map<String,List<String>> areaMap = makeSiteMap();

            List<String> area = getAreaFatherNameArr();
            String[] areaFatherNameArr = area.toArray(new String[]{});

            for(int i = 0;i < areaFatherNameArr.length;i++){
                String key = areaFatherNameArr[i];
                String[] son = areaMap.get(key).toArray(new String[]{});
                Row row = hideSheet.createRow(rowIndex++);
                row.createCell(1).setCellValue(key);
                for(int j = 0; j < son.length; j ++){
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(son[j]);
                }

                // 添加名称管理器
                String range = getRange(1, rowIndex, son.length);
                Name name = book.createName();
                //key不可重复
                name.setNameName(key);
                String formula = "area!" + range;
                name.setRefersToFormula(formula);
            }
         templateParams.getParamsList().stream().forEach(a->{

             XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet)sheet1);
             // 省规则
             DataValidationConstraint provConstraint = dvHelper.createExplicitListConstraint(provinceArr);
             // 四个参数分别是：起始行、终止行、起始列、终止列
             CellRangeAddressList provRangeAddressList = new CellRangeAddressList(templateParams.getFirstRow(), templateParams.getLastRow(), letterToNumber(a.getOffset())-1, letterToNumber(a.getOffset())-1);
             DataValidation provinceDataValidation = dvHelper.createValidation(provConstraint, provRangeAddressList);
             //验证
             provinceDataValidation.createErrorBox("error", "请使用下拉方式选择合适的选项");
             provinceDataValidation.setShowErrorBox(true);
             provinceDataValidation.setSuppressDropDownArrow(true);
             sheet1.addValidationData(provinceDataValidation);

             // 对前n行设置有效性
             for(int i = 2;i < templateParams.getLastRow();i++){
                 recursionDataValidation(a.getOffset(),a.getSeriesNum(),sheet1,i);
             }
         });
        }
          //制作计量单位类型
//        createUnitType( book, sheet1);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(templateParams.getFileName());
            book.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }

    }

    public static void recursionDataValidation(String offset, int seriesNum,Sheet sheet1, int i){
        setDataValidation(offset , (XSSFSheet) sheet1,i,letterToNumber(offset)+1);
        if (seriesNum >2){
            seriesNum --;
            recursionDataValidation(getLetter(offset) ,seriesNum, sheet1,i);
        }
    }
    /**
     * 设置有效性
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param sheet
     * @param rowNum 行数
     * @param colNum 列数
     */
    public static void setDataValidation(String offset,XSSFSheet sheet, int rowNum,int colNum) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidation data_validation_list;
        data_validation_list = getDataValidationByFormula(
                "INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum,dvHelper);
        sheet.addValidationData(data_validation_list);
    }
    /**
     * 加载下拉列表内容
     * @param formulaString
     * @param naturalRowIndex
     * @param naturalColumnIndex
     * @param dvHelper
     * @return
     */
    private static  DataValidation getDataValidationByFormula(
            String formulaString, int naturalRowIndex, int naturalColumnIndex,XSSFDataValidationHelper dvHelper) {
        // 加载下拉列表内容
        // 举例：若formulaString = "INDIRECT($A$2)" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，
        //如果A2是江苏省，那么此处就是江苏省下的市信息。
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex -1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                lastRow, firstCol, lastCol);
        // 数据有效性对象
        // 绑定
        XSSFDataValidation data_validation_list = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof XSSFDataValidation) {
            data_validation_list.setSuppressDropDownArrow(true);
            data_validation_list.setShowErrorBox(true);
        } else {
            data_validation_list.setSuppressDropDownArrow(false);
        }
        // 设置输入信息提示信息
//        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        data_validation_list.createErrorBox("error", "请使用下拉方式选择合适的省市区");
        return data_validation_list;
    }
    /**
     *
     * @param offset 偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId 第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     *
     * @author denggonghai 2016年8月31日 下午5:17:49
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char)('A' + offset);
        if (colCount <= 25) {
            char end = (char)(start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char)('A' + 25);
                } else {
                    endSuffix = (char)('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char)('A' + 25);
                    endPrefix = (char)(endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char)('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char)(endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }
//    private static String provinceLists="[{\"id\": \"110000\",\"name\": \"北京\",\"child\": [{\t\"name\": \"北京市\",\t\"id\": \"110100\",\t\"child\": [{\"id\": \"110101\",\"child\": null,\"name\": \"东城区\"\t}, {\"child\": null,\"id\": \"110102\",\"name\": \"西城区\"\t}, {\"name\": \"崇文区\",\"id\": \"110103\",\"child\": null\t}, {\"name\": \"宣武区\",\"id\": \"110104\",\"child\": null\t}, {\"id\": \"110105\",\"name\": \"朝阳区\",\"child\": null\t}, {\"name\": \"丰台区\",\"id\": \"110106\",\"child\": null\t}, {\"name\": \"石景山区\",\"id\": \"110107\",\"child\": null\t}, {\"child\": null,\"id\": \"110108\",\"name\": \"海淀区\"\t}, {\"name\": \"门头沟区\",\"id\": \"110109\",\"child\": null\t}, {\"child\": null,\"name\": \"房山区\",\"id\": \"110111\"\t}, {\"child\": null,\"name\": \"通州区\",\"id\": \"110112\"\t}, {\"name\": \"顺义区\",\"id\": \"110113\",\"child\": null\t}, {\"id\": \"110114\",\"name\": \"昌平区\",\"child\": null\t}, {\"id\": \"110115\",\"name\": \"大兴区\",\"child\": null\t}, {\"id\": \"110116\",\"name\": \"怀柔区\",\"child\": null\t}, {\"id\": \"110117\",\"name\": \"平谷区\",\"child\": null\t}]}, {\t\"child\": [{\"id\": \"110228\",\"name\": \"密云县\",\"child\": null\t}, {\"id\": \"110229\",\"child\": null,\"name\": \"延庆县\"\t}],\t\"name\": \"北京市县\",\t\"id\": \"110200\"}] }, {\"id\": \"120000\",\"child\": [{\t\"name\": \"天津市\",\t\"child\": [{\"name\": \"和平区\",\"child\": null,\"id\": \"120101\"\t}, {\"name\": \"河东区\",\"id\": \"120102\",\"child\": null\t}, {\"name\": \"河西区\",\"id\": \"120103\",\"child\": null\t}, {\"id\": \"120104\",\"name\": \"南开区\",\"child\": null\t}, {\"id\": \"120105\",\"name\": \"河北区\",\"child\": null\t}, {\"id\": \"120106\",\"name\": \"红桥区\",\"child\": null\t}, {\"name\": \"塘沽区\",\"child\": null,\"id\": \"120107\"\t}, {\"child\": null,\"id\": \"120108\",\"name\": \"汉沽区\"\t}, {\"name\": \"大港区\",\"child\": null,\"id\": \"120109\"\t}, {\"child\": null,\"name\": \"东丽区\",\"id\": \"120110\"\t}, {\"child\": null,\"name\": \"西青区\",\"id\": \"120111\"\t}, {\"child\": null,\"id\": \"120112\",\"name\": \"津南区\"\t}, {\"child\": null,\"name\": \"北辰区\",\"id\": \"120113\"\t}, {\"child\": null,\"name\": \"武清区\",\"id\": \"120114\"\t}, {\"id\": \"120115\",\"name\": \"宝坻区\",\"child\": null\t}],\t\"id\": \"120100\"}, {\t\"id\": \"120200\",\t\"name\": \"天津市县\",\t\"child\": [{\"child\": null,\"name\": \"宁河县\",\"id\": \"120221\"\t}, {\"id\": \"120223\",\"name\": \"静海县\",\"child\": null\t}, {\"name\": \"蓟县\",\"id\": \"120225\",\"child\": null\t}]}],\"name\": \"天津\" }]";
    /**
     * 制作所有的省的list
     */
    public static List<String>  makeProvNameList (){
        List<String> provNameList = new ArrayList<String>();
        try {
            for(int i = 0;i<jsonArray.size();i++){
                JSONObject jsonob = new JSONObject().parseObject(jsonArray.get(i).toString());
                provNameList.add(jsonob.get("name").toString());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return provNameList;
    }
    /**
     * 制作所有的省的list
     */
    public static List<String>  getAreaFatherNameArr (){
        List<String> areaFatherNameArr = new ArrayList<String>();
        try {
            for(int i = 0;i<jsonArray.size();i++){
                JSONObject jsonob = new JSONObject().parseObject(jsonArray.get(i).toString());
                areaFatherNameArr.add(jsonob.get("name").toString());

                JSONArray jsonArrays = new JSONArray().parseArray(jsonob.getString("child"));
                for(int j = 0;j<jsonArrays.size();j++){
                    JSONObject jsonobs = new JSONObject().parseObject(jsonArrays.get(j).toString());
                    areaFatherNameArr.add(jsonobs.getString("name"));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(JSON.toJSON(areaFatherNameArr));
        return areaFatherNameArr;
    }

    /**
     * 制作所有市和区list
     * @return
     */
    public static Map<String, List<String>> makeSiteMap(){
        Map<String, List<String>> siteMap = new HashMap<String, List<String>>();

        try {

            for(int i = 0;i<jsonArray.size();i++){
                JSONObject jsonob = new JSONObject().parseObject(jsonArray.get(i).toString());

                JSONArray jsonArrays = new JSONArray().parseArray(jsonob.getString("child"));

                List<String> list = new ArrayList<String>();
                for(int j = 0;j<jsonArrays.size();j++){
                    JSONObject jsonobs = new JSONObject().parseObject(jsonArrays.get(j).toString());
                    list.add(jsonobs.get("name").toString());

                    JSONArray jsonArrayse = new JSONArray().parseArray(jsonobs.getString("child"));

                    List<String> liste = new ArrayList<String>();
                    for(int k = 0;k<jsonArrayse.size();k++){

                        JSONObject object = new JSONObject().parseObject(jsonArrayse.get(k).toString());
                        liste.add(object.getString("name"));
                    }
                    //省的名字是key值，value是省下面的市
                    siteMap.put(jsonobs.get("name").toString(), liste);

                }
                //省的名字是key值，value是省下面的市
                siteMap.put(jsonob.get("name").toString(), list);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return siteMap;
    }
    public static void createUnitType(Workbook book, Sheet sheet1){
        //制作货物类型
      /*  Sheet hideSheet2 = book.createSheet("site3");
        book.setSheetHidden(book.getSheetIndex(hideSheet2), true);
        // 查询所有的订单类型名称
        List<String> goodsTypeList = makeUnitType();

        int rowId1 = 0;
        // 设置第一行，存省的信息
        Row proviRow1 = hideSheet2.createRow(rowId1++);
        proviRow1.createCell(0).setCellValue("计量单位");
        for(int j = 0; j < goodsTypeList.size(); j ++){
            Cell proviCell = proviRow1.createCell(j + 1);
            proviCell.setCellValue(goodsTypeList.get(j));
        }*/
        List<String> goodsTypeList = makeUnitType();
        XSSFDataValidationHelper dvHelper2 = new XSSFDataValidationHelper((XSSFSheet)sheet1);
        // 订单类型规则
        DataValidationConstraint provConstraint2 = dvHelper2.createExplicitListConstraint(goodsTypeList.toArray(new String[]{}));
        CellRangeAddressList provRangeAddressList2 = new CellRangeAddressList(1, 20000, 14, 14);
        DataValidation provinceDataValidation2 = dvHelper2.createValidation(provConstraint2, provRangeAddressList2);
        provinceDataValidation2.createErrorBox("error", "请选择正确的计量单位");
        provinceDataValidation2.setShowErrorBox(true);
        provinceDataValidation2.setSuppressDropDownArrow(true);
        sheet1.addValidationData(provinceDataValidation2);
    }

    /**
     * 制作计量单位list
     * @return list
     */
    public static List<String>  makeUnitType(){
        List<String> list = new ArrayList<String>();
        try {
            list.add("吨");
            list.add("立方米");
            list.add("车");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    public static int letterToNumber(String letter) {
        int length = letter.length();
        int num = 0;
        int number = 0;
        for(int i = 0; i < length; i++) {
            char ch = letter.charAt(length - i - 1);
            num = (int)(ch - 'A' + 1) ;
            num *= Math.pow(26, i);
            number += num;
        }
        return number;
    }

    public static String getLetter(String letter){
        for(int i = 1;i<=26;i++){
            if (Objects.equals(String.valueOf((char)(96+i)).toUpperCase(),"Z")) {
                return "Z";
            }
            if (Objects.equals(String.valueOf((char)(96+i)).toUpperCase(),letter.toUpperCase())) {
                return String.valueOf( (char)(96+i+1)).toUpperCase();
            }
        }
        return null;
    }
}
