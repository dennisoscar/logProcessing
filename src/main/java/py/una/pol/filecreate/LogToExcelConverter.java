package py.una.pol.filecreate;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class LogToExcelConverter {

    public static void main(String[] args) {
        /*String logFilePath = "/home/fran/Descargas/logUSA/logCompleto.log";*/
        String logFilePath = "C:\\Users\\odga\\Downloads\\Tesisi\\logCompleto.log";
        String excelFilePath = "C:\\Users\\odga\\Downloads\\Tesisi\\MallaNoCanectadoUSA_cloud.xlsx";

        try {
            convertLogToExcel(logFilePath, excelFilePath);
            System.out.println("Archivo Excel creado correctamente.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al procesar el archivo de log o al crear el archivo de Excel.");
            e.printStackTrace();
        }
    }

    public static void convertLogToExcel(String logFilePath, String excelFilePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        Row currentRow = null;
        int rowIndex = 0;
        int count = 0;
        String generation = "";
        String fitness = "";
        String bloqueoPromedio = "";
        Double averageSur = 0d;
        String chromosome = "";
        Double standarDesviation = 0d;
        String tiempoSimulacion = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Iniciamos la prueba utilzando el algorirmo")) {
                    String algorithm = line.split("algorirmo")[1].trim();
                    String crossover = findValueForLabel(reader.readLine(), "Probabilidad de cruce:");
                    String mutation = findValueForLabel(reader.readLine(), "Probabilidad de mutacion:");
                    String poblacion = findValueForLabel(reader.readLine(), "población:");
                    String sheetName = "_PC=" + crossover + "_PM=" + mutation + "_POB=" + poblacion;
                    // Verificar si la hoja ya existe
                    int sheetIndex = workbook.getSheetIndex(sheetName);
                    if (sheetIndex >= 0) {
                        sheet = workbook.getSheetAt(sheetIndex);
                        currentRow = sheet.createRow(rowIndex);
                        rowIndex++;

                        CellRangeAddress cellRange = new CellRangeAddress(currentRow.getRowNum(), currentRow.getRowNum(), 0, 10);
                        sheet.addMergedRegion(cellRange);

                        Cell mergedCell = currentRow.createCell(0);
                        mergedCell.setCellValue("ALGORITMO: " + algorithm + " PROBABILIDAD DE CRUCE: " + crossover + " PROBABILDAD DE MUTACIÓN: " + mutation + " CANTIDAD DE POBLACIÓN: " + poblacion);
                        currentRow = sheet.createRow(rowIndex);
                        rowIndex++;

                    } else {
                        sheet = workbook.createSheet(sheetName);
                        rowIndex = 0;
                        currentRow = sheet.createRow(rowIndex);
                        rowIndex += 5; // Dejar 4 filas libres

                        currentRow.createCell(0).setCellValue("TIEMPO");
                        currentRow.createCell(1).setCellValue("NUMERO GENERACION");
                        currentRow.createCell(2).setCellValue("Fitnes");
                        currentRow.createCell(3).setCellValue("BloqueoPromedio");
                        currentRow.createCell(4).setCellValue("AverageSur");
                        currentRow.createCell(5).setCellValue("StandarDesviation");
                        currentRow.createCell(6).setCellValue("CROMOSOMA");
                        currentRow = sheet.createRow(rowIndex);
                        rowIndex++;

                        CellRangeAddress cellRange = new CellRangeAddress(currentRow.getRowNum(), currentRow.getRowNum(), 0, 10);
                        sheet.addMergedRegion(cellRange);

                        Cell mergedCell = currentRow.createCell(0);
                        mergedCell.setCellValue("ALGORITMO: " + algorithm + " PROBABILIDAD DE CRUCE: " + crossover + " PROBABILDAD DE MUTACIÓN: " + mutation + " CANTIDAD DE POBLACIÓN: " + poblacion);
                        currentRow = sheet.createRow(rowIndex);
                        rowIndex++;
                    }

                } else if (line.contains("GENERACION ")) {
                    generation = findValueForLabel(line, "GENERACION");

                } else if (line.contains("fitnes mejor individuo:")) {
                    fitness = findValueForLabel(line, "fitnes mejor individuo:");

                } else if (line.contains("bloqueo promedio mejor individuo:")) {
                    bloqueoPromedio = findValueForLabel(line, "bloqueo promedio mejor individuo:");

                } else if (line.contains("averageSur mejor individuo:")) {
                    averageSur = Double.valueOf(findValueForLabel(line, "averageSur mejor individuo:"));

                } else if (line.contains("standar desviation mejor individuo:")) {
                    standarDesviation = Double.valueOf(findValueForLabel(line, "standar desviation mejor individuo:"));

                } else if (line.contains("Mejor:")) {
                    chromosome = findValueForLabel(line, "Mejor:");

                } else if (line.contains("Aca finaliza la simulacion: 30")) {
                    currentRow.createCell(0);
                    currentRow.createCell(1).setCellValue(generation);
                    currentRow.createCell(2, CellType.NUMERIC).setCellValue(fitness);
                    currentRow.createCell(3, CellType.NUMERIC).setCellValue(bloqueoPromedio);
                    currentRow.createCell(4, CellType.NUMERIC).setCellValue(averageSur*1000000);
                    currentRow.createCell(5, CellType.NUMERIC).setCellValue(standarDesviation*1000000);
                    currentRow.createCell(6).setCellValue(chromosome);
                    rowIndex-=30;
                    currentRow = sheet.getRow(rowIndex);
                } else if(line.contains("Aca finaliza la simulacion:")){
                    currentRow.createCell(0);
                    currentRow.createCell(1).setCellValue(generation);
                    currentRow.createCell(2, CellType.NUMERIC).setCellValue(fitness);
                    currentRow.createCell(3, CellType.NUMERIC).setCellValue(bloqueoPromedio);
                    currentRow.createCell(4, CellType.NUMERIC).setCellValue(averageSur*1000000);
                    currentRow.createCell(5, CellType.NUMERIC).setCellValue(standarDesviation*1000000);
                    currentRow.createCell(6).setCellValue(chromosome);
                    currentRow = sheet.createRow(rowIndex);
                    rowIndex++;
                } else if (line.contains("El tiempo de la simulacion")){
                    tiempoSimulacion = findValueForLabel(line, "El tiempo de la simulacion ");
                    currentRow.createCell(0, CellType.NUMERIC).setCellValue(Integer.valueOf(tiempoSimulacion.substring(7, tiempoSimulacion.lastIndexOf(".")).trim()));
                    rowIndex++;
                    currentRow = sheet.getRow(rowIndex);
                    count++;
                }
                if(count == 30){
                    currentRow = sheet.createRow(rowIndex);
                    currentRow.createCell(0).setCellFormula("AVERAGE(A" + (rowIndex - 29) + ":A" + (rowIndex) + ")");
                    currentRow.createCell(4).setCellFormula("AVERAGE(E" + (rowIndex - 29) + ":E" + (rowIndex) + ")/1000000");
                    currentRow.createCell(5).setCellFormula("AVERAGE(F" + (rowIndex - 29) + ":F" + (rowIndex) + ")/1000000");
                    rowIndex++;
                    count= 0;
                    generation = "";
                    fitness = "";
                    bloqueoPromedio = "";
                    averageSur = 0d;
                    chromosome = "";
                    standarDesviation = 0d;
                    tiempoSimulacion = "";
                }
            }
        }

        if (sheet != null) {

            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }
        }

        workbook.close();
    }

    private static String findValueForLabel(String line, String label) {
        int labelIndex = line.indexOf(label);
        if (labelIndex >= 0) {
            return line.substring(labelIndex + label.length()).trim();
        }
        return "";
    }

    private static String combineColumns(Row row) {
        StringBuilder combinedValue = new StringBuilder();

        int lastCellIndex = row.getLastCellNum();
        for (int i = 0; i < lastCellIndex; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = cell.getStringCellValue();
                if (cellValue.contains(":")) {
                    int separatorIndex = cellValue.indexOf(":");
                    String columnTitle = cellValue.substring(0, separatorIndex).trim();
                    String columnValue = cellValue.substring(separatorIndex + 1).trim();

                    if (columnTitle.equals("Aca finaliza la simulacion")) {
                        combinedValue.append("SIMULACION NRO: ").append(columnValue).append(" ");
                    } else {
                        combinedValue.append(columnValue).append(" ");
                    }
                }
            }
        }

        return combinedValue.toString().trim();
    }


//    private static String combineColumns(Row row) {
//        StringBuilder combinedValue = new StringBuilder();
//        for (int i = 0; i < 6; i++) {
//            Cell cell = row.getCell(i);
//            if (cell != null) {
//                combinedValue.append(cell.getStringCellValue()).append(" ");
//            }
//        }
//        return combinedValue.toString().trim();
//    }
}

