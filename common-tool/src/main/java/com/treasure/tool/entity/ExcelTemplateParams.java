package com.treasure.tool.entity;

import java.util.List;

public class ExcelTemplateParams {

        private String readFilepath;
        private String fileName;
        private String sheetName;
        private int rowIndex;
        private boolean flag;
        private int firstRow;
        private int lastRow;

        private List<MenuParams> paramsList;

    public List<MenuParams> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<MenuParams> paramsList) {
        this.paramsList = paramsList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public String getReadFilepath() {
        return readFilepath;
    }

    public void setReadFilepath(String readFilepath) {
        this.readFilepath = readFilepath;
    }


    public static class MenuParams{

          private String offset;

          private int seriesNum;

        public MenuParams(String offset, int seriesNum) {
            this.offset = offset;
            this.seriesNum = seriesNum;
        }

        public String getOffset() {
              return offset.toUpperCase();
          }

          public void setOffset(String offset) {
              this.offset = offset;
          }

          public int getSeriesNum() {
              return seriesNum;
          }

          public void setSeriesNum(int seriesNum) {
              this.seriesNum = seriesNum;
          }
      }


}
