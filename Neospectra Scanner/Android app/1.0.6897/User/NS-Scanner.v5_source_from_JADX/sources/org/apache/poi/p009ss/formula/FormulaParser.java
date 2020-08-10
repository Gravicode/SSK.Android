package org.apache.poi.p009ss.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.hssf.record.constant.ErrorConstant;
import org.apache.poi.hssf.record.formula.AbstractFunctionPtg;
import org.apache.poi.hssf.record.formula.AddPtg;
import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.ArrayPtg;
import org.apache.poi.hssf.record.formula.AttrPtg;
import org.apache.poi.hssf.record.formula.BoolPtg;
import org.apache.poi.hssf.record.formula.ConcatPtg;
import org.apache.poi.hssf.record.formula.DividePtg;
import org.apache.poi.hssf.record.formula.EqualPtg;
import org.apache.poi.hssf.record.formula.ErrPtg;
import org.apache.poi.hssf.record.formula.FuncPtg;
import org.apache.poi.hssf.record.formula.FuncVarPtg;
import org.apache.poi.hssf.record.formula.GreaterEqualPtg;
import org.apache.poi.hssf.record.formula.GreaterThanPtg;
import org.apache.poi.hssf.record.formula.IntPtg;
import org.apache.poi.hssf.record.formula.LessEqualPtg;
import org.apache.poi.hssf.record.formula.LessThanPtg;
import org.apache.poi.hssf.record.formula.MemAreaPtg;
import org.apache.poi.hssf.record.formula.MemFuncPtg;
import org.apache.poi.hssf.record.formula.MissingArgPtg;
import org.apache.poi.hssf.record.formula.MultiplyPtg;
import org.apache.poi.hssf.record.formula.NamePtg;
import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.hssf.record.formula.NotEqualPtg;
import org.apache.poi.hssf.record.formula.NumberPtg;
import org.apache.poi.hssf.record.formula.OperandPtg;
import org.apache.poi.hssf.record.formula.OperationPtg;
import org.apache.poi.hssf.record.formula.ParenthesisPtg;
import org.apache.poi.hssf.record.formula.PercentPtg;
import org.apache.poi.hssf.record.formula.PowerPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.RangePtg;
import org.apache.poi.hssf.record.formula.Ref3DPtg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.hssf.record.formula.StringPtg;
import org.apache.poi.hssf.record.formula.SubtractPtg;
import org.apache.poi.hssf.record.formula.UnaryMinusPtg;
import org.apache.poi.hssf.record.formula.UnaryPlusPtg;
import org.apache.poi.hssf.record.formula.UnionPtg;
import org.apache.poi.hssf.record.formula.ValueOperatorPtg;
import org.apache.poi.hssf.record.formula.function.FunctionMetadata;
import org.apache.poi.hssf.record.formula.function.FunctionMetadataRegistry;
import org.apache.poi.p009ss.SpreadsheetVersion;
import org.apache.poi.p009ss.util.AreaReference;
import org.apache.poi.p009ss.util.CellReference;
import org.apache.poi.p009ss.util.CellReference.NameType;

/* renamed from: org.apache.poi.ss.formula.FormulaParser */
public final class FormulaParser {
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Za-z]+)?(\\$?[0-9]+)?");
    private static char TAB = 9;
    private FormulaParsingWorkbook _book;
    private final int _formulaLength;
    private final String _formulaString;
    private int _pointer = 0;
    private ParseNode _rootNode;
    private int _sheetIndex;
    private SpreadsheetVersion _ssVersion;
    private char look;

    /* renamed from: org.apache.poi.ss.formula.FormulaParser$Identifier */
    private static final class Identifier {
        private final boolean _isQuoted;
        private final String _name;

        public Identifier(String name, boolean isQuoted) {
            this._name = name;
            this._isQuoted = isQuoted;
        }

        public String getName() {
            return this._name;
        }

        public boolean isQuoted() {
            return this._isQuoted;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [");
            if (this._isQuoted) {
                sb.append("'");
                sb.append(this._name);
                sb.append("'");
            } else {
                sb.append(this._name);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /* renamed from: org.apache.poi.ss.formula.FormulaParser$SheetIdentifier */
    private static final class SheetIdentifier {
        private final String _bookName;
        private final Identifier _sheetIdentifier;

        public SheetIdentifier(String bookName, Identifier sheetIdentifier) {
            this._bookName = bookName;
            this._sheetIdentifier = sheetIdentifier;
        }

        public String getBookName() {
            return this._bookName;
        }

        public Identifier getSheetIdentifier() {
            return this._sheetIdentifier;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [");
            if (this._bookName != null) {
                sb.append(" [");
                sb.append(this._sheetIdentifier.getName());
                sb.append("]");
            }
            if (this._sheetIdentifier.isQuoted()) {
                sb.append("'");
                sb.append(this._sheetIdentifier.getName());
                sb.append("'");
            } else {
                sb.append(this._sheetIdentifier.getName());
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /* renamed from: org.apache.poi.ss.formula.FormulaParser$SimpleRangePart */
    private static final class SimpleRangePart {
        private final String _rep;
        private final Type _type;

        /* renamed from: org.apache.poi.ss.formula.FormulaParser$SimpleRangePart$Type */
        private enum Type {
            CELL,
            ROW,
            COLUMN;

            public static Type get(boolean hasLetters, boolean hasDigits) {
                if (hasLetters) {
                    return hasDigits ? CELL : COLUMN;
                } else if (hasDigits) {
                    return ROW;
                } else {
                    throw new IllegalArgumentException("must have either letters or numbers");
                }
            }
        }

        public SimpleRangePart(String rep, boolean hasLetters, boolean hasNumbers) {
            this._rep = rep;
            this._type = Type.get(hasLetters, hasNumbers);
        }

        public boolean isCell() {
            return this._type == Type.CELL;
        }

        public boolean isRowOrColumn() {
            return this._type != Type.CELL;
        }

        public CellReference getCellReference() {
            if (this._type == Type.CELL) {
                return new CellReference(this._rep);
            }
            throw new IllegalStateException("Not applicable to this type");
        }

        public boolean isColumn() {
            return this._type == Type.COLUMN;
        }

        public boolean isRow() {
            return this._type == Type.ROW;
        }

        public String getRep() {
            return this._rep;
        }

        public boolean isCompatibleForArea(SimpleRangePart part2) {
            return this._type == part2._type;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append(getClass().getName());
            sb.append(" [");
            sb.append(this._rep);
            sb.append("]");
            return sb.toString();
        }
    }

    private FormulaParser(String formula, FormulaParsingWorkbook book, int sheetIndex) {
        this._formulaString = formula;
        this._book = book;
        this._ssVersion = book == null ? SpreadsheetVersion.EXCEL97 : book.getSpreadsheetVersion();
        this._formulaLength = this._formulaString.length();
        this._sheetIndex = sheetIndex;
    }

    public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, int formulaType, int sheetIndex) {
        FormulaParser fp = new FormulaParser(formula, workbook, sheetIndex);
        fp.parse();
        return fp.getRPNPtg(formulaType);
    }

    private void GetChar() {
        if (this._pointer > this._formulaLength) {
            throw new RuntimeException("too far");
        }
        if (this._pointer < this._formulaLength) {
            this.look = this._formulaString.charAt(this._pointer);
        } else {
            this.look = 0;
        }
        this._pointer++;
    }

    private void resetPointer(int ptr) {
        this._pointer = ptr;
        if (this._pointer <= this._formulaLength) {
            this.look = this._formulaString.charAt(this._pointer - 1);
        } else {
            this.look = 0;
        }
    }

    private RuntimeException expected(String s) {
        String msg;
        if (this.look != '=' || this._formulaString.substring(0, this._pointer - 1).trim().length() >= 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Parse error near char ");
            sb.append(this._pointer - 1);
            sb.append(" '");
            sb.append(this.look);
            sb.append("'");
            sb.append(" in specified formula '");
            sb.append(this._formulaString);
            sb.append("'. Expected ");
            sb.append(s);
            msg = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("The specified formula '");
            sb2.append(this._formulaString);
            sb2.append("' starts with an equals sign which is not allowed.");
            msg = sb2.toString();
        }
        return new FormulaParseException(msg);
    }

    private static boolean IsAlpha(char c) {
        return Character.isLetter(c) || c == '$' || c == '_';
    }

    private static boolean IsDigit(char c) {
        return Character.isDigit(c);
    }

    private static boolean IsWhite(char c) {
        return c == ' ' || c == TAB;
    }

    private void SkipWhite() {
        while (IsWhite(this.look)) {
            GetChar();
        }
    }

    private void Match(char x) {
        if (this.look != x) {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(x);
            sb.append("'");
            throw expected(sb.toString());
        }
        GetChar();
    }

    private String GetNum() {
        StringBuffer value = new StringBuffer();
        while (IsDigit(this.look)) {
            value.append(this.look);
            GetChar();
        }
        if (value.length() == 0) {
            return null;
        }
        return value.toString();
    }

    private ParseNode parseRangeExpression() {
        ParseNode result = parseRangeable();
        boolean hasRange = false;
        while (this.look == ':') {
            int pos = this._pointer;
            GetChar();
            ParseNode nextPart = parseRangeable();
            checkValidRangeOperand("LHS", pos, result);
            checkValidRangeOperand("RHS", pos, nextPart);
            result = new ParseNode((Ptg) RangePtg.instance, new ParseNode[]{result, nextPart});
            hasRange = true;
        }
        if (hasRange) {
            return augmentWithMemPtg(result);
        }
        return result;
    }

    private static ParseNode augmentWithMemPtg(ParseNode root) {
        Ptg memPtg;
        if (needsMemFunc(root)) {
            memPtg = new MemFuncPtg(root.getEncodedSize());
        } else {
            memPtg = new MemAreaPtg(root.getEncodedSize());
        }
        return new ParseNode(memPtg, root);
    }

    private static boolean needsMemFunc(ParseNode root) {
        Ptg token = root.getToken();
        if ((token instanceof AbstractFunctionPtg) || (token instanceof ExternSheetReferenceToken) || (token instanceof NamePtg) || (token instanceof NameXPtg)) {
            return true;
        }
        if ((token instanceof OperationPtg) || (token instanceof ParenthesisPtg)) {
            for (ParseNode child : root.getChildren()) {
                if (needsMemFunc(child)) {
                    return true;
                }
            }
            return false;
        } else if (!(token instanceof OperandPtg) && (token instanceof OperationPtg)) {
            return true;
        } else {
            return false;
        }
    }

    private static void checkValidRangeOperand(String sideName, int currentParsePosition, ParseNode pn) {
        if (!isValidRangeOperand(pn)) {
            StringBuilder sb = new StringBuilder();
            sb.append("The ");
            sb.append(sideName);
            sb.append(" of the range operator ':' at position ");
            sb.append(currentParsePosition);
            sb.append(" is not a proper reference.");
            throw new FormulaParseException(sb.toString());
        }
    }

    private static boolean isValidRangeOperand(ParseNode a) {
        Ptg tkn = a.getToken();
        boolean z = true;
        if (tkn instanceof OperandPtg) {
            return true;
        }
        if (tkn instanceof AbstractFunctionPtg) {
            if (((AbstractFunctionPtg) tkn).getDefaultOperandClass() != 0) {
                z = false;
            }
            return z;
        } else if (tkn instanceof ValueOperatorPtg) {
            return false;
        } else {
            if (tkn instanceof OperationPtg) {
                return true;
            }
            if (tkn instanceof ParenthesisPtg) {
                return isValidRangeOperand(a.getChildren()[0]);
            }
            if (tkn == ErrPtg.REF_INVALID) {
                return true;
            }
            return false;
        }
    }

    private ParseNode parseRangeable() {
        String prefix;
        SkipWhite();
        int savePointer = this._pointer;
        SheetIdentifier sheetIden = parseSheetName();
        if (sheetIden == null) {
            resetPointer(savePointer);
        } else {
            SkipWhite();
            savePointer = this._pointer;
        }
        SimpleRangePart part1 = parseSimpleRangePart();
        if (part1 != null) {
            boolean whiteAfterPart1 = IsWhite(this.look);
            if (whiteAfterPart1) {
                SkipWhite();
            }
            if (this.look == ':') {
                int colonPos = this._pointer;
                GetChar();
                SkipWhite();
                SimpleRangePart part2 = parseSimpleRangePart();
                if (part2 != null && !part1.isCompatibleForArea(part2)) {
                    part2 = null;
                }
                if (part2 != null) {
                    return createAreaRefParseNode(sheetIden, part1, part2);
                }
                resetPointer(colonPos);
                if (part1.isCell()) {
                    return createAreaRefParseNode(sheetIden, part1, part2);
                }
                if (sheetIden == null) {
                    prefix = "";
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("'");
                    sb.append(sheetIden.getSheetIdentifier().getName());
                    sb.append('!');
                    prefix = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(prefix);
                sb2.append(part1.getRep());
                sb2.append("' is not a proper reference.");
                throw new FormulaParseException(sb2.toString());
            } else if (this.look == 46) {
                GetChar();
                int dotCount = 1;
                while (this.look == '.') {
                    dotCount++;
                    GetChar();
                }
                boolean whiteBeforePart2 = IsWhite(this.look);
                SkipWhite();
                SimpleRangePart part22 = parseSimpleRangePart();
                String part1And2 = this._formulaString.substring(savePointer - 1, this._pointer - 1);
                if (part22 == null) {
                    if (sheetIden == null) {
                        return parseNonRange(savePointer);
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Complete area reference expected after sheet name at index ");
                    sb3.append(this._pointer);
                    sb3.append(".");
                    throw new FormulaParseException(sb3.toString());
                } else if (whiteAfterPart1 || whiteBeforePart2) {
                    if (!part1.isRowOrColumn() && !part22.isRowOrColumn()) {
                        return createAreaRefParseNode(sheetIden, part1, part22);
                    }
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Dotted range (full row or column) expression '");
                    sb4.append(part1And2);
                    sb4.append("' must not contain whitespace.");
                    throw new FormulaParseException(sb4.toString());
                } else if (dotCount == 1 && part1.isRow() && part22.isRow()) {
                    return parseNonRange(savePointer);
                } else {
                    if ((!part1.isRowOrColumn() && !part22.isRowOrColumn()) || dotCount == 2) {
                        return createAreaRefParseNode(sheetIden, part1, part22);
                    }
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Dotted range (full row or column) expression '");
                    sb5.append(part1And2);
                    sb5.append("' must have exactly 2 dots.");
                    throw new FormulaParseException(sb5.toString());
                }
            } else if (part1.isCell() && isValidCellReference(part1.getRep())) {
                return createAreaRefParseNode(sheetIden, part1, null);
            } else {
                if (sheetIden == null) {
                    return parseNonRange(savePointer);
                }
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Second part of cell reference expected after sheet name at index ");
                sb6.append(this._pointer);
                sb6.append(".");
                throw new FormulaParseException(sb6.toString());
            }
        } else if (sheetIden == null) {
            return parseNonRange(savePointer);
        } else {
            if (this.look == '#') {
                return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
            }
            StringBuilder sb7 = new StringBuilder();
            sb7.append("Cell reference expected after sheet name at index ");
            sb7.append(this._pointer);
            sb7.append(".");
            throw new FormulaParseException(sb7.toString());
        }
    }

    private ParseNode parseNonRange(int savePointer) {
        resetPointer(savePointer);
        if (Character.isDigit(this.look)) {
            return new ParseNode(parseNumber());
        }
        if (this.look == '\"') {
            return new ParseNode(new StringPtg(parseStringLiteral()));
        }
        StringBuilder sb = new StringBuilder();
        if (Character.isLetter(this.look) || this.look == '_') {
            while (isValidDefinedNameChar(this.look)) {
                sb.append(this.look);
                GetChar();
            }
            SkipWhite();
            String name = sb.toString();
            if (this.look == '(') {
                return function(name);
            }
            if (name.equalsIgnoreCase("TRUE") || name.equalsIgnoreCase("FALSE")) {
                return new ParseNode(BoolPtg.valueOf(name.equalsIgnoreCase("TRUE")));
            }
            if (this._book == null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Need book to evaluate name '");
                sb2.append(name);
                sb2.append("'");
                throw new IllegalStateException(sb2.toString());
            }
            EvaluationName evalName = this._book.getName(name, this._sheetIndex);
            if (evalName == null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Specified named range '");
                sb3.append(name);
                sb3.append("' does not exist in the current workbook.");
                throw new FormulaParseException(sb3.toString());
            } else if (evalName.isRange()) {
                return new ParseNode(evalName.createPtg());
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Specified name '");
                sb4.append(name);
                sb4.append("' is not a range as expected.");
                throw new FormulaParseException(sb4.toString());
            }
        } else {
            throw expected("number, string, or defined name");
        }
    }

    private static boolean isValidDefinedNameChar(char ch) {
        if (Character.isLetterOrDigit(ch) || ch == '.' || ch == '?' || ch == '\\' || ch == '_') {
            return true;
        }
        return false;
    }

    private ParseNode createAreaRefParseNode(SheetIdentifier sheetIden, SimpleRangePart part1, SimpleRangePart part2) throws FormulaParseException {
        int extIx;
        Ptg ptg;
        if (sheetIden == null) {
            extIx = Integer.MIN_VALUE;
        } else {
            String sName = sheetIden.getSheetIdentifier().getName();
            if (sheetIden.getBookName() == null) {
                extIx = this._book.getExternalSheetIndex(sName);
            } else {
                extIx = this._book.getExternalSheetIndex(sheetIden.getBookName(), sName);
            }
        }
        if (part2 == null) {
            CellReference cr = part1.getCellReference();
            if (sheetIden == null) {
                ptg = new RefPtg(cr);
            } else {
                ptg = new Ref3DPtg(cr, extIx);
            }
            Ptg ptg2 = ptg;
        } else {
            AreaReference areaRef = createAreaRef(part1, part2);
            if (sheetIden == null) {
                ptg = new AreaPtg(areaRef);
            } else {
                ptg = new Area3DPtg(areaRef, extIx);
            }
        }
        return new ParseNode(ptg);
    }

    private static AreaReference createAreaRef(SimpleRangePart part1, SimpleRangePart part2) {
        if (!part1.isCompatibleForArea(part2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("has incompatible parts: '");
            sb.append(part1.getRep());
            sb.append("' and '");
            sb.append(part2.getRep());
            sb.append("'.");
            throw new FormulaParseException(sb.toString());
        } else if (part1.isRow()) {
            return AreaReference.getWholeRow(part1.getRep(), part2.getRep());
        } else {
            if (part1.isColumn()) {
                return AreaReference.getWholeColumn(part1.getRep(), part2.getRep());
            }
            return new AreaReference(part1.getCellReference(), part2.getCellReference());
        }
    }

    private SimpleRangePart parseSimpleRangePart() {
        int ptr = this._pointer - 1;
        boolean hasDigits = false;
        boolean hasLetters = false;
        while (ptr < this._formulaLength) {
            char ch = this._formulaString.charAt(ptr);
            if (!Character.isDigit(ch)) {
                if (!Character.isLetter(ch)) {
                    if (!(ch == '$' || ch == '_')) {
                        break;
                    }
                } else {
                    hasLetters = true;
                }
            } else {
                hasDigits = true;
            }
            ptr++;
        }
        if (ptr <= this._pointer - 1) {
            return null;
        }
        String rep = this._formulaString.substring(this._pointer - 1, ptr);
        if (!CELL_REF_PATTERN.matcher(rep).matches()) {
            return null;
        }
        if (!hasLetters || !hasDigits) {
            if (hasLetters) {
                if (!CellReference.isColumnWithnRange(rep.replace("$", ""), this._ssVersion)) {
                    return null;
                }
            } else if (!hasDigits) {
                return null;
            } else {
                try {
                    int i = Integer.parseInt(rep.replace("$", ""));
                    if (i < 1 || i > 65536) {
                        return null;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (!isValidCellReference(rep)) {
            return null;
        }
        resetPointer(ptr + 1);
        return new SimpleRangePart(rep, hasLetters, hasDigits);
    }

    private SheetIdentifier parseSheetName() {
        String bookName;
        if (this.look == '[') {
            StringBuilder sb = new StringBuilder();
            GetChar();
            while (this.look != ']') {
                sb.append(this.look);
                GetChar();
            }
            GetChar();
            bookName = sb.toString();
        } else {
            bookName = null;
        }
        if (this.look == '\'') {
            StringBuffer sb2 = new StringBuffer();
            Match('\'');
            boolean done = this.look == '\'';
            while (!done) {
                sb2.append(this.look);
                GetChar();
                if (this.look == '\'') {
                    Match('\'');
                    done = this.look != '\'';
                }
            }
            Identifier iden = new Identifier(sb2.toString(), true);
            SkipWhite();
            if (this.look != '!') {
                return null;
            }
            GetChar();
            return new SheetIdentifier(bookName, iden);
        } else if (this.look != '_' && !Character.isLetter(this.look)) {
            return null;
        } else {
            StringBuilder sb3 = new StringBuilder();
            while (isUnquotedSheetNameChar(this.look)) {
                sb3.append(this.look);
                GetChar();
            }
            SkipWhite();
            if (this.look != '!') {
                return null;
            }
            GetChar();
            return new SheetIdentifier(bookName, new Identifier(sb3.toString(), false));
        }
    }

    private static boolean isUnquotedSheetNameChar(char ch) {
        if (Character.isLetterOrDigit(ch) || ch == '.' || ch == '_') {
            return true;
        }
        return false;
    }

    private boolean isValidCellReference(String str) {
        boolean z = false;
        boolean result = CellReference.classifyCellReference(str, this._ssVersion) == NameType.CELL;
        if (!result) {
            return result;
        }
        if (!(FunctionMetadataRegistry.getFunctionByName(str.toUpperCase()) != null)) {
            return result;
        }
        int savePointer = this._pointer;
        resetPointer(this._pointer + str.length());
        SkipWhite();
        if (this.look != '(') {
            z = true;
        }
        boolean result2 = z;
        resetPointer(savePointer);
        return result2;
    }

    private ParseNode function(String name) {
        Ptg nameToken = null;
        if (!AbstractFunctionPtg.isBuiltInFunctionName(name)) {
            if (this._book == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Need book to evaluate name '");
                sb.append(name);
                sb.append("'");
                throw new IllegalStateException(sb.toString());
            }
            EvaluationName hName = this._book.getName(name, this._sheetIndex);
            if (hName == null) {
                nameToken = this._book.getNameXPtg(name);
                if (nameToken == null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Name '");
                    sb2.append(name);
                    sb2.append("' is completely unknown in the current workbook");
                    throw new FormulaParseException(sb2.toString());
                }
            } else if (!hName.isFunctionName()) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Attempt to use name '");
                sb3.append(name);
                sb3.append("' as a function, but defined name in workbook does not refer to a function");
                throw new FormulaParseException(sb3.toString());
            } else {
                nameToken = hName.createPtg();
            }
        }
        Match('(');
        ParseNode[] args = Arguments();
        Match(')');
        return getFunction(name, nameToken, args);
    }

    private ParseNode getFunction(String name, Ptg namePtg, ParseNode[] args) {
        AbstractFunctionPtg retval;
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByName(name.toUpperCase());
        int numArgs = args.length;
        if (fm == null) {
            if (namePtg == null) {
                throw new IllegalStateException("NamePtg must be supplied for external functions");
            }
            ParseNode[] allArgs = new ParseNode[(numArgs + 1)];
            allArgs[0] = new ParseNode(namePtg);
            System.arraycopy(args, 0, allArgs, 1, numArgs);
            return new ParseNode((Ptg) FuncVarPtg.create(name, numArgs + 1), allArgs);
        } else if (namePtg != null) {
            throw new IllegalStateException("NamePtg no applicable to internal functions");
        } else {
            boolean isVarArgs = !fm.hasFixedArgsLength();
            int funcIx = fm.getIndex();
            if (funcIx == 4 && args.length == 1) {
                return new ParseNode((Ptg) AttrPtg.getSumSingle(), args);
            }
            validateNumArgs(args.length, fm);
            if (isVarArgs) {
                retval = FuncVarPtg.create(name, numArgs);
            } else {
                retval = FuncPtg.create(funcIx);
            }
            return new ParseNode((Ptg) retval, args);
        }
    }

    private void validateNumArgs(int numArgs, FunctionMetadata fm) {
        int maxArgs;
        String msg;
        String msg2;
        if (numArgs < fm.getMinParams()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Too few arguments to function '");
            sb.append(fm.getName());
            sb.append("'. ");
            String msg3 = sb.toString();
            if (fm.hasFixedArgsLength()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(msg3);
                sb2.append("Expected ");
                sb2.append(fm.getMinParams());
                msg2 = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(msg3);
                sb3.append("At least ");
                sb3.append(fm.getMinParams());
                sb3.append(" were expected");
                msg2 = sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(msg2);
            sb4.append(" but got ");
            sb4.append(numArgs);
            sb4.append(".");
            throw new FormulaParseException(sb4.toString());
        }
        if (!fm.hasUnlimitedVarags()) {
            maxArgs = fm.getMaxParams();
        } else if (this._book != null) {
            maxArgs = this._book.getSpreadsheetVersion().getMaxFunctionArgs();
        } else {
            maxArgs = fm.getMaxParams();
        }
        if (numArgs > maxArgs) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Too many arguments to function '");
            sb5.append(fm.getName());
            sb5.append("'. ");
            String msg4 = sb5.toString();
            if (fm.hasFixedArgsLength()) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(msg4);
                sb6.append("Expected ");
                sb6.append(maxArgs);
                msg = sb6.toString();
            } else {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(msg4);
                sb7.append("At most ");
                sb7.append(maxArgs);
                sb7.append(" were expected");
                msg = sb7.toString();
            }
            StringBuilder sb8 = new StringBuilder();
            sb8.append(msg);
            sb8.append(" but got ");
            sb8.append(numArgs);
            sb8.append(".");
            throw new FormulaParseException(sb8.toString());
        }
    }

    private static boolean isArgumentDelimiter(char ch) {
        return ch == ',' || ch == ')';
    }

    private ParseNode[] Arguments() {
        List<ParseNode> temp = new ArrayList<>(2);
        SkipWhite();
        if (this.look == ')') {
            return ParseNode.EMPTY_ARRAY;
        }
        boolean missedPrevArg = true;
        int numArgs = 0;
        while (true) {
            SkipWhite();
            if (isArgumentDelimiter(this.look)) {
                if (missedPrevArg) {
                    temp.add(new ParseNode(MissingArgPtg.instance));
                    numArgs++;
                }
                if (this.look == ')') {
                    ParseNode[] result = new ParseNode[temp.size()];
                    temp.toArray(result);
                    return result;
                }
                Match(',');
                missedPrevArg = true;
            } else {
                temp.add(comparisonExpression());
                numArgs++;
                missedPrevArg = false;
                SkipWhite();
                if (!isArgumentDelimiter(this.look)) {
                    throw expected("',' or ')'");
                }
            }
        }
    }

    private ParseNode powerFactor() {
        ParseNode result = percentFactor();
        while (true) {
            SkipWhite();
            if (this.look != '^') {
                return result;
            }
            Match('^');
            result = new ParseNode(PowerPtg.instance, result, percentFactor());
        }
    }

    private ParseNode percentFactor() {
        ParseNode result = parseSimpleFactor();
        while (true) {
            SkipWhite();
            if (this.look != '%') {
                return result;
            }
            Match('%');
            result = new ParseNode((Ptg) PercentPtg.instance, result);
        }
    }

    private ParseNode parseSimpleFactor() {
        SkipWhite();
        char c = this.look;
        if (c == '(') {
            Match('(');
            ParseNode inside = comparisonExpression();
            Match(')');
            return new ParseNode((Ptg) ParenthesisPtg.instance, inside);
        } else if (c == '+') {
            Match('+');
            return parseUnary(true);
        } else if (c == '-') {
            Match('-');
            return parseUnary(false);
        } else if (c != '{') {
            switch (c) {
                case '\"':
                    return new ParseNode(new StringPtg(parseStringLiteral()));
                case '#':
                    return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
                default:
                    if (IsAlpha(this.look) || Character.isDigit(this.look) || this.look == '\'' || this.look == '[') {
                        return parseRangeExpression();
                    }
                    if (this.look == '.') {
                        return new ParseNode(parseNumber());
                    }
                    throw expected("cell ref or constant literal");
            }
        } else {
            Match('{');
            ParseNode arrayNode = parseArray();
            Match('}');
            return arrayNode;
        }
    }

    private ParseNode parseUnary(boolean isPlus) {
        boolean numberFollows = IsDigit(this.look) || this.look == '.';
        ParseNode factor = powerFactor();
        if (numberFollows) {
            Ptg token = factor.getToken();
            if (token instanceof NumberPtg) {
                if (isPlus) {
                    return factor;
                }
                return new ParseNode(new NumberPtg(-((NumberPtg) token).getValue()));
            } else if (token instanceof IntPtg) {
                if (isPlus) {
                    return factor;
                }
                return new ParseNode(new NumberPtg((double) (-((IntPtg) token).getValue())));
            }
        }
        return new ParseNode((Ptg) isPlus ? UnaryPlusPtg.instance : UnaryMinusPtg.instance, factor);
    }

    private ParseNode parseArray() {
        List<Object[]> rowsData = new ArrayList<>();
        while (true) {
            rowsData.add(parseArrayRow());
            if (this.look == '}') {
                Object[][] values2d = new Object[rowsData.size()][];
                rowsData.toArray(values2d);
                checkRowLengths(values2d, values2d[0].length);
                return new ParseNode(new ArrayPtg(values2d));
            } else if (this.look != ';') {
                throw expected("'}' or ';'");
            } else {
                Match(';');
            }
        }
    }

    private void checkRowLengths(Object[][] values2d, int nColumns) {
        for (int i = 0; i < values2d.length; i++) {
            int rowLen = values2d[i].length;
            if (rowLen != nColumns) {
                StringBuilder sb = new StringBuilder();
                sb.append("Array row ");
                sb.append(i);
                sb.append(" has length ");
                sb.append(rowLen);
                sb.append(" but row 0 has length ");
                sb.append(nColumns);
                throw new FormulaParseException(sb.toString());
            }
        }
    }

    private Object[] parseArrayRow() {
        char c;
        List<Object> temp = new ArrayList<>();
        while (true) {
            temp.add(parseArrayItem());
            SkipWhite();
            c = this.look;
            if (c != ',') {
                break;
            }
            Match(',');
        }
        if (c == ';' || c == '}') {
            Object[] result = new Object[temp.size()];
            temp.toArray(result);
            return result;
        }
        throw expected("'}' or ','");
    }

    private Object parseArrayItem() {
        SkipWhite();
        char c = this.look;
        if (c == '-') {
            Match('-');
            SkipWhite();
            return convertArrayNumber(parseNumber(), false);
        } else if (c == 'F' || c == 'T' || c == 'f' || c == 't') {
            return parseBooleanLiteral();
        } else {
            switch (c) {
                case '\"':
                    return parseStringLiteral();
                case '#':
                    return ErrorConstant.valueOf(parseErrorLiteral());
                default:
                    return convertArrayNumber(parseNumber(), true);
            }
        }
    }

    private Boolean parseBooleanLiteral() {
        String iden = parseUnquotedIdentifier();
        if ("TRUE".equalsIgnoreCase(iden)) {
            return Boolean.TRUE;
        }
        if ("FALSE".equalsIgnoreCase(iden)) {
            return Boolean.FALSE;
        }
        throw expected("'TRUE' or 'FALSE'");
    }

    private static Double convertArrayNumber(Ptg ptg, boolean isPositive) {
        double value;
        if (ptg instanceof IntPtg) {
            value = (double) ((IntPtg) ptg).getValue();
        } else if (ptg instanceof NumberPtg) {
            value = ((NumberPtg) ptg).getValue();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected ptg (");
            sb.append(ptg.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
        if (!isPositive) {
            value = -value;
        }
        return new Double(value);
    }

    private Ptg parseNumber() {
        String number2 = null;
        String exponent = null;
        String number1 = GetNum();
        if (this.look == '.') {
            GetChar();
            number2 = GetNum();
        }
        if (this.look == 'E') {
            GetChar();
            String sign = "";
            if (this.look == '+') {
                GetChar();
            } else if (this.look == '-') {
                GetChar();
                sign = "-";
            }
            String number = GetNum();
            if (number == null) {
                throw expected("Integer");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(sign);
            sb.append(number);
            exponent = sb.toString();
        }
        if (number1 != null || number2 != null) {
            return getNumberPtgFromString(number1, number2, exponent);
        }
        throw expected("Integer");
    }

    private int parseErrorLiteral() {
        Match('#');
        String part1 = parseUnquotedIdentifier().toUpperCase();
        if (part1 == null) {
            throw expected("remainder of error constant literal");
        }
        char charAt = part1.charAt(0);
        if (charAt != 'D') {
            if (charAt != 'N') {
                if (charAt != 'R') {
                    if (charAt != 'V') {
                        throw expected("#VALUE!, #REF!, #DIV/0!, #NAME?, #NUM!, #NULL! or #N/A");
                    } else if (part1.equals("VALUE")) {
                        Match('!');
                        return 15;
                    } else {
                        throw expected("#VALUE!");
                    }
                } else if (part1.equals("REF")) {
                    Match('!');
                    return 23;
                } else {
                    throw expected("#REF!");
                }
            } else if (part1.equals("NAME")) {
                Match('?');
                return 29;
            } else if (part1.equals("NUM")) {
                Match('!');
                return 36;
            } else if (part1.equals("NULL")) {
                Match('!');
                return 0;
            } else if (part1.equals("N")) {
                Match('/');
                if (this.look == 'A' || this.look == 'a') {
                    Match(this.look);
                    return 42;
                }
                throw expected("#N/A");
            } else {
                throw expected("#NAME?, #NUM!, #NULL! or #N/A");
            }
        } else if (part1.equals("DIV")) {
            Match('/');
            Match('0');
            Match('!');
            return 7;
        } else {
            throw expected("#DIV/0!");
        }
    }

    private String parseUnquotedIdentifier() {
        if (this.look == '\'') {
            throw expected("unquoted identifier");
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (!Character.isLetterOrDigit(this.look) && this.look != '.') {
                break;
            }
            sb.append(this.look);
            GetChar();
        }
        if (sb.length() < 1) {
            return null;
        }
        return sb.toString();
    }

    private static Ptg getNumberPtgFromString(String number1, String number2, String exponent) {
        StringBuffer number = new StringBuffer();
        if (number2 == null) {
            number.append(number1);
            if (exponent != null) {
                number.append('E');
                number.append(exponent);
            }
            String numberStr = number.toString();
            try {
                int intVal = Integer.parseInt(numberStr);
                if (IntPtg.isInRange(intVal)) {
                    return new IntPtg(intVal);
                }
                return new NumberPtg(numberStr);
            } catch (NumberFormatException e) {
                return new NumberPtg(numberStr);
            }
        } else {
            if (number1 != null) {
                number.append(number1);
            }
            number.append('.');
            number.append(number2);
            if (exponent != null) {
                number.append('E');
                number.append(exponent);
            }
            return new NumberPtg(number.toString());
        }
    }

    private String parseStringLiteral() {
        Match('\"');
        StringBuffer token = new StringBuffer();
        while (true) {
            if (this.look == '\"') {
                GetChar();
                if (this.look != '\"') {
                    return token.toString();
                }
            }
            token.append(this.look);
            GetChar();
        }
    }

    private ParseNode Term() {
        Ptg operator;
        ParseNode result = powerFactor();
        while (true) {
            SkipWhite();
            char c = this.look;
            if (c == '*') {
                Match('*');
                operator = MultiplyPtg.instance;
            } else if (c != '/') {
                return result;
            } else {
                Match('/');
                operator = DividePtg.instance;
            }
            result = new ParseNode(operator, result, powerFactor());
        }
    }

    private ParseNode unionExpression() {
        ParseNode result = comparisonExpression();
        boolean hasUnions = false;
        while (true) {
            SkipWhite();
            if (this.look != ',') {
                break;
            }
            GetChar();
            hasUnions = true;
            result = new ParseNode(UnionPtg.instance, result, comparisonExpression());
        }
        if (hasUnions) {
            return augmentWithMemPtg(result);
        }
        return result;
    }

    private ParseNode comparisonExpression() {
        ParseNode result = concatExpression();
        while (true) {
            SkipWhite();
            switch (this.look) {
                case '<':
                case '=':
                case '>':
                    result = new ParseNode(getComparisonToken(), result, concatExpression());
                default:
                    return result;
            }
        }
    }

    private Ptg getComparisonToken() {
        if (this.look == '=') {
            Match(this.look);
            return EqualPtg.instance;
        }
        boolean isGreater = this.look == '>';
        Match(this.look);
        if (!isGreater) {
            switch (this.look) {
                case '=':
                    Match('=');
                    return LessEqualPtg.instance;
                case '>':
                    Match('>');
                    return NotEqualPtg.instance;
                default:
                    return LessThanPtg.instance;
            }
        } else if (this.look != '=') {
            return GreaterThanPtg.instance;
        } else {
            Match('=');
            return GreaterEqualPtg.instance;
        }
    }

    private ParseNode concatExpression() {
        ParseNode result = additiveExpression();
        while (true) {
            SkipWhite();
            if (this.look != '&') {
                return result;
            }
            Match('&');
            result = new ParseNode(ConcatPtg.instance, result, additiveExpression());
        }
    }

    private ParseNode additiveExpression() {
        Ptg operator;
        ParseNode result = Term();
        while (true) {
            SkipWhite();
            char c = this.look;
            if (c == '+') {
                Match('+');
                operator = AddPtg.instance;
            } else if (c != '-') {
                return result;
            } else {
                Match('-');
                operator = SubtractPtg.instance;
            }
            result = new ParseNode(operator, result, Term());
        }
    }

    private void parse() {
        this._pointer = 0;
        GetChar();
        this._rootNode = unionExpression();
        if (this._pointer <= this._formulaLength) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unused input [");
            sb.append(this._formulaString.substring(this._pointer - 1));
            sb.append("] after attempting to parse the formula [");
            sb.append(this._formulaString);
            sb.append("]");
            throw new FormulaParseException(sb.toString());
        }
    }

    private Ptg[] getRPNPtg(int formulaType) {
        new OperandClassTransformer(formulaType).transformFormula(this._rootNode);
        return ParseNode.toTokenArray(this._rootNode);
    }
}
