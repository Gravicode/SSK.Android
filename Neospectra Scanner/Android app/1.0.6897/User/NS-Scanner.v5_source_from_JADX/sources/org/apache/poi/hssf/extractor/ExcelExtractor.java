package org.apache.poi.hssf.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.p009ss.usermodel.HeaderFooter;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelExtractor extends POIOLE2TextExtractor implements org.apache.poi.p009ss.extractor.ExcelExtractor {
    private HSSFDataFormatter _formatter;
    private boolean _includeBlankCells;
    private boolean _includeCellComments;
    private boolean _includeHeadersFooters;
    private boolean _includeSheetNames;
    private boolean _shouldEvaluateFormulas;
    private HSSFWorkbook _wb;

    private static final class CommandArgs {
        private final boolean _evaluateFormulas;
        private final boolean _headersFooters;
        private final File _inputFile;
        private final boolean _requestHelp;
        private final boolean _showBlankCells;
        private final boolean _showCellComments;
        private final boolean _showSheetNames;

        public CommandArgs(String[] args) throws CommandParseException {
            int i;
            int nArgs = args.length;
            File inputFile = null;
            boolean requestHelp = false;
            boolean showSheetNames = true;
            boolean evaluateFormulas = true;
            boolean showCellComments = false;
            boolean showBlankCells = false;
            boolean headersFooters = true;
            int i2 = 0;
            while (true) {
                if (i2 >= nArgs) {
                    break;
                }
                String arg = args[i2];
                if ("-help".equalsIgnoreCase(arg)) {
                    requestHelp = true;
                    break;
                }
                if ("-i".equals(arg)) {
                    i = i2 + 1;
                    if (i >= nArgs) {
                        throw new CommandParseException("Expected filename after '-i'");
                    }
                    String arg2 = args[i];
                    if (inputFile != null) {
                        throw new CommandParseException("Only one input file can be supplied");
                    }
                    inputFile = new File(arg2);
                    if (!inputFile.exists()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Specified input file '");
                        sb.append(arg2);
                        sb.append("' does not exist");
                        throw new CommandParseException(sb.toString());
                    } else if (inputFile.isDirectory()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Specified input file '");
                        sb2.append(arg2);
                        sb2.append("' is a directory");
                        throw new CommandParseException(sb2.toString());
                    }
                } else if ("--show-sheet-names".equals(arg)) {
                    i = i2 + 1;
                    showSheetNames = parseBoolArg(args, i);
                } else if ("--evaluate-formulas".equals(arg)) {
                    i = i2 + 1;
                    evaluateFormulas = parseBoolArg(args, i);
                } else if ("--show-comments".equals(arg)) {
                    i = i2 + 1;
                    showCellComments = parseBoolArg(args, i);
                } else if ("--show-blanks".equals(arg)) {
                    i = i2 + 1;
                    showBlankCells = parseBoolArg(args, i);
                } else if ("--headers-footers".equals(arg)) {
                    i = i2 + 1;
                    headersFooters = parseBoolArg(args, i);
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Invalid argument '");
                    sb3.append(arg);
                    sb3.append("'");
                    throw new CommandParseException(sb3.toString());
                }
                i2 = i + 1;
            }
            this._requestHelp = requestHelp;
            this._inputFile = inputFile;
            this._showSheetNames = showSheetNames;
            this._evaluateFormulas = evaluateFormulas;
            this._showCellComments = showCellComments;
            this._showBlankCells = showBlankCells;
            this._headersFooters = headersFooters;
        }

        private static boolean parseBoolArg(String[] args, int i) throws CommandParseException {
            if (i >= args.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected value after '");
                sb.append(args[i - 1]);
                sb.append("'");
                throw new CommandParseException(sb.toString());
            }
            String value = args[i].toUpperCase();
            if ("Y".equals(value) || "YES".equals(value) || "ON".equals(value) || "TRUE".equals(value)) {
                return true;
            }
            if ("N".equals(value) || "NO".equals(value) || "OFF".equals(value) || "FALSE".equals(value)) {
                return false;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid value '");
            sb2.append(args[i]);
            sb2.append("' for '");
            sb2.append(args[i - 1]);
            sb2.append("'. Expected 'Y' or 'N'");
            throw new CommandParseException(sb2.toString());
        }

        public boolean isRequestHelp() {
            return this._requestHelp;
        }

        public File getInputFile() {
            return this._inputFile;
        }

        public boolean shouldShowSheetNames() {
            return this._showSheetNames;
        }

        public boolean shouldEvaluateFormulas() {
            return this._evaluateFormulas;
        }

        public boolean shouldShowCellComments() {
            return this._showCellComments;
        }

        public boolean shouldShowBlankCells() {
            return this._showBlankCells;
        }

        public boolean shouldIncludeHeadersFooters() {
            return this._headersFooters;
        }
    }

    private static final class CommandParseException extends Exception {
        public CommandParseException(String msg) {
            super(msg);
        }
    }

    public ExcelExtractor(HSSFWorkbook wb) {
        super(wb);
        this._includeSheetNames = true;
        this._shouldEvaluateFormulas = true;
        this._includeCellComments = false;
        this._includeBlankCells = false;
        this._includeHeadersFooters = true;
        this._wb = wb;
        this._formatter = new HSSFDataFormatter();
    }

    public ExcelExtractor(POIFSFileSystem fs) throws IOException {
        this(fs.getRoot(), fs);
    }

    public ExcelExtractor(DirectoryNode dir, POIFSFileSystem fs) throws IOException {
        this(new HSSFWorkbook(dir, fs, true));
    }

    private static void printUsageMessage(PrintStream ps) {
        ps.println("Use:");
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        sb.append(ExcelExtractor.class.getName());
        sb.append(" [<flag> <value> [<flag> <value> [...]]] [-i <filename.xls>]");
        ps.println(sb.toString());
        ps.println("       -i <filename.xls> specifies input file (default is to use stdin)");
        ps.println("       Flags can be set on or off by using the values 'Y' or 'N'.");
        ps.println("       Following are available flags and their default values:");
        ps.println("       --show-sheet-names  Y");
        ps.println("       --evaluate-formulas Y");
        ps.println("       --show-comments     N");
        ps.println("       --show-blanks       Y");
        ps.println("       --headers-footers   Y");
    }

    public static void main(String[] args) {
        InputStream is;
        try {
            CommandArgs cmdArgs = new CommandArgs(args);
            if (cmdArgs.isRequestHelp()) {
                printUsageMessage(System.out);
                return;
            }
            try {
                if (cmdArgs.getInputFile() == null) {
                    is = System.in;
                } else {
                    is = new FileInputStream(cmdArgs.getInputFile());
                }
                ExcelExtractor extractor = new ExcelExtractor(new HSSFWorkbook(is));
                extractor.setIncludeSheetNames(cmdArgs.shouldShowSheetNames());
                extractor.setFormulasNotResults(!cmdArgs.shouldEvaluateFormulas());
                extractor.setIncludeCellComments(cmdArgs.shouldShowCellComments());
                extractor.setIncludeBlankCells(cmdArgs.shouldShowBlankCells());
                extractor.setIncludeHeadersFooters(cmdArgs.shouldIncludeHeadersFooters());
                System.out.println(extractor.getText());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } catch (CommandParseException e2) {
            System.err.println(e2.getMessage());
            printUsageMessage(System.err);
            System.exit(1);
        }
    }

    public void setIncludeSheetNames(boolean includeSheetNames) {
        this._includeSheetNames = includeSheetNames;
    }

    public void setFormulasNotResults(boolean formulasNotResults) {
        this._shouldEvaluateFormulas = !formulasNotResults;
    }

    public void setIncludeCellComments(boolean includeCellComments) {
        this._includeCellComments = includeCellComments;
    }

    public void setIncludeBlankCells(boolean includeBlankCells) {
        this._includeBlankCells = includeBlankCells;
    }

    public void setIncludeHeadersFooters(boolean includeHeadersFooters) {
        this._includeHeadersFooters = includeHeadersFooters;
    }

    /* JADX WARNING: type inference failed for: r4v0 */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r8v1 */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r12v1 */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r8v5 */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r7v10, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v5 */
    /* JADX WARNING: type inference failed for: r12v6 */
    /* JADX WARNING: type inference failed for: r12v7, types: [org.apache.poi.hssf.usermodel.HSSFCellStyle] */
    /* JADX WARNING: type inference failed for: r4v12 */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* JADX WARNING: type inference failed for: r8v20 */
    /* JADX WARNING: type inference failed for: r8v21 */
    /* JADX WARNING: type inference failed for: r12v8 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r12v10 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: type inference failed for: r12v12 */
    /* JADX WARNING: type inference failed for: r12v13 */
    /* JADX WARNING: type inference failed for: r12v14 */
    /* JADX WARNING: type inference failed for: r12v15 */
    /* JADX WARNING: type inference failed for: r12v16 */
    /* JADX WARNING: type inference failed for: r12v17 */
    /* JADX WARNING: type inference failed for: r12v18 */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x010c, code lost:
        r16 = r6;
        r17 = r7;
        r12 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0122, code lost:
        r19 = r9;
        r12 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0166, code lost:
        r6 = r13.getCellComment();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x016c, code lost:
        if (r0._includeCellComments == false) goto L_0x01a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x016e, code lost:
        if (r6 == null) goto L_0x01a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0170, code lost:
        r7 = r6.getString().getString().replace(10, ' ');
        r8 = new java.lang.StringBuilder();
        r8.append(" Comment by ");
        r8.append(r6.getAuthor());
        r8.append(": ");
        r8.append(r7);
        r1.append(r8.toString());
        r8 = r6;
        r12 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01a3, code lost:
        r8 = r6;
        r12 = r12;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v2
      assigns: []
      uses: []
      mth insns count: 194
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getText() {
        /*
            r20 = this;
            r0 = r20
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            org.apache.poi.hssf.usermodel.HSSFWorkbook r2 = r0._wb
            org.apache.poi.ss.usermodel.Row$MissingCellPolicy r3 = org.apache.poi.hssf.usermodel.HSSFRow.RETURN_BLANK_AS_NULL
            r2.setMissingCellPolicy(r3)
            r2 = 0
            r3 = 0
            r4 = r2
        L_0x0011:
            org.apache.poi.hssf.usermodel.HSSFWorkbook r5 = r0._wb
            int r5 = r5.getNumberOfSheets()
            if (r3 >= r5) goto L_0x01ea
            org.apache.poi.hssf.usermodel.HSSFWorkbook r5 = r0._wb
            org.apache.poi.hssf.usermodel.HSSFSheet r5 = r5.getSheetAt(r3)
            if (r5 != 0) goto L_0x0023
            goto L_0x01e6
        L_0x0023:
            boolean r6 = r0._includeSheetNames
            if (r6 == 0) goto L_0x0037
            org.apache.poi.hssf.usermodel.HSSFWorkbook r6 = r0._wb
            java.lang.String r6 = r6.getSheetName(r3)
            if (r6 == 0) goto L_0x0037
            r1.append(r6)
            java.lang.String r7 = "\n"
            r1.append(r7)
        L_0x0037:
            boolean r6 = r0._includeHeadersFooters
            if (r6 == 0) goto L_0x0046
            org.apache.poi.hssf.usermodel.HSSFHeader r6 = r5.getHeader()
            java.lang.String r6 = _extractHeaderFooter(r6)
            r1.append(r6)
        L_0x0046:
            int r6 = r5.getFirstRowNum()
            int r7 = r5.getLastRowNum()
            r8 = r4
            r4 = r2
            r2 = r6
        L_0x0051:
            if (r2 > r7) goto L_0x01d1
            org.apache.poi.hssf.usermodel.HSSFRow r9 = r5.getRow(r2)
            if (r9 != 0) goto L_0x005f
            r16 = r6
            r17 = r7
            goto L_0x01c9
        L_0x005f:
            short r10 = r9.getFirstCellNum()
            short r11 = r9.getLastCellNum()
            boolean r12 = r0._includeBlankCells
            if (r12 == 0) goto L_0x006c
            r10 = 0
        L_0x006c:
            r12 = r8
            r8 = r4
            r4 = r10
        L_0x006f:
            if (r4 >= r11) goto L_0x01b9
            org.apache.poi.hssf.usermodel.HSSFCell r13 = r9.getCell(r4)
            r14 = 1
            if (r13 != 0) goto L_0x0082
            boolean r14 = r0._includeBlankCells
            r16 = r6
            r17 = r7
            r19 = r9
            goto L_0x01a4
        L_0x0082:
            int r15 = r13.getCellType()
            switch(r15) {
                case 0: goto L_0x0156;
                case 1: goto L_0x0144;
                case 2: goto L_0x00cb;
                case 3: goto L_0x0089;
                case 4: goto L_0x00bb;
                case 5: goto L_0x00af;
                default: goto L_0x0089;
            }
        L_0x0089:
            r16 = r6
            r17 = r7
            r19 = r9
            java.lang.RuntimeException r6 = new java.lang.RuntimeException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Unexpected cell type ("
            r7.append(r8)
            int r8 = r13.getCellType()
            r7.append(r8)
            java.lang.String r8 = ")"
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x00af:
            byte r8 = r13.getErrorCellValue()
            java.lang.String r8 = org.apache.poi.hssf.record.formula.eval.ErrorEval.getText(r8)
            r1.append(r8)
            goto L_0x00c3
        L_0x00bb:
            boolean r8 = r13.getBooleanCellValue()
            r1.append(r8)
        L_0x00c3:
            r16 = r6
            r17 = r7
            r19 = r9
            goto L_0x0166
        L_0x00cb:
            boolean r15 = r0._shouldEvaluateFormulas
            if (r15 != 0) goto L_0x00d7
            java.lang.String r8 = r13.getCellFormula()
            r1.append(r8)
            goto L_0x00c3
        L_0x00d7:
            int r15 = r13.getCachedFormulaResultType()
            switch(r15) {
                case 0: goto L_0x0111;
                case 1: goto L_0x00f9;
                case 2: goto L_0x00de;
                case 3: goto L_0x00de;
                case 4: goto L_0x00f1;
                case 5: goto L_0x00e5;
                default: goto L_0x00de;
            }
        L_0x00de:
            r16 = r6
            r17 = r7
            r19 = r9
            goto L_0x0143
        L_0x00e5:
            byte r15 = r13.getErrorCellValue()
            java.lang.String r15 = org.apache.poi.hssf.record.formula.eval.ErrorEval.getText(r15)
            r1.append(r15)
            goto L_0x010c
        L_0x00f1:
            boolean r15 = r13.getBooleanCellValue()
            r1.append(r15)
            goto L_0x010c
        L_0x00f9:
            org.apache.poi.hssf.usermodel.HSSFRichTextString r8 = r13.getRichStringCellValue()
            if (r8 == 0) goto L_0x010c
            int r15 = r8.length()
            if (r15 <= 0) goto L_0x010c
            java.lang.String r15 = r8.toString()
            r1.append(r15)
        L_0x010c:
            r16 = r6
            r17 = r7
            goto L_0x0122
        L_0x0111:
            org.apache.poi.hssf.usermodel.HSSFCellStyle r12 = r13.getCellStyle()
            if (r12 != 0) goto L_0x0125
            r16 = r6
            r17 = r7
            double r6 = r13.getNumericCellValue()
            r1.append(r6)
        L_0x0122:
            r19 = r9
            goto L_0x0143
        L_0x0125:
            r16 = r6
            r17 = r7
            org.apache.poi.hssf.usermodel.HSSFDataFormatter r6 = r0._formatter
            r18 = r8
            double r7 = r13.getNumericCellValue()
            short r15 = r12.getDataFormat()
            r19 = r9
            java.lang.String r9 = r12.getDataFormatString()
            java.lang.String r6 = r6.formatRawCellContents(r7, r15, r9)
            r1.append(r6)
        L_0x0143:
            goto L_0x0166
        L_0x0144:
            r16 = r6
            r17 = r7
            r19 = r9
            org.apache.poi.hssf.usermodel.HSSFRichTextString r6 = r13.getRichStringCellValue()
            java.lang.String r6 = r6.getString()
            r1.append(r6)
            goto L_0x0166
        L_0x0156:
            r16 = r6
            r17 = r7
            r19 = r9
            org.apache.poi.hssf.usermodel.HSSFDataFormatter r6 = r0._formatter
            java.lang.String r6 = r6.formatCellValue(r13)
            r1.append(r6)
        L_0x0166:
            org.apache.poi.hssf.usermodel.HSSFComment r6 = r13.getCellComment()
            boolean r7 = r0._includeCellComments
            if (r7 == 0) goto L_0x01a3
            if (r6 == 0) goto L_0x01a3
            org.apache.poi.hssf.usermodel.HSSFRichTextString r7 = r6.getString()
            java.lang.String r7 = r7.getString()
            r8 = 10
            r9 = 32
            java.lang.String r7 = r7.replace(r8, r9)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = " Comment by "
            r8.append(r9)
            java.lang.String r9 = r6.getAuthor()
            r8.append(r9)
            java.lang.String r9 = ": "
            r8.append(r9)
            r8.append(r7)
            java.lang.String r8 = r8.toString()
            r1.append(r8)
            r8 = r6
            r12 = r7
            goto L_0x01a4
        L_0x01a3:
            r8 = r6
        L_0x01a4:
            if (r14 == 0) goto L_0x01af
            int r6 = r11 + -1
            if (r4 >= r6) goto L_0x01af
            java.lang.String r6 = "\t"
            r1.append(r6)
        L_0x01af:
            int r4 = r4 + 1
            r6 = r16
            r7 = r17
            r9 = r19
            goto L_0x006f
        L_0x01b9:
            r16 = r6
            r17 = r7
            r18 = r8
            r19 = r9
            java.lang.String r4 = "\n"
            r1.append(r4)
            r8 = r12
            r4 = r18
        L_0x01c9:
            int r2 = r2 + 1
            r6 = r16
            r7 = r17
            goto L_0x0051
        L_0x01d1:
            r16 = r6
            r17 = r7
            boolean r2 = r0._includeHeadersFooters
            if (r2 == 0) goto L_0x01e4
            org.apache.poi.hssf.usermodel.HSSFFooter r2 = r5.getFooter()
            java.lang.String r2 = _extractHeaderFooter(r2)
            r1.append(r2)
        L_0x01e4:
            r2 = r4
            r4 = r8
        L_0x01e6:
            int r3 = r3 + 1
            goto L_0x0011
        L_0x01ea:
            java.lang.String r2 = r1.toString()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.extractor.ExcelExtractor.getText():java.lang.String");
    }

    public static String _extractHeaderFooter(HeaderFooter hf) {
        StringBuffer text = new StringBuffer();
        if (hf.getLeft() != null) {
            text.append(hf.getLeft());
        }
        if (hf.getCenter() != null) {
            if (text.length() > 0) {
                text.append("\t");
            }
            text.append(hf.getCenter());
        }
        if (hf.getRight() != null) {
            if (text.length() > 0) {
                text.append("\t");
            }
            text.append(hf.getRight());
        }
        if (text.length() > 0) {
            text.append("\n");
        }
        return text.toString();
    }
}
