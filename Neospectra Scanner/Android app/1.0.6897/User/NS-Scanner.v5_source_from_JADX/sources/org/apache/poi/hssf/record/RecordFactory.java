package org.apache.poi.hssf.record;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.record.chart.BeginRecord;
import org.apache.poi.hssf.record.chart.CatLabRecord;
import org.apache.poi.hssf.record.chart.ChartEndBlockRecord;
import org.apache.poi.hssf.record.chart.ChartEndObjectRecord;
import org.apache.poi.hssf.record.chart.ChartFRTInfoRecord;
import org.apache.poi.hssf.record.chart.ChartRecord;
import org.apache.poi.hssf.record.chart.ChartStartBlockRecord;
import org.apache.poi.hssf.record.chart.ChartStartObjectRecord;
import org.apache.poi.hssf.record.chart.ChartTitleFormatRecord;
import org.apache.poi.hssf.record.chart.DataFormatRecord;
import org.apache.poi.hssf.record.chart.EndRecord;
import org.apache.poi.hssf.record.chart.LegendRecord;
import org.apache.poi.hssf.record.chart.LinkedDataRecord;
import org.apache.poi.hssf.record.chart.SeriesRecord;
import org.apache.poi.hssf.record.chart.SeriesTextRecord;
import org.apache.poi.hssf.record.chart.SeriesToChartGroupRecord;
import org.apache.poi.hssf.record.chart.ValueRangeRecord;
import org.apache.poi.hssf.record.pivottable.DataItemRecord;
import org.apache.poi.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord;
import org.apache.poi.hssf.record.pivottable.PageItemRecord;
import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
import org.apache.poi.hssf.record.pivottable.ViewDefinitionRecord;
import org.apache.poi.hssf.record.pivottable.ViewFieldsRecord;
import org.apache.poi.hssf.record.pivottable.ViewSourceRecord;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public final class RecordFactory {
    private static final Class<?>[] CONSTRUCTOR_ARGS = {RecordInputStream.class};
    private static final int NUM_RECORDS = 512;
    private static short[] _allKnownRecordSIDs;
    private static final Map<Integer, I_RecordCreator> _recordCreatorsById = recordsToMap(recordClasses);
    private static final Class<? extends Record>[] recordClasses;

    private interface I_RecordCreator {
        Record create(RecordInputStream recordInputStream);

        Class<? extends Record> getRecordClass();
    }

    private static final class ReflectionConstructorRecordCreator implements I_RecordCreator {

        /* renamed from: _c */
        private final Constructor<? extends Record> f818_c;

        public ReflectionConstructorRecordCreator(Constructor<? extends Record> c) {
            this.f818_c = c;
        }

        public Record create(RecordInputStream in) {
            try {
                return (Record) this.f818_c.newInstance(new Object[]{in});
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e2) {
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException(e3);
            } catch (InvocationTargetException e4) {
                throw new RecordFormatException("Unable to construct record instance", e4.getTargetException());
            }
        }

        public Class<? extends Record> getRecordClass() {
            return this.f818_c.getDeclaringClass();
        }
    }

    private static final class ReflectionMethodRecordCreator implements I_RecordCreator {

        /* renamed from: _m */
        private final Method f819_m;

        public ReflectionMethodRecordCreator(Method m) {
            this.f819_m = m;
        }

        public Record create(RecordInputStream in) {
            try {
                return (Record) this.f819_m.invoke(null, new Object[]{in});
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RecordFormatException("Unable to construct record instance", e3.getTargetException());
            }
        }

        public Class<? extends Record> getRecordClass() {
            return this.f819_m.getDeclaringClass();
        }
    }

    static {
        Class[] clsArr = new Class[ShapeTypes.FLOW_CHART_PREDEFINED_PROCESS];
        clsArr[0] = ArrayRecord.class;
        clsArr[1] = AutoFilterInfoRecord.class;
        clsArr[2] = BackupRecord.class;
        clsArr[3] = BlankRecord.class;
        clsArr[4] = BOFRecord.class;
        clsArr[5] = BookBoolRecord.class;
        clsArr[6] = BoolErrRecord.class;
        clsArr[7] = BottomMarginRecord.class;
        clsArr[8] = BoundSheetRecord.class;
        clsArr[9] = CalcCountRecord.class;
        clsArr[10] = CalcModeRecord.class;
        clsArr[11] = CFHeaderRecord.class;
        clsArr[12] = CFRuleRecord.class;
        clsArr[13] = ChartRecord.class;
        clsArr[14] = ChartTitleFormatRecord.class;
        clsArr[15] = CodepageRecord.class;
        clsArr[16] = ColumnInfoRecord.class;
        clsArr[17] = ContinueRecord.class;
        clsArr[18] = CountryRecord.class;
        clsArr[19] = CRNCountRecord.class;
        clsArr[20] = CRNRecord.class;
        clsArr[21] = DateWindow1904Record.class;
        clsArr[22] = DBCellRecord.class;
        clsArr[23] = DefaultColWidthRecord.class;
        clsArr[24] = DefaultRowHeightRecord.class;
        clsArr[25] = DeltaRecord.class;
        clsArr[26] = DimensionsRecord.class;
        clsArr[27] = DrawingGroupRecord.class;
        clsArr[28] = DrawingRecord.class;
        clsArr[29] = DrawingSelectionRecord.class;
        clsArr[30] = DSFRecord.class;
        clsArr[31] = DVALRecord.class;
        clsArr[32] = DVRecord.class;
        clsArr[33] = EOFRecord.class;
        clsArr[34] = ExtendedFormatRecord.class;
        clsArr[35] = ExternalNameRecord.class;
        clsArr[36] = ExternSheetRecord.class;
        clsArr[37] = ExtSSTRecord.class;
        clsArr[38] = FeatRecord.class;
        clsArr[39] = FeatHdrRecord.class;
        clsArr[40] = FilePassRecord.class;
        clsArr[41] = FileSharingRecord.class;
        clsArr[42] = FnGroupCountRecord.class;
        clsArr[43] = FontRecord.class;
        clsArr[44] = FooterRecord.class;
        clsArr[45] = FormatRecord.class;
        clsArr[46] = FormulaRecord.class;
        clsArr[47] = GridsetRecord.class;
        clsArr[48] = GutsRecord.class;
        clsArr[49] = HCenterRecord.class;
        clsArr[50] = HeaderRecord.class;
        clsArr[51] = HeaderFooterRecord.class;
        clsArr[52] = HideObjRecord.class;
        clsArr[53] = HorizontalPageBreakRecord.class;
        clsArr[54] = HyperlinkRecord.class;
        clsArr[55] = IndexRecord.class;
        clsArr[56] = InterfaceEndRecord.class;
        clsArr[57] = InterfaceHdrRecord.class;
        clsArr[58] = IterationRecord.class;
        clsArr[59] = LabelRecord.class;
        clsArr[60] = LabelSSTRecord.class;
        clsArr[61] = LeftMarginRecord.class;
        clsArr[62] = LegendRecord.class;
        clsArr[63] = MergeCellsRecord.class;
        clsArr[64] = MMSRecord.class;
        clsArr[65] = MulBlankRecord.class;
        clsArr[66] = MulRKRecord.class;
        clsArr[67] = NameRecord.class;
        clsArr[68] = NameCommentRecord.class;
        clsArr[69] = NoteRecord.class;
        clsArr[70] = NumberRecord.class;
        clsArr[71] = ObjectProtectRecord.class;
        clsArr[72] = ObjRecord.class;
        clsArr[73] = PaletteRecord.class;
        clsArr[74] = PaneRecord.class;
        clsArr[75] = PasswordRecord.class;
        clsArr[76] = PasswordRev4Record.class;
        clsArr[77] = PrecisionRecord.class;
        clsArr[78] = PrintGridlinesRecord.class;
        clsArr[79] = PrintHeadersRecord.class;
        clsArr[80] = PrintSetupRecord.class;
        clsArr[81] = ProtectionRev4Record.class;
        clsArr[82] = ProtectRecord.class;
        clsArr[83] = RecalcIdRecord.class;
        clsArr[84] = RefModeRecord.class;
        clsArr[85] = RefreshAllRecord.class;
        clsArr[86] = RightMarginRecord.class;
        clsArr[87] = RKRecord.class;
        clsArr[88] = RowRecord.class;
        clsArr[89] = SaveRecalcRecord.class;
        clsArr[90] = ScenarioProtectRecord.class;
        clsArr[91] = SelectionRecord.class;
        clsArr[92] = SeriesRecord.class;
        clsArr[93] = SeriesTextRecord.class;
        clsArr[94] = SharedFormulaRecord.class;
        clsArr[95] = SSTRecord.class;
        clsArr[96] = StringRecord.class;
        clsArr[97] = StyleRecord.class;
        clsArr[98] = SupBookRecord.class;
        clsArr[99] = TabIdRecord.class;
        clsArr[100] = TableRecord.class;
        clsArr[101] = TableStylesRecord.class;
        clsArr[102] = TextObjectRecord.class;
        clsArr[103] = TopMarginRecord.class;
        clsArr[104] = UncalcedRecord.class;
        clsArr[105] = UseSelFSRecord.class;
        clsArr[106] = UserSViewBegin.class;
        clsArr[107] = UserSViewEnd.class;
        clsArr[108] = ValueRangeRecord.class;
        clsArr[109] = VCenterRecord.class;
        clsArr[110] = VerticalPageBreakRecord.class;
        clsArr[111] = WindowOneRecord.class;
        clsArr[112] = WindowProtectRecord.class;
        clsArr[113] = WindowTwoRecord.class;
        clsArr[114] = WriteAccessRecord.class;
        clsArr[115] = WriteProtectRecord.class;
        clsArr[116] = WSBoolRecord.class;
        clsArr[117] = BeginRecord.class;
        clsArr[118] = ChartFRTInfoRecord.class;
        clsArr[119] = ChartStartBlockRecord.class;
        clsArr[120] = ChartEndBlockRecord.class;
        clsArr[121] = ChartStartObjectRecord.class;
        clsArr[122] = ChartEndObjectRecord.class;
        clsArr[123] = CatLabRecord.class;
        clsArr[124] = DataFormatRecord.class;
        clsArr[125] = EndRecord.class;
        clsArr[126] = LinkedDataRecord.class;
        clsArr[127] = SeriesToChartGroupRecord.class;
        clsArr[128] = DataItemRecord.class;
        clsArr[129] = ExtendedPivotTableViewFieldsRecord.class;
        clsArr[130] = PageItemRecord.class;
        clsArr[131] = StreamIDRecord.class;
        clsArr[132] = ViewDefinitionRecord.class;
        clsArr[133] = ViewFieldsRecord.class;
        clsArr[134] = ViewSourceRecord.class;
        recordClasses = clsArr;
    }

    public static Class<? extends Record> getRecordClass(int sid) {
        I_RecordCreator rc = (I_RecordCreator) _recordCreatorsById.get(Integer.valueOf(sid));
        if (rc == null) {
            return null;
        }
        return rc.getRecordClass();
    }

    public static Record[] createRecord(RecordInputStream in) {
        Record record = createSingleRecord(in);
        if (record instanceof DBCellRecord) {
            return new Record[]{null};
        } else if (record instanceof RKRecord) {
            return new Record[]{convertToNumberRecord((RKRecord) record)};
        } else if (record instanceof MulRKRecord) {
            return convertRKRecords((MulRKRecord) record);
        } else {
            return new Record[]{record};
        }
    }

    public static Record createSingleRecord(RecordInputStream in) {
        I_RecordCreator constructor = (I_RecordCreator) _recordCreatorsById.get(Integer.valueOf(in.getSid()));
        if (constructor == null) {
            return new UnknownRecord(in);
        }
        return constructor.create(in);
    }

    public static NumberRecord convertToNumberRecord(RKRecord rk) {
        NumberRecord num = new NumberRecord();
        num.setColumn(rk.getColumn());
        num.setRow(rk.getRow());
        num.setXFIndex(rk.getXFIndex());
        num.setValue(rk.getRKNumber());
        return num;
    }

    public static NumberRecord[] convertRKRecords(MulRKRecord mrk) {
        NumberRecord[] mulRecs = new NumberRecord[mrk.getNumColumns()];
        for (int k = 0; k < mrk.getNumColumns(); k++) {
            NumberRecord nr = new NumberRecord();
            nr.setColumn((short) (mrk.getFirstColumn() + k));
            nr.setRow(mrk.getRow());
            nr.setXFIndex(mrk.getXFAt(k));
            nr.setValue(mrk.getRKNumberAt(k));
            mulRecs[k] = nr;
        }
        return mulRecs;
    }

    public static BlankRecord[] convertBlankRecords(MulBlankRecord mbk) {
        BlankRecord[] mulRecs = new BlankRecord[mbk.getNumColumns()];
        for (int k = 0; k < mbk.getNumColumns(); k++) {
            BlankRecord br = new BlankRecord();
            br.setColumn((short) (mbk.getFirstColumn() + k));
            br.setRow(mbk.getRow());
            br.setXFIndex(mbk.getXFAt(k));
            mulRecs[k] = br;
        }
        return mulRecs;
    }

    public static short[] getAllKnownRecordSIDs() {
        if (_allKnownRecordSIDs == null) {
            short[] results = new short[_recordCreatorsById.size()];
            int i = 0;
            for (Integer sid : _recordCreatorsById.keySet()) {
                int i2 = i + 1;
                results[i] = sid.shortValue();
                i = i2;
            }
            Arrays.sort(results);
            _allKnownRecordSIDs = results;
        }
        return (short[]) _allKnownRecordSIDs.clone();
    }

    private static Map<Integer, I_RecordCreator> recordsToMap(Class<? extends Record>[] records) {
        Map<Integer, I_RecordCreator> result = new HashMap<>();
        Set<Class<?>> uniqueRecClasses = new HashSet<>((records.length * 3) / 2);
        int i = 0;
        while (i < records.length) {
            Class<? extends Record> recClass = records[i];
            if (!Record.class.isAssignableFrom(recClass)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid record sub-class (");
                sb.append(recClass.getName());
                sb.append(")");
                throw new RuntimeException(sb.toString());
            } else if (Modifier.isAbstract(recClass.getModifiers())) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid record class (");
                sb2.append(recClass.getName());
                sb2.append(") - must not be abstract");
                throw new RuntimeException(sb2.toString());
            } else if (!uniqueRecClasses.add(recClass)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("duplicate record class (");
                sb3.append(recClass.getName());
                sb3.append(")");
                throw new RuntimeException(sb3.toString());
            } else {
                try {
                    int sid = recClass.getField("sid").getShort(null);
                    Integer key = Integer.valueOf(sid);
                    if (result.containsKey(key)) {
                        Class<?> prevClass = ((I_RecordCreator) result.get(key)).getRecordClass();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("duplicate record sid 0x");
                        sb4.append(Integer.toHexString(sid).toUpperCase());
                        sb4.append(" for classes (");
                        sb4.append(recClass.getName());
                        sb4.append(") and (");
                        sb4.append(prevClass.getName());
                        sb4.append(")");
                        throw new RuntimeException(sb4.toString());
                    }
                    result.put(key, getRecordCreator(recClass));
                    i++;
                } catch (Exception e) {
                    throw new RecordFormatException("Unable to determine record types");
                }
            }
        }
        return result;
    }

    private static I_RecordCreator getRecordCreator(Class<? extends Record> recClass) {
        try {
            return new ReflectionConstructorRecordCreator(recClass.getConstructor(CONSTRUCTOR_ARGS));
        } catch (NoSuchMethodException e) {
            try {
                return new ReflectionMethodRecordCreator(recClass.getDeclaredMethod("create", CONSTRUCTOR_ARGS));
            } catch (NoSuchMethodException e2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to find constructor or create method for (");
                sb.append(recClass.getName());
                sb.append(").");
                throw new RuntimeException(sb.toString());
            }
        }
    }

    public static List<Record> createRecords(InputStream in) throws RecordFormatException {
        List<Record> records = new ArrayList<>(512);
        RecordFactoryInputStream recStream = new RecordFactoryInputStream(in, true);
        while (true) {
            Record nextRecord = recStream.nextRecord();
            Record record = nextRecord;
            if (nextRecord == null) {
                return records;
            }
            records.add(record);
        }
    }
}
