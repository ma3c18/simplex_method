package simplex_method;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

public class SimplexMethod {

    private static Scanner input;

    /**
     * Lee la entrada por teclado
     *
     * @return entrada del teclado
     */
    public static double readInput() {
        input = new Scanner(System.in);
        return input.nextDouble();
    }

    /**
     * Imprime el menú principal
     *
     */
    public static void printMainMenu() {
        System.out.print("SIMPLEX METHOD\n" + "1) MAXIMIZE\n" +
            "2) MINIMIZE\n" + "3) Exit\n" + "Choose a option: ");
    }

    /**
     * Establece las dimensiones de la matriz
     *
     * @return un arreglo con el número de filas y columnas
     */
    public static int[] setDimensions() {
        int numValsOF, restrictions;
        System.out.print("\nEnter the number of values for the OF: ");
        numValsOF = (int) readInput();
        System.out.print("Enter the number of restrictions: ");
        restrictions = (int) readInput();
        return new int[]{(restrictions + 1), (numValsOF + restrictions + 1)};
    }

    /**
     * Llena la matriz con los valores correspondientes
     *
     * @param matrix matriz origen
     * @param row número de filas
     * @param column número de columnas
     * @param dimensions arreglo con las dimensiones de la matriz origen
     */
    public static void fillMainMatrix(double matrix[][], int row, int column,
        int dimensions[]) {
        int S; // Contador para variables de holgura
        for (int i = 0; i < row; i++) {
            if (i != (row - 1)) {
                System.out.println("\nRestriction #" + (i + 1) + ": ");
            } else {
                System.out.println("\nObjetive function: ");
            }
            S = 1;
            for (int j = 0; j < column; j++) {
                if (j < (dimensions[1] - (dimensions[0] - 1) - 1)) {
                    System.out.print("Enter the values for X" + (j + 1) +
                        ": ");
                } else {
                    if (j != (column - 1)) {
                        System.out.print("Enter the values for S" + S + ": ");
                        S++;
                    } else {
                        System.out.print("Enter the value for RHS: ");
                    }
                }
                matrix[i][j] = (int) readInput();
                /*if (i < (matrix.length - 1)) {
                    matrix[i][j] = (int) (Math.random() * 20 + 1);
                } else {
                    matrix[i][j] = (int) (Math.random() * (-20 - -1 + 1) - 1);
                }*/
            }
        }

    }

    /**
     * Imprime los valores de la matriz principal
     *
     * @param matrix matriz principal con los datos establecidos
     * @param row cantidad de filas
     * @param column cantidad de columnas
     */
    public static void printMainMatrix(double matrix[][], int row, int column) {
        int X = 1, S = 1; // Contador de terminos (FO) y variables de holgura
        System.out.print("\nBV");
        for (int k = 0; k < column; k++) {
            if (k < column - row) {
                System.out.print("\tX" + X);
                X++;
            } else {
                if (k != (column - 1)) {
                    System.out.print("\tS" + S);
                    S++;
                } else {
                    System.out.print("\tRHS");
                }
            }
        }
        System.out.println();
        S = 1;
        for (int i = 0; i < row; i++) {
            if (i < (matrix.length - 1)) {
                System.out.print("S" + S + "\t");
                S++;
            } else {
                System.out.print("Z\t");
            }
            for (int j = 0; j < column; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Divide la columna RHS (Constante de restricción) y la columna pivote. El
     * resultado de la división se guardará en un arreglo
     *
     * @param matrix valores establecidos en matriz principal
     * @param row número de filas
     * @param column número de columnas
     * @param pivotColumn posición de la columna pivote
     * @return un arreglo con los resultados de la división de columnas
     */
    public static double[] splitRHSColumns(double matrix[][], int row,
        int column, int pivotColumn) {
        double results[] = new double[row - 1];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i != (row - 1)) {
                    results[i] = (double) (matrix[i][column - 1]) /
                        (double) (matrix[i][pivotColumn]);
                }
            }
        }
        return results;
    }

    /**
     * Encuentra la fila pivote
     *
     * @param results arreglo con los resultados de la división de columnas
     * @return la posición de la fila pivote
     */
    public static int searchPivotRow(double results[]) {
        double lowestNumber = results[0];
        int rowPosition = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] < lowestNumber) {
                lowestNumber = results[i];
                rowPosition = i;
            }
        }
        return rowPosition;
    }

    /**
     * Encuentra la columna pivote
     *
     * @param matrix valores establecidos en matriz principal
     * @param row número de filas
     * @param column número de columnas
     * @return la posición de la columna
     */
    public static int searchPivotColumn(double matrix[][], int row, int column) {
        double lowestNumber = matrix[row - 1][0];
        int columnPosition = 0;
        for (int j = 0; j < column; j++) {
            if (j != column - 1) {
                if (matrix[matrix.length - 1][j] < lowestNumber) {
                    lowestNumber = matrix[row - 1][j];
                    columnPosition = j;
                }
            }

        }
        return columnPosition;
    }

    /**
     * Encuentra el elemento pivote en la intersección de la fila pivote y la
     * columna pivote
     *
     * @param matrix valores establecidos en matriz principal
     * @param pivotRow posición de la fila pivote
     * @param pivotColumn posición de la columna pivote
     * @return la posición del elemento pivote
     */
    public static double searchPivotElement(double matrix[][], int pivotRow,
        int pivotColumn) {
        return matrix[pivotRow][pivotColumn];
    }

    /**
     * Muestra la actualización de los valores de la matriz principal
     *
     * @param matrix valores establecidos en matriz principal
     * @param row número de filas
     * @param column número de columna
     * @param pivotRow posición de la fila pivote
     * @param pivotColumn posición de la columna pivote
     * @param format formato de decimales establecido (0.0)
     */
    public static void printMatrixUpdate(double matrix[][], int row, int column,
        int pivotRow, int pivotColumn, DecimalFormat format) {
        int X = 1, S = 1; // Contador de terminos (FO) y variables de holgura
        System.out.print("\nBV");
        for (int k = 0; k < column; k++) {
            if (k < column - row) {
                System.out.print("\tX" + X);
                X++;
            } else {
                if (k != (column - 1)) {
                    System.out.print("\tS" + S);
                    S++;
                } else {
                    System.out.print("\tRHS");
                }
            }
        }
        System.out.println();
        for (int i = 0; i < row; i++) {
            if (i < (matrix.length - 1)) {
                if (i != pivotRow) {
                    System.out.print("S" + (i + 1) + "\t");
                } else {
                    System.out.print("X" + (pivotColumn + 1) + "\t");
                }
            } else {
                System.out.print("Z\t");
            }
            for (int j = 0; j < column; j++) {
                System.out.print(applyFormatting(matrix[i][j], format) + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Divide la fila entrante entre el elemento pivote y lo guarda en un
     * arreglo
     *
     * @param matrix valores establecidos en matriz principal
     * @param row número de filas
     * @param column número de columnas
     * @param pivotRow posición de fila pivote
     * @param pivotElement valor del elemento pivote
     * @return arreglo con los valores de la fila entrante
     */
    public static double[] splitIncomingRow(double matrix[][], int row,
        int column, int pivotRow, double pivotElement) {
        int position = 0; // Posición del resultado de la división
        double separateRow[] = new double[column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i == pivotRow) {
                    separateRow[position] = matrix[i][j] / pivotElement;
                    matrix[i][j] = separateRow[position];
                    position++;
                }
            }
        }
        return separateRow;
    }

    /**
     * Separa la columna de coeficientes pivotes y lo almacena en un arreglo
     *
     * @param matrix valores establecidos en matriz principal
     * @param row número de filas
     * @param column número de columnas
     * @param pivotColumn posición de la columna pivote
     * @return arreglo con los elementos pivotes
     */
    public static double[] separateColumnFromPivotElements(double matrix[][],
        int row, int column, int pivotColumn) {
        int position = 0; // Posición de los elementos pivotes
        double pivotElements[] = new double[row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (j == pivotColumn) {
                    pivotElements[position] = matrix[i][j];
                    position++;
                }
            }
        }
        return pivotElements;
    }

    /**
     * Calcula la fila nueva que resulta de la resta de la fila vieja y el
     * coeficiente pivote de la fila vieja multiplicado por la fila entrante
     *
     * @param matrix valores establecidos en matriz principal
     * @param row número de filas
     * @param column número de columnas
     * @param pivotRow posición de la fila pivote
     * @param incomingRow arreglo con los valores de la fila entrante
     * @param pivotElements arreglo de coeficientes pivotes de fila vieja
     */
    public static void computeNewRow(double matrix[][], int row, int column,
        int pivotRow, double incomingRow[],
        double pivotElements[]) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i != pivotRow) {
                    matrix[i][j] = matrix[i][j] - pivotElements[i] *
                        incomingRow[j];
                }
            }
        }
    }

    /**
     * Aplica el formato de decimales a los valores de la matriz
     *
     * @param value valor para aplicar el formato
     * @param format formato establecido
     * @return el valor con el formato establecido de decimales
     */
    public static String applyFormatting(double value, DecimalFormat format) {
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(value);
    }

    public static void main(String[] args) {
        int row, column, menuOption, tableNumber, pivotRow, pivotColumn,
            dimensions[];
        double pivotElement, incomingRow[], divisionResults[], pivotElements[],
            matrix[][];
        boolean exit = false;
        DecimalFormat format = new DecimalFormat("0.00");

        do {
            printMainMenu();
            menuOption = (int) readInput();

            switch (menuOption) {
                case 1:
                    // Dimensión de la matriz
                    dimensions = setDimensions();
                    row = dimensions[0];
                    column = dimensions[1];

                    // Datos de la tabla
                    matrix = new double[row][column];
                    fillMainMatrix(matrix, row, column, dimensions);

                    // Tabla incial
                    System.out.print("\nTABLE #1");
                    printMainMatrix(matrix, row, column);

                    // Columna pivote
                    pivotColumn = searchPivotColumn(matrix, row, column);

                    // Fila pivote
                    divisionResults = new double[row - 1];
                    divisionResults = splitRHSColumns(matrix, row, column,
                        pivotColumn);
                    pivotRow = searchPivotRow(divisionResults);

                    // Elemento Pivote
                    pivotElement = searchPivotElement(matrix, pivotRow,
                        pivotColumn);

                    // Fila entrante
                    incomingRow = splitIncomingRow(matrix, row, column,
                        pivotRow, pivotElement);

                    // Condición de Optimalidad
                    tableNumber = 2;

                    for (int j = 0; j < column; j++) {
                        while (matrix[row - 1][j] < 0) {
                            System.out.print("\nTABLE #" + tableNumber);

                            // Columna de coeficientes pivotes
                            pivotElements = separateColumnFromPivotElements(
                                matrix, row, column, pivotColumn);

                            // Nueva fila
                            computeNewRow(matrix, row, column, pivotRow,
                                incomingRow, pivotElements);

                            // Valores de la matriz principal actualizados
                            printMatrixUpdate(matrix, row, column, pivotRow,
                                pivotColumn, format);

                            tableNumber++;
                        }
                    }
                    System.out.println();
                    break;
                case 2:
                    break;
                case 3:
                    exit = true;
                    System.out.println("Successfull exit");
                    break;
                default:
                    System.out.println("Enter an option valid [1 - 3]\n");
            }
        } while (!exit);

    }

}
