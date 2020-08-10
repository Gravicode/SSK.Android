package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class JsonReader implements Closeable {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 17;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final char[] buffer = new char[1024];

    /* renamed from: in */
    private final Reader f58in;
    private boolean lenient = false;
    private int limit = 0;
    private int lineNumber = 0;
    private int lineStart = 0;
    private int[] pathIndices;
    private String[] pathNames;
    int peeked = 0;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos = 0;
    private int[] stack = new int[32];
    private int stackSize = 0;

    static {
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
            public void promoteNameToValue(JsonReader reader) throws IOException {
                if (reader instanceof JsonTreeReader) {
                    ((JsonTreeReader) reader).promoteNameToValue();
                    return;
                }
                int p = reader.peeked;
                if (p == 0) {
                    p = reader.doPeek();
                }
                if (p == 13) {
                    reader.peeked = 9;
                } else if (p == 12) {
                    reader.peeked = 8;
                } else if (p == 14) {
                    reader.peeked = 10;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Expected a name but was ");
                    sb.append(reader.peek());
                    sb.append(reader.locationString());
                    throw new IllegalStateException(sb.toString());
                }
            }
        };
    }

    public JsonReader(Reader in) {
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + 1;
        iArr[i] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.f58in = in;
    }

    public final void setLenient(boolean lenient2) {
        this.lenient = lenient2;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 3) {
            push(1);
            this.pathIndices[this.stackSize - 1] = 0;
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected BEGIN_ARRAY but was ");
        sb.append(peek());
        sb.append(locationString());
        throw new IllegalStateException(sb.toString());
    }

    public void endArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 4) {
            this.stackSize--;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected END_ARRAY but was ");
        sb.append(peek());
        sb.append(locationString());
        throw new IllegalStateException(sb.toString());
    }

    public void beginObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 1) {
            push(3);
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected BEGIN_OBJECT but was ");
        sb.append(peek());
        sb.append(locationString());
        throw new IllegalStateException(sb.toString());
    }

    public void endObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 2) {
            this.stackSize--;
            this.pathNames[this.stackSize] = null;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected END_OBJECT but was ");
        sb.append(peek());
        sb.append(locationString());
        throw new IllegalStateException(sb.toString());
    }

    public boolean hasNext() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        return (p == 2 || p == 4) ? false : true;
    }

    public JsonToken peek() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        switch (p) {
            case 1:
                return JsonToken.BEGIN_OBJECT;
            case 2:
                return JsonToken.END_OBJECT;
            case 3:
                return JsonToken.BEGIN_ARRAY;
            case 4:
                return JsonToken.END_ARRAY;
            case 5:
            case 6:
                return JsonToken.BOOLEAN;
            case 7:
                return JsonToken.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return JsonToken.STRING;
            case 12:
            case 13:
            case 14:
                return JsonToken.NAME;
            case 15:
            case 16:
                return JsonToken.NUMBER;
            case 17:
                return JsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    /* access modifiers changed from: 0000 */
    public int doPeek() throws IOException {
        int peekStack = this.stack[this.stackSize - 1];
        if (peekStack == 1) {
            this.stack[this.stackSize - 1] = 2;
        } else if (peekStack == 2) {
            int c = nextNonWhitespace(true);
            if (c != 44) {
                if (c == 59) {
                    checkLenient();
                } else if (c != 93) {
                    throw syntaxError("Unterminated array");
                } else {
                    this.peeked = 4;
                    return 4;
                }
            }
        } else if (peekStack == 3 || peekStack == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (peekStack == 5) {
                int c2 = nextNonWhitespace(true);
                if (c2 != 44) {
                    if (c2 == 59) {
                        checkLenient();
                    } else if (c2 != 125) {
                        throw syntaxError("Unterminated object");
                    } else {
                        this.peeked = 2;
                        return 2;
                    }
                }
            }
            int c3 = nextNonWhitespace(true);
            if (c3 == 34) {
                this.peeked = 13;
                return 13;
            } else if (c3 == 39) {
                checkLenient();
                this.peeked = 12;
                return 12;
            } else if (c3 != 125) {
                checkLenient();
                this.pos--;
                if (isLiteral((char) c3)) {
                    this.peeked = 14;
                    return 14;
                }
                throw syntaxError("Expected name");
            } else if (peekStack != 5) {
                this.peeked = 2;
                return 2;
            } else {
                throw syntaxError("Expected name");
            }
        } else if (peekStack == 4) {
            this.stack[this.stackSize - 1] = 5;
            int c4 = nextNonWhitespace(true);
            if (c4 != 58) {
                if (c4 != 61) {
                    throw syntaxError("Expected ':'");
                }
                checkLenient();
                if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>') {
                    this.pos++;
                }
            }
        } else if (peekStack == 6) {
            if (this.lenient) {
                consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = 7;
        } else if (peekStack == 7) {
            if (nextNonWhitespace(false) == -1) {
                this.peeked = 17;
                return 17;
            }
            checkLenient();
            this.pos--;
        } else if (peekStack == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
        int c5 = nextNonWhitespace(true);
        if (c5 == 34) {
            this.peeked = 9;
            return 9;
        } else if (c5 != 39) {
            if (!(c5 == 44 || c5 == 59)) {
                if (c5 == 91) {
                    this.peeked = 3;
                    return 3;
                } else if (c5 != 93) {
                    if (c5 != 123) {
                        this.pos--;
                        int result = peekKeyword();
                        if (result != 0) {
                            return result;
                        }
                        int result2 = peekNumber();
                        if (result2 != 0) {
                            return result2;
                        }
                        if (!isLiteral(this.buffer[this.pos])) {
                            throw syntaxError("Expected value");
                        }
                        checkLenient();
                        this.peeked = 10;
                        return 10;
                    }
                    this.peeked = 1;
                    return 1;
                } else if (peekStack == 1) {
                    this.peeked = 4;
                    return 4;
                }
            }
            if (peekStack == 1 || peekStack == 2) {
                checkLenient();
                this.pos--;
                this.peeked = 7;
                return 7;
            }
            throw syntaxError("Unexpected value");
        } else {
            checkLenient();
            this.peeked = 8;
            return 8;
        }
    }

    private int peekKeyword() throws IOException {
        int peeking;
        String keywordUpper;
        String keyword;
        char c = this.buffer[this.pos];
        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = 5;
        } else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = 6;
        } else if (c != 'n' && c != 'N') {
            return 0;
        } else {
            keyword = "null";
            keywordUpper = "NULL";
            peeking = 7;
        }
        int length = keyword.length();
        for (int i = 1; i < length; i++) {
            if (this.pos + i >= this.limit && !fillBuffer(i + 1)) {
                return 0;
            }
            char c2 = this.buffer[this.pos + i];
            if (c2 != keyword.charAt(i) && c2 != keywordUpper.charAt(i)) {
                return 0;
            }
        }
        if ((this.pos + length < this.limit || fillBuffer(length + 1)) && isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        this.peeked = peeking;
        return peeking;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0100, code lost:
        r8 = r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int peekNumber() throws java.io.IOException {
        /*
            r18 = this;
            r0 = r18
            char[] r1 = r0.buffer
            int r2 = r0.pos
            int r3 = r0.limit
            r4 = 0
            r6 = 0
            r7 = 1
            r8 = 0
            r9 = 0
            r10 = r4
            r4 = r3
            r3 = r2
            r2 = 0
        L_0x0012:
            int r5 = r3 + r2
            r13 = 0
            r15 = 2
            if (r5 != r4) goto L_0x0030
            int r5 = r1.length
            if (r2 != r5) goto L_0x001d
            return r9
        L_0x001d:
            int r5 = r2 + 1
            boolean r5 = r0.fillBuffer(r5)
            if (r5 != 0) goto L_0x002c
            r16 = r3
            r17 = r4
            goto L_0x00b5
        L_0x002c:
            int r3 = r0.pos
            int r4 = r0.limit
        L_0x0030:
            int r5 = r3 + r2
            char r5 = r1[r5]
            r12 = 43
            r9 = 5
            if (r5 == r12) goto L_0x0116
            r12 = 69
            if (r5 == r12) goto L_0x0108
            r12 = 101(0x65, float:1.42E-43)
            if (r5 == r12) goto L_0x0108
            switch(r5) {
                case 45: goto L_0x00f6;
                case 46: goto L_0x00ec;
                default: goto L_0x0044;
            }
        L_0x0044:
            r12 = 48
            if (r5 < r12) goto L_0x00aa
            r12 = 57
            if (r5 <= r12) goto L_0x0052
            r16 = r3
            r17 = r4
            goto L_0x00ae
        L_0x0052:
            r12 = 1
            if (r8 == r12) goto L_0x009e
            if (r8 != 0) goto L_0x005c
            r16 = r3
            r17 = r4
            goto L_0x00a2
        L_0x005c:
            if (r8 != r15) goto L_0x008c
            int r9 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r9 != 0) goto L_0x0064
            r9 = 0
            return r9
        L_0x0064:
            r13 = 10
            long r13 = r13 * r10
            int r9 = r5 + -48
            r16 = r3
            r17 = r4
            long r3 = (long) r9
            long r13 = r13 - r3
            r3 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            int r9 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r9 > 0) goto L_0x0084
            int r3 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r3 != 0) goto L_0x0082
            int r3 = (r13 > r10 ? 1 : (r13 == r10 ? 0 : -1))
            if (r3 >= 0) goto L_0x0082
            goto L_0x0084
        L_0x0082:
            r12 = 0
        L_0x0084:
            r3 = r7 & r12
            r9 = r13
            r7 = r3
            r10 = r9
            goto L_0x011f
        L_0x008c:
            r16 = r3
            r17 = r4
            r3 = 3
            if (r8 != r3) goto L_0x0096
            r3 = 4
            goto L_0x011e
        L_0x0096:
            if (r8 == r9) goto L_0x009b
            r3 = 6
            if (r8 != r3) goto L_0x011f
        L_0x009b:
            r3 = 7
            goto L_0x011e
        L_0x009e:
            r16 = r3
            r17 = r4
        L_0x00a2:
            int r3 = r5 + -48
            int r3 = -r3
            long r3 = (long) r3
            r8 = 2
            r10 = r3
            goto L_0x011f
        L_0x00aa:
            r16 = r3
            r17 = r4
        L_0x00ae:
            boolean r3 = r0.isLiteral(r5)
            if (r3 != 0) goto L_0x00ea
        L_0x00b5:
            if (r8 != r15) goto L_0x00d8
            if (r7 == 0) goto L_0x00d8
            r3 = -9223372036854775808
            int r3 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r3 != 0) goto L_0x00c1
            if (r6 == 0) goto L_0x00d8
        L_0x00c1:
            int r3 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r3 != 0) goto L_0x00c7
            if (r6 != 0) goto L_0x00d8
        L_0x00c7:
            if (r6 == 0) goto L_0x00cb
            r3 = r10
            goto L_0x00cc
        L_0x00cb:
            long r3 = -r10
        L_0x00cc:
            r0.peekedLong = r3
            int r3 = r0.pos
            int r3 = r3 + r2
            r0.pos = r3
            r3 = 15
            r0.peeked = r3
            return r3
        L_0x00d8:
            if (r8 == r15) goto L_0x00e3
            r3 = 4
            if (r8 == r3) goto L_0x00e3
            r3 = 7
            if (r8 != r3) goto L_0x00e1
            goto L_0x00e3
        L_0x00e1:
            r3 = 0
            return r3
        L_0x00e3:
            r0.peekedNumberLength = r2
            r3 = 16
            r0.peeked = r3
            return r3
        L_0x00ea:
            r3 = 0
            return r3
        L_0x00ec:
            r16 = r3
            r17 = r4
            r3 = 0
            if (r8 != r15) goto L_0x00f5
            r4 = 3
            goto L_0x0100
        L_0x00f5:
            return r3
        L_0x00f6:
            r16 = r3
            r17 = r4
            if (r8 != 0) goto L_0x0102
            r3 = 1
            r4 = 1
            r6 = r3
        L_0x0100:
            r8 = r4
            goto L_0x011f
        L_0x0102:
            if (r8 != r9) goto L_0x0106
            r3 = 6
            goto L_0x011e
        L_0x0106:
            r3 = 0
            return r3
        L_0x0108:
            r16 = r3
            r17 = r4
            r3 = 0
            if (r8 == r15) goto L_0x0114
            r4 = 4
            if (r8 != r4) goto L_0x0113
            goto L_0x0114
        L_0x0113:
            return r3
        L_0x0114:
            r3 = 5
            goto L_0x011e
        L_0x0116:
            r16 = r3
            r17 = r4
            if (r8 != r9) goto L_0x0128
            r3 = 6
        L_0x011e:
            r8 = r3
        L_0x011f:
            int r2 = r2 + 1
            r3 = r16
            r4 = r17
            r9 = 0
            goto L_0x0012
        L_0x0128:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.stream.JsonReader.peekNumber():int");
    }

    private boolean isLiteral(char c) throws IOException {
        switch (c) {
            case 9:
            case 10:
            case 12:
            case 13:
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case ShapeTypes.RIBBON_2 /*123*/:
            case ShapeTypes.ELLIPSE_RIBBON_2 /*125*/:
                break;
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\':
                checkLenient();
                break;
            default:
                return true;
        }
        return false;
    }

    public String nextName() throws IOException {
        String result;
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 14) {
            result = nextUnquotedValue();
        } else if (p == 12) {
            result = nextQuotedValue('\'');
        } else if (p == 13) {
            result = nextQuotedValue('\"');
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected a name but was ");
            sb.append(peek());
            sb.append(locationString());
            throw new IllegalStateException(sb.toString());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = result;
        return result;
    }

    public String nextString() throws IOException {
        String result;
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 10) {
            result = nextUnquotedValue();
        } else if (p == 8) {
            result = nextQuotedValue('\'');
        } else if (p == 9) {
            result = nextQuotedValue('\"');
        } else if (p == 11) {
            result = this.peekedString;
            this.peekedString = null;
        } else if (p == 15) {
            result = Long.toString(this.peekedLong);
        } else if (p == 16) {
            result = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected a string but was ");
            sb.append(peek());
            sb.append(locationString());
            throw new IllegalStateException(sb.toString());
        }
        this.peeked = 0;
        int[] iArr = this.pathIndices;
        int i = this.stackSize - 1;
        iArr[i] = iArr[i] + 1;
        return result;
    }

    public boolean nextBoolean() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 5) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            return true;
        } else if (p == 6) {
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr2[i2] = iArr2[i2] + 1;
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected a boolean but was ");
            sb.append(peek());
            sb.append(locationString());
            throw new IllegalStateException(sb.toString());
        }
    }

    public void nextNull() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 7) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected null but was ");
        sb.append(peek());
        sb.append(locationString());
        throw new IllegalStateException(sb.toString());
    }

    public double nextDouble() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 15) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            return (double) this.peekedLong;
        }
        if (p == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p == 8 || p == 9) {
            this.peekedString = nextQuotedValue(p == 8 ? '\'' : '\"');
        } else if (p == 10) {
            this.peekedString = nextUnquotedValue();
        } else if (p != 11) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected a double but was ");
            sb.append(peek());
            sb.append(locationString());
            throw new IllegalStateException(sb.toString());
        }
        this.peeked = 11;
        double result = Double.parseDouble(this.peekedString);
        if (this.lenient || (!Double.isNaN(result) && !Double.isInfinite(result))) {
            this.peekedString = null;
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr2[i2] = iArr2[i2] + 1;
            return result;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("JSON forbids NaN and infinities: ");
        sb2.append(result);
        sb2.append(locationString());
        throw new MalformedJsonException(sb2.toString());
    }

    public long nextLong() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 15) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            return this.peekedLong;
        }
        if (p == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p == 8 || p == 9 || p == 10) {
            if (p == 10) {
                this.peekedString = nextUnquotedValue();
            } else {
                this.peekedString = nextQuotedValue(p == 8 ? '\'' : '\"');
            }
            try {
                long result = Long.parseLong(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i2 = this.stackSize - 1;
                iArr2[i2] = iArr2[i2] + 1;
                return result;
            } catch (NumberFormatException e) {
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected a long but was ");
            sb.append(peek());
            sb.append(locationString());
            throw new IllegalStateException(sb.toString());
        }
        this.peeked = 11;
        double asDouble = Double.parseDouble(this.peekedString);
        long result2 = (long) asDouble;
        if (((double) result2) != asDouble) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected a long but was ");
            sb2.append(this.peekedString);
            sb2.append(locationString());
            throw new NumberFormatException(sb2.toString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr3 = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr3[i3] = iArr3[i3] + 1;
        return result2;
    }

    private String nextQuotedValue(char quote) throws IOException {
        char[] buffer2 = this.buffer;
        StringBuilder builder = null;
        while (true) {
            int start = this.pos;
            int l = this.limit;
            StringBuilder builder2 = builder;
            int c = start;
            while (c < l) {
                int p = c + 1;
                char c2 = buffer2[c];
                if (c2 == quote) {
                    this.pos = p;
                    int len = (p - start) - 1;
                    if (builder2 == null) {
                        return new String(buffer2, start, len);
                    }
                    builder2.append(buffer2, start, len);
                    return builder2.toString();
                } else if (c2 == '\\') {
                    this.pos = p;
                    int len2 = (p - start) - 1;
                    if (builder2 == null) {
                        builder2 = new StringBuilder(Math.max((len2 + 1) * 2, 16));
                    }
                    builder2.append(buffer2, start, len2);
                    builder2.append(readEscapeCharacter());
                    int p2 = this.pos;
                    l = this.limit;
                    start = p2;
                    c = p2;
                } else {
                    if (c2 == 10) {
                        this.lineNumber++;
                        this.lineStart = p;
                    }
                    c = p;
                }
            }
            if (builder2 == null) {
                builder2 = new StringBuilder(Math.max((c - start) * 2, 16));
            }
            builder2.append(buffer2, start, c - start);
            this.pos = c;
            if (!fillBuffer(1)) {
                throw syntaxError("Unterminated string");
            }
            builder = builder2;
        }
    }

    private String nextUnquotedValue() throws IOException {
        String result;
        StringBuilder builder = null;
        int i = 0;
        while (true) {
            if (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case ShapeTypes.RIBBON_2 /*123*/:
                    case ShapeTypes.ELLIPSE_RIBBON_2 /*125*/:
                        break;
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\':
                        checkLenient();
                        break;
                    default:
                        i++;
                        continue;
                }
            } else if (i >= this.buffer.length) {
                if (builder == null) {
                    builder = new StringBuilder(Math.max(i, 16));
                }
                builder.append(this.buffer, this.pos, i);
                this.pos += i;
                i = 0;
                if (!fillBuffer(1)) {
                }
            } else if (fillBuffer(i + 1)) {
            }
        }
        if (builder == null) {
            result = new String(this.buffer, this.pos, i);
        } else {
            builder.append(this.buffer, this.pos, i);
            result = builder.toString();
        }
        this.pos += i;
        return result;
    }

    private void skipQuotedValue(char quote) throws IOException {
        char[] buffer2 = this.buffer;
        do {
            int c = this.pos;
            int l = this.limit;
            while (c < l) {
                int p = c + 1;
                char c2 = buffer2[c];
                if (c2 == quote) {
                    this.pos = p;
                    return;
                } else if (c2 == '\\') {
                    this.pos = p;
                    readEscapeCharacter();
                    int p2 = this.pos;
                    l = this.limit;
                    c = p2;
                } else {
                    if (c2 == 10) {
                        this.lineNumber++;
                        this.lineStart = p;
                    }
                    c = p;
                }
            }
            this.pos = c;
        } while (fillBuffer(1) != 0);
        throw syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int i = 0;
            while (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case ShapeTypes.RIBBON_2 /*123*/:
                    case ShapeTypes.ELLIPSE_RIBBON_2 /*125*/:
                        break;
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\':
                        checkLenient();
                        break;
                    default:
                        i++;
                }
                this.pos += i;
                return;
            }
            this.pos += i;
        } while (fillBuffer(1));
    }

    public int nextInt() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 15) {
            int result = (int) this.peekedLong;
            if (this.peekedLong != ((long) result)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected an int but was ");
                sb.append(this.peekedLong);
                sb.append(locationString());
                throw new NumberFormatException(sb.toString());
            }
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i = this.stackSize - 1;
            iArr[i] = iArr[i] + 1;
            return result;
        }
        if (p == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p == 8 || p == 9 || p == 10) {
            if (p == 10) {
                this.peekedString = nextUnquotedValue();
            } else {
                this.peekedString = nextQuotedValue(p == 8 ? '\'' : '\"');
            }
            try {
                int result2 = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i2 = this.stackSize - 1;
                iArr2[i2] = iArr2[i2] + 1;
                return result2;
            } catch (NumberFormatException e) {
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected an int but was ");
            sb2.append(peek());
            sb2.append(locationString());
            throw new IllegalStateException(sb2.toString());
        }
        this.peeked = 11;
        double asDouble = Double.parseDouble(this.peekedString);
        int result3 = (int) asDouble;
        if (((double) result3) != asDouble) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Expected an int but was ");
            sb3.append(this.peekedString);
            sb3.append(locationString());
            throw new NumberFormatException(sb3.toString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr3 = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr3[i3] = iArr3[i3] + 1;
        return result3;
    }

    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.f58in.close();
    }

    public void skipValue() throws IOException {
        int count = 0;
        do {
            int p = this.peeked;
            if (p == 0) {
                p = doPeek();
            }
            if (p == 3) {
                push(1);
                count++;
            } else if (p == 1) {
                push(3);
                count++;
            } else if (p == 4) {
                this.stackSize--;
                count--;
            } else if (p == 2) {
                this.stackSize--;
                count--;
            } else if (p == 14 || p == 10) {
                skipUnquotedValue();
            } else if (p == 8 || p == 12) {
                skipQuotedValue('\'');
            } else if (p == 9 || p == 13) {
                skipQuotedValue('\"');
            } else if (p == 16) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
        } while (count != 0);
        int[] iArr = this.pathIndices;
        int i = this.stackSize - 1;
        iArr[i] = iArr[i] + 1;
        this.pathNames[this.stackSize - 1] = "null";
    }

    private void push(int newTop) {
        if (this.stackSize == this.stack.length) {
            int[] newStack = new int[(this.stackSize * 2)];
            int[] newPathIndices = new int[(this.stackSize * 2)];
            String[] newPathNames = new String[(this.stackSize * 2)];
            System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
            System.arraycopy(this.pathIndices, 0, newPathIndices, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, newPathNames, 0, this.stackSize);
            this.stack = newStack;
            this.pathIndices = newPathIndices;
            this.pathNames = newPathNames;
        }
        int[] newStack2 = this.stack;
        int i = this.stackSize;
        this.stackSize = i + 1;
        newStack2[i] = newTop;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        char[] buffer2 = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer2, this.pos, buffer2, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            int read = this.f58in.read(buffer2, this.limit, buffer2.length - this.limit);
            int total = read;
            if (read == -1) {
                return false;
            }
            this.limit += total;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer2[0] == 65279) {
                this.pos++;
                this.lineStart++;
                minimum++;
            }
        } while (this.limit < minimum);
        return true;
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        int p;
        char[] buffer2 = this.buffer;
        int p2 = this.pos;
        int l = this.limit;
        while (true) {
            if (p2 == l) {
                this.pos = p2;
                if (fillBuffer(1)) {
                    p2 = this.pos;
                    l = this.limit;
                } else if (!throwOnEof) {
                    return -1;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("End of input");
                    sb.append(locationString());
                    throw new EOFException(sb.toString());
                }
            }
            int p3 = p2 + 1;
            char p4 = buffer2[p2];
            if (p4 == 10) {
                this.lineNumber++;
                this.lineStart = p3;
            } else if (!(p4 == ' ' || p4 == 13 || p4 == 9)) {
                if (p4 == '/') {
                    this.pos = p3;
                    if (p3 == l) {
                        this.pos--;
                        boolean charsLoaded = fillBuffer(2);
                        this.pos++;
                        if (!charsLoaded) {
                            return p4;
                        }
                    }
                    checkLenient();
                    char peek = buffer2[this.pos];
                    if (peek == '*') {
                        this.pos++;
                        if (!skipTo("*/")) {
                            throw syntaxError("Unterminated comment");
                        }
                        p = this.pos + 2;
                        l = this.limit;
                    } else if (peek != '/') {
                        return p4;
                    } else {
                        this.pos++;
                        skipToEndOfLine();
                        p = this.pos;
                        l = this.limit;
                    }
                } else if (p4 == '#') {
                    this.pos = p3;
                    checkLenient();
                    skipToEndOfLine();
                    p = this.pos;
                    l = this.limit;
                } else {
                    this.pos = p3;
                    return p4;
                }
                p2 = p;
            }
            p2 = p3;
        }
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        char c;
        do {
            if (this.pos < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                c = cArr[i];
                if (c == 10) {
                    this.lineNumber++;
                    this.lineStart = this.pos;
                    return;
                }
            } else {
                return;
            }
        } while (c != 13);
    }

    private boolean skipTo(String toFind) throws IOException {
        int length = toFind.length();
        while (true) {
            int c = 0;
            if (this.pos + length > this.limit && !fillBuffer(length)) {
                return false;
            }
            if (this.buffer[this.pos] == 10) {
                this.lineNumber++;
                this.lineStart = this.pos + 1;
            } else {
                while (true) {
                    int c2 = c;
                    if (c2 >= length) {
                        return true;
                    }
                    if (this.buffer[this.pos + c2] != toFind.charAt(c2)) {
                        break;
                    }
                    c = c2 + 1;
                }
            }
            this.pos++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(locationString());
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public String locationString() {
        int line = this.lineNumber + 1;
        int column = (this.pos - this.lineStart) + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(" at line ");
        sb.append(line);
        sb.append(" column ");
        sb.append(column);
        sb.append(" path ");
        sb.append(getPath());
        return sb.toString();
    }

    public String getPath() {
        StringBuilder result = new StringBuilder().append('$');
        int size = this.stackSize;
        for (int i = 0; i < size; i++) {
            switch (this.stack[i]) {
                case 1:
                case 2:
                    result.append('[');
                    result.append(this.pathIndices[i]);
                    result.append(']');
                    break;
                case 3:
                case 4:
                case 5:
                    result.append('.');
                    if (this.pathNames[i] == null) {
                        break;
                    } else {
                        result.append(this.pathNames[i]);
                        break;
                    }
            }
        }
        return result.toString();
    }

    private char readEscapeCharacter() throws IOException {
        int i;
        if (this.pos != this.limit || fillBuffer(1)) {
            char[] cArr = this.buffer;
            int i2 = this.pos;
            this.pos = i2 + 1;
            char escaped = cArr[i2];
            if (escaped == 10) {
                this.lineNumber++;
                this.lineStart = this.pos;
            } else if (!(escaped == '\"' || escaped == '\'' || escaped == '/' || escaped == '\\')) {
                if (escaped == 'b') {
                    return 8;
                }
                if (escaped == 'f') {
                    return 12;
                }
                if (escaped == 'n') {
                    return 10;
                }
                if (escaped == 'r') {
                    return 13;
                }
                switch (escaped) {
                    case 't':
                        return 9;
                    case 'u':
                        if (this.pos + 4 <= this.limit || fillBuffer(4)) {
                            char result = 0;
                            int i3 = this.pos;
                            int end = i3 + 4;
                            while (i3 < end) {
                                char c = this.buffer[i3];
                                char result2 = (char) (result << 4);
                                if (c >= '0' && c <= '9') {
                                    i = c - '0';
                                } else if (c >= 'a' && c <= 'f') {
                                    i = (c - 'a') + 10;
                                } else if (c < 'A' || c > 'F') {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("\\u");
                                    sb.append(new String(this.buffer, this.pos, 4));
                                    throw new NumberFormatException(sb.toString());
                                } else {
                                    i = (c - 'A') + 10;
                                }
                                result = (char) (i + result2);
                                i3++;
                            }
                            this.pos += 4;
                            return result;
                        }
                        throw syntaxError("Unterminated escape sequence");
                    default:
                        throw syntaxError("Invalid escape sequence");
                }
            }
            return escaped;
        }
        throw syntaxError("Unterminated escape sequence");
    }

    private IOException syntaxError(String message) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append(locationString());
        throw new MalformedJsonException(sb.toString());
    }

    private void consumeNonExecutePrefix() throws IOException {
        nextNonWhitespace(true);
        this.pos--;
        if (this.pos + NON_EXECUTE_PREFIX.length <= this.limit || fillBuffer(NON_EXECUTE_PREFIX.length)) {
            int i = 0;
            while (i < NON_EXECUTE_PREFIX.length) {
                if (this.buffer[this.pos + i] == NON_EXECUTE_PREFIX[i]) {
                    i++;
                } else {
                    return;
                }
            }
            this.pos += NON_EXECUTE_PREFIX.length;
        }
    }
}
