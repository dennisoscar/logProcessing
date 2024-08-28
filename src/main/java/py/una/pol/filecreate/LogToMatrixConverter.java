package py.una.pol.filecreate;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogToMatrixConverter {
    public static void main(String[] args) {
        /*String logFilePath = "/home/fran/Descargas/logUSA/logCompleto.log";*/
        String logFilePath = "C:\\Users\\odga\\Downloads\\Tesisi\\logCompleto.log";
        String excelFilePath = "C:\\Users\\odga\\Downloads\\Tesisi\\MallaNoCanectadoUSA_cloud.xlsx";



        /*creacion de matriz para*/

        try {
            convertLogToMatrix(logFilePath, excelFilePath);
            System.out.println("Archivo Excel creado correctamente.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al procesar el archivo de log o al crear el archivo de Excel.");
            e.printStackTrace();
        }
    }

    public static void convertLogToMatrix(String logFilePath, String excelFilePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        Row currentRow = null;
        int rowIndex = 0;
        int count = 0;
        int cantidadDeFitness = 0;
        String generation = "";
        String fitness = "";
        String bloqueoPromedio = "";
        Double averageSur = 0d;
        String chromosome = "";
        Double standarDesviation = 0d;
        String tiempoSimulacion = "";
        /*int filas = 30;
        int columnas =  100 ;*/
        //Integer[][] tiempo = new Integer[30][100];
        double[][] tiempo = new double[30][100];
        Datos[][] matrizDatos = new Datos[30][100];
        List<double[][]> algoritmo = new ArrayList<double[][]>();
        List<Datos[][]> algoritmo2 = new ArrayList<Datos[][]>();
        int fila = 0;
        int columna = 0;
        int contadorAlgoritmo = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            algoritmo.add(tiempo);
            algoritmo2.add(matrizDatos);
            String algorithm = " ";
            while ((line = reader.readLine()) != null) {

                if (line.contains("Iniciamos la prueba utilzando el algorirmo")) {

                    algorithm = line.split("algorirmo")[1].trim();
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
                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    ////algoritmo.get(contadorAlgoritmo)[fila][columna] = Double.parseDouble(generation);

                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setGeneration(generation);
                } else if (line.contains("fitnes mejor individuo:")) {
                    fitness = findValueForLabel(line, "fitnes mejor individuo:");
                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setFitness(fitness);
                } else if (line.contains("bloqueo promedio mejor individuo:")) {
                    bloqueoPromedio = findValueForLabel(line, "bloqueo promedio mejor individuo:");
                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setBloqueoPromedio(bloqueoPromedio);
                } else if (line.contains("averageSur mejor individuo:")) {
                    averageSur = Double.valueOf(findValueForLabel(line, "averageSur mejor individuo:"));

                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setAverageSur(averageSur);
                } else if (line.contains("standar desviation mejor individuo:")) {
                    standarDesviation = Double.valueOf(findValueForLabel(line, "standar desviation mejor individuo:"));

                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setStandarDesviation(standarDesviation);
                    columna++;
                } else if (line.contains("Mejor:")) {
                    chromosome = findValueForLabel(line, "Mejor:");
                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setChromosome(chromosome);

                } else if (line.contains("Aca finaliza la simulacion: 30")) {
                    currentRow.createCell(0);
                    currentRow.createCell(1).setCellValue(generation);
                    currentRow.createCell(2, CellType.NUMERIC).setCellValue(fitness);
                    currentRow.createCell(3, CellType.NUMERIC).setCellValue(bloqueoPromedio);
                    currentRow.createCell(4, CellType.NUMERIC).setCellValue(averageSur * 1000000);
                    currentRow.createCell(5, CellType.NUMERIC).setCellValue(standarDesviation * 1000000);
                    currentRow.createCell(6).setCellValue(chromosome);
                    rowIndex -= 30;
                    columna = 0;
                    currentRow = sheet.getRow(rowIndex);
                } else if (line.contains("Aca finaliza la simulacion:")) {
                    currentRow.createCell(0);
                    currentRow.createCell(1).setCellValue(generation);
                    currentRow.createCell(2, CellType.NUMERIC).setCellValue(fitness);
                    currentRow.createCell(3, CellType.NUMERIC).setCellValue(bloqueoPromedio);
                    currentRow.createCell(4, CellType.NUMERIC).setCellValue(averageSur * 1000000);
                    currentRow.createCell(5, CellType.NUMERIC).setCellValue(standarDesviation * 1000000);
                    currentRow.createCell(6).setCellValue(chromosome);
                    currentRow = sheet.createRow(rowIndex);
                    rowIndex++;
                    fila++;
                    columna=0;
                } else if (line.contains("El tiempo de la simulacion")) {
                    tiempoSimulacion = findValueForLabel(line, "El tiempo de la simulacion ");
                    currentRow.createCell(0, CellType.NUMERIC).setCellValue(Integer.valueOf(tiempoSimulacion.substring(7, tiempoSimulacion.lastIndexOf(".")).trim()));
                    rowIndex++;
                    currentRow = sheet.getRow(rowIndex);
                    count++;
                    ////algoritmo.get(contadorAlgoritmo)[fila][columna] = Double.parseDouble(tiempo); aca setear el tiempo
                    if (algoritmo2.get(contadorAlgoritmo)[fila][columna] == null) {
                        algoritmo2.get(contadorAlgoritmo)[fila][columna] = new Datos();
                    }
                    algoritmo2.get(contadorAlgoritmo)[fila][columna].setTiempoSimulacion(tiempoSimulacion.substring(7, tiempoSimulacion.lastIndexOf(".")).trim());
                    columna++;
                }
                if (count == 30) {
                    currentRow = sheet.createRow(rowIndex);
                    currentRow.createCell(0).setCellFormula("AVERAGE(A" + (rowIndex - 29) + ":A" + (rowIndex) + ")");
                    currentRow.createCell(4).setCellFormula("AVERAGE(E" + (rowIndex - 29) + ":E" + (rowIndex) + ")/1000000");
                    currentRow.createCell(5).setCellFormula("AVERAGE(F" + (rowIndex - 29) + ":F" + (rowIndex) + ")/1000000");
                    rowIndex++;
                    count = 0;
                    contadorAlgoritmo++;
                    calcularPromediosPorColumna(algoritmo2.get(algoritmo2.size()-1),algorithm);
                    algoritmo2.add(matrizDatos);
                    fila=0;
                    columna=0;
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

       /* algoritmo.forEach((item)->{
            for (int i = 0; i < 30; i++) {        // El primer índice recorre las filas.
                for (int j = 0; j < 100; j++) {    // El segundo índice recorre las columnas.
                    // Procesamos cada elemento de la matriz.

                    System.out.println("fila: " + i + ", columna: "+ j);
                    System.out.println(item[i][j]);
                    System.out.print("\t");
                }
                System.out.println("\n");
                System.out.println("\n nuevo algoritmo ");
            }
        });*/

        algoritmo2.forEach((matriz) -> {
            for (int i = 0; i < matriz.length; i++) { // Corregido el bucle
                for (int j = 0; j < matriz[i].length; j++) { // Corregido el bucle
                    if (matriz[i][j] != null) {
                        System.out.println("Datos en fila " + i + ", columna " + j + ": " + matriz[i][j].toString());
                    }
                }
            }
            System.out.println("Nuevo algoritmo");
        });

        System.out.println(cantidadDeFitness);
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


    // Método para calcular el promedio de cada columna pasando directamente la matriz
    public static void calcularPromediosPorColumna(Datos[][] matriz, String algorithm) {
        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Recorrer la matriz por columnas
        for (int j = 0; j < columnas; j++) {
            double sumaFitness = 0;
            double sumaBloqueoPromedio = 0;
            double sumaAverageSur = 0;
            double sumaStandarDesviation = 0;
            double sumaTiempoSimulacion = 0;
            int conteoElementos = 0;

            for (int i = 0; i < filas; i++) {
                if (matriz[i][j] != null) {
                    sumaFitness += Double.parseDouble(matriz[i][j].getFitness());
                    sumaBloqueoPromedio += Double.parseDouble(matriz[i][j].getBloqueoPromedio());
                    sumaAverageSur += matriz[i][j].getAverageSur();
                    sumaStandarDesviation += matriz[i][j].getStandarDesviation();
  //                  sumaTiempoSimulacion += Double.parseDouble(matriz[i][j].getTiempoSimulacion());
                    conteoElementos++;
                }
            }

            // Calcular y mostrar el promedio para la columna j
            if (conteoElementos > 0) {
                double promedioFitness = sumaFitness / conteoElementos;
                double promedioBloqueoPromedio = sumaBloqueoPromedio / conteoElementos;
                double promedioAverageSur = sumaAverageSur / conteoElementos;
                double promedioStandarDesviation = sumaStandarDesviation / conteoElementos;
 //               double promedioTiempoSimulacion = sumaTiempoSimulacion / conteoElementos;

                System.out.println("Columna " + j + ":");
                System.out.println("Promedio Fitness: " + promedioFitness);
                System.out.println("Promedio Bloqueo Promedio: " + promedioBloqueoPromedio);
                System.out.println("Promedio AverageSur: " + promedioAverageSur);
                System.out.println("Promedio Standar Desviation: " + promedioStandarDesviation);
  //              System.out.println("Promedio Tiempo Simulacion: " + promedioTiempoSimulacion);
            }
        }
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
