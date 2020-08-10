package org.apache.poi.hpsf;

import org.apache.poi.util.HexDump;

public class IllegalVariantTypeException extends VariantTypeException {
    public IllegalVariantTypeException(long variantType, Object value, String msg) {
        super(variantType, value, msg);
    }

    public IllegalVariantTypeException(long variantType, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append("The variant type ");
        sb.append(variantType);
        sb.append(" (");
        sb.append(Variant.getVariantName(variantType));
        sb.append(", ");
        sb.append(HexDump.toHex(variantType));
        sb.append(") is illegal in this context.");
        this(variantType, value, sb.toString());
    }
}
