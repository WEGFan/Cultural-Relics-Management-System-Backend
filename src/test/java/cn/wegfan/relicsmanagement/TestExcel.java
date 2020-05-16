package cn.wegfan.relicsmanagement;

import com.spire.xls.*;

import java.awt.*;

public class TestExcel {

    public static void main(String[] args) {
        //Create a Workbook instance
        Workbook workbook = new Workbook();

        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);
        //Set name for the first worksheet
        sheet.setName("Data Sheet");

        //Create a CellStyle for header cells
        CellStyle style1 = workbook.getStyles().addStyle("Header Style");
        style1.getFont().setSize(12f);
        style1.getFont().setColor(Color.BLACK);
        style1.getFont().isBold(true);
        style1.setHorizontalAlignment(HorizontalAlignType.Center);
        style1.setVerticalAlignment(VerticalAlignType.Center);

        //Create a CellStyle for data cells
        CellStyle style2 = workbook.getStyles().addStyle("Data Style");
        style2.getFont().setSize(10f);
        style2.getFont().setColor(Color.BLACK);

        //Add data and apply style for header cells
        for (int column = 1; column < 5; column++) {
            CellRange header = sheet.getCellRange(1, column);
            header.setValue("Column " + column);
            header.setStyle(style1);
            header.setColumnWidth(15f);
        }

        // Add data and apply style for data cells
        for (int row = 2; row < 2000; row++) {
            for (int column = 1; column < 5; column++) {
                CellRange cell = sheet.getCellRange(row, column);
                cell.setValue("Data " + row + ", " + column);
                cell.setStyle(style2);
            }
        }

        //Save the resultant file
        workbook.saveToFile("CreateExcel.xlsx", FileFormat.Version2016);
    }

}
