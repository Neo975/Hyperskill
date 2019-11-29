import java.text.DecimalFormatSymbols;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String input;
        Pattern pattern;
        Matcher matcher;
        int sourceRadix;
        String numberSource;
        int targetRadix;
        String result;
        NumberConverter nc;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("> Enter: ");
            input = scanner.nextLine();
            pattern = Pattern.compile("^([A-Za-z0-9,]+)\\s+in\\s+base\\s+([A-Za-z0-9]+)\\s+to\\s+base\\s+([A-Za-z0-9]+)");
            matcher = pattern.matcher(input);
            if(!matcher.matches()) {
                System.out.println("Incorrect input syntax. Correct format:");
                System.out.println("<NUMBER> in base <SOURCE RADIX> to base <TARGET RADIX>");
                System.out.println("Type \"exit\" for exit");
                continue;
            }
            numberSource = matcher.group(1);
            sourceRadix = Integer.parseInt(matcher.group(2));
            targetRadix = Integer.parseInt(matcher.group(3));
            nc = new NumberConverter(numberSource, sourceRadix);
            result = nc.convertToRadix(targetRadix);
            System.out.println(result);
        } while (!input.equalsIgnoreCase("exit"));
    }

    public String forTest() {
        return "kuku";
    }
}

class NumberConverter {
    private static int DECIMAL_PLACES = 5;
    private static int MAX_RADIX = 36;
    private static int MIN_RADIX = 1;
    private long decimalIntegerPart;
    private double decimalFractionPart;
    private boolean isError;

    public NumberConverter(String sourceNumber, int sourceRadix) {
        isError = false;
        if(sourceRadix > NumberConverter.MAX_RADIX || sourceRadix < NumberConverter.MIN_RADIX) {
            isError = true;
        } else {
            String sourceIntegerPart = getIntegerPart(sourceNumber);
            String sourceFractionPart = getFractionPart(sourceNumber);
            decimalIntegerPart = convertNumberIntegerPartToDecimal(sourceIntegerPart, sourceRadix);
            decimalFractionPart = convertNumberFractionPartToDecimal(sourceFractionPart, sourceRadix);
        }
    }

    public String convertToRadix(int targetRadix) {
        String result;

        if(targetRadix > NumberConverter.MAX_RADIX || targetRadix < NumberConverter.MIN_RADIX || isError) {
            isError = true;
            return "error";
        }

        String integerPart = convertDecimalIntegerPartToTarget(decimalIntegerPart, targetRadix);
        String fractionPart = convertDecimalFractionPartToTarget(decimalFractionPart, targetRadix);
        result = integerPart;
        if(!fractionPart.equals("")) {
            result = result + DecimalFormatSymbols.getInstance().getDecimalSeparator() + fractionPart;
        }

        return result;
    }

    private String getIntegerPart(String stringNumber) {
        char separatorDecimal = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        String[] parts = stringNumber.split(String.valueOf(separatorDecimal));

        return parts[0];
    }

    private String getFractionPart(String stringNumber) {
        String result = "";
        char separatorDecimal = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        String[] parts = stringNumber.split(String.valueOf(separatorDecimal));

        if(parts.length < 2) {
            return result;
        } else {
            return parts[1];
        }
    }

    private long convertNumberIntegerPartToDecimal(String sourceNumberIntegerPart, int sourceRadix) {
        if(sourceRadix == 1) {
            long n = 0;
            for(int i = 0; i < sourceNumberIntegerPart.length(); i++) {
                n++;
            }
            return n;
        }
        try {
            long numberSourceIntegerPartLong = Long.parseLong(sourceNumberIntegerPart, sourceRadix);
            String strDecimalNumber = Long.toString(numberSourceIntegerPartLong, 10);
            return Long.parseLong(strDecimalNumber);
        } catch (NumberFormatException e) {
            isError = true;
            return 0;
        }
    }

    private double convertNumberFractionPartToDecimal(String sourceNumberFractionPart, int sourceRadix) {
        double result = 0.0;

        if(sourceRadix == 1) {
            return result;
        }
        try {
            for (int i = 0; i < sourceNumberFractionPart.length(); i++) {
                result += Long.parseLong(String.valueOf(sourceNumberFractionPart.charAt(i)), sourceRadix) / Math.pow(sourceRadix, i + 1);
                int j = 0;
            }
        } catch (NumberFormatException e) {
            isError = true;
        }
        return result;
    }

    private String convertDecimalIntegerPartToTarget(long sourceDecimalIntegerPart, int targetRadix) {
        String result = "";

        if(targetRadix == 1) {
            for(int i = 0; i < sourceDecimalIntegerPart; i++) {
                result = result + "1";
            }
            return result;
        }
        return Long.toString(sourceDecimalIntegerPart, targetRadix);
    }

    private String convertDecimalFractionPartToTarget(double sourceDecimalFractionPart, int targetRadix) {
        String result = "";
        double intermediate = sourceDecimalFractionPart;

        if(targetRadix == 1) {
            return result;
        }
        for(int i = 0; i < NumberConverter.DECIMAL_PLACES; i++) {
            intermediate = intermediate * targetRadix;
            result = result + String.valueOf(convertDecimalIntegerPartToTarget((int)intermediate, targetRadix));
            intermediate = intermediate - (int)intermediate;
        }
        if(result.equals("00000")) {
            return "";
        } else {
            return result;
        }
    }
}
