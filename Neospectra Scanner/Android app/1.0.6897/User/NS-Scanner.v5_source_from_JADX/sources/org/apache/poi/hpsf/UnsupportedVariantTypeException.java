package org.apache.poi.hpsf;

import org.apache.poi.util.HexDump;

public abstract class UnsupportedVariantTypeException extends VariantTypeException {
    public UnsupportedVariantTypeException(long variantType, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append("HPSF does not yet support the variant type ");
        sb.append(variantType);
        sb.append(" (");
        sb.append(Variant.getVariantName(variantType));
        sb.append(", ");
        sb.append(HexDump.toHex(variantType));
        sb.append("). If you want support for ");
        sb.append("this variant type in one of the next POI releases please ");
        sb.append("submit a request for enhancement (RFE) to ");
        sb.append("<http://issues.apache.org/bugzilla/>! Thank you!");
        super(variantType, value, sb.toString());
    }
}
