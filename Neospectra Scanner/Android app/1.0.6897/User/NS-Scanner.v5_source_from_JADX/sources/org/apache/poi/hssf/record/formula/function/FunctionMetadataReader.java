package org.apache.poi.hssf.record.formula.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.poi.hssf.record.formula.Ptg;

final class FunctionMetadataReader {
    private static final String[] DIGIT_ENDING_FUNCTION_NAMES = {"LOG10", "ATAN2", "DAYS360", "SUMXMY2", "SUMX2MY2", "SUMX2PY2"};
    private static final Set DIGIT_ENDING_FUNCTION_NAMES_SET = new HashSet(Arrays.asList(DIGIT_ENDING_FUNCTION_NAMES));
    private static final String ELLIPSIS = "...";
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final String METADATA_FILE_NAME = "functionMetadata.txt";
    private static final Pattern SPACE_DELIM_PATTERN = Pattern.compile(" ");
    private static final Pattern TAB_DELIM_PATTERN = Pattern.compile("\t");

    FunctionMetadataReader() {
    }

    public static FunctionMetadataRegistry createRegistry() {
        InputStream is = FunctionMetadataReader.class.getResourceAsStream(METADATA_FILE_NAME);
        if (is == null) {
            throw new RuntimeException("resource 'functionMetadata.txt' not found");
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            FunctionDataBuilder fdb = new FunctionDataBuilder(400);
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        br.close();
                        return fdb.build();
                    } else if (line.length() >= 1) {
                        if (line.charAt(0) != '#') {
                            if (line.trim().length() >= 1) {
                                processLine(fdb, line);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static void processLine(FunctionDataBuilder fdb, String line) {
        String str = line;
        String[] parts = TAB_DELIM_PATTERN.split(str, -2);
        if (parts.length != 8) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad line format '");
            sb.append(str);
            sb.append("' - expected 8 data fields");
            throw new RuntimeException(sb.toString());
        }
        int functionIndex = parseInt(parts[0]);
        String functionName = parts[1];
        int minParams = parseInt(parts[2]);
        int maxParams = parseInt(parts[3]);
        byte returnClassCode = parseReturnTypeCode(parts[4]);
        byte[] parameterClassCodes = parseOperandTypeCodes(parts[5]);
        boolean hasNote = parts[7].length() > 0;
        validateFunctionName(functionName);
        fdb.add(functionIndex, functionName, minParams, maxParams, returnClassCode, parameterClassCodes, hasNote);
    }

    private static byte parseReturnTypeCode(String code) {
        if (code.length() == 0) {
            return 0;
        }
        return parseOperandTypeCode(code);
    }

    private static byte[] parseOperandTypeCodes(String codes) {
        if (codes.length() < 1) {
            return EMPTY_BYTE_ARRAY;
        }
        if (isDash(codes)) {
            return EMPTY_BYTE_ARRAY;
        }
        String[] array = SPACE_DELIM_PATTERN.split(codes);
        int nItems = array.length;
        if (ELLIPSIS.equals(array[nItems - 1])) {
            nItems--;
        }
        byte[] result = new byte[nItems];
        for (int i = 0; i < nItems; i++) {
            result[i] = parseOperandTypeCode(array[i]);
        }
        return result;
    }

    private static boolean isDash(String codes) {
        return codes.length() == 1 && codes.charAt(0) == '-';
    }

    private static byte parseOperandTypeCode(String code) {
        if (code.length() != 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad operand type code format '");
            sb.append(code);
            sb.append("' expected single char");
            throw new RuntimeException(sb.toString());
        }
        char charAt = code.charAt(0);
        if (charAt == 'A') {
            return Ptg.CLASS_ARRAY;
        }
        if (charAt == 'R') {
            return 0;
        }
        if (charAt == 'V') {
            return 32;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unexpected operand type code '");
        sb2.append(code);
        sb2.append("' (");
        sb2.append(code.charAt(0));
        sb2.append(")");
        throw new IllegalArgumentException(sb2.toString());
    }

    private static void validateFunctionName(String functionName) {
        int ix = functionName.length() - 1;
        if (Character.isDigit(functionName.charAt(ix))) {
            while (ix >= 0 && Character.isDigit(functionName.charAt(ix))) {
                ix--;
            }
            if (!DIGIT_ENDING_FUNCTION_NAMES_SET.contains(functionName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid function name '");
                sb.append(functionName);
                sb.append("' (is footnote number incorrectly appended)");
                throw new RuntimeException(sb.toString());
            }
        }
    }

    private static int parseInt(String valStr) {
        try {
            return Integer.parseInt(valStr);
        } catch (NumberFormatException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Value '");
            sb.append(valStr);
            sb.append("' could not be parsed as an integer");
            throw new RuntimeException(sb.toString());
        }
    }
}
