package com.google.android.gms.internal;

final class zzfih {
    static String zzbc(zzfes zzfes) {
        String str;
        zzfii zzfii = new zzfii(zzfes);
        StringBuilder sb = new StringBuilder(zzfii.size());
        for (int i = 0; i < zzfii.size(); i++) {
            int zzkn = zzfii.zzkn(i);
            if (zzkn == 34) {
                str = "\\\"";
            } else if (zzkn == 39) {
                str = "\\'";
            } else if (zzkn != 92) {
                switch (zzkn) {
                    case 7:
                        str = "\\a";
                        break;
                    case 8:
                        str = "\\b";
                        break;
                    case 9:
                        str = "\\t";
                        break;
                    case 10:
                        str = "\\n";
                        break;
                    case 11:
                        str = "\\v";
                        break;
                    case 12:
                        str = "\\f";
                        break;
                    case 13:
                        str = "\\r";
                        break;
                    default:
                        if (zzkn < 32 || zzkn > 126) {
                            sb.append('\\');
                            sb.append((char) (((zzkn >>> 6) & 3) + 48));
                            sb.append((char) (((zzkn >>> 3) & 7) + 48));
                            zzkn = (zzkn & 7) + 48;
                        }
                        sb.append((char) zzkn);
                        continue;
                }
            } else {
                str = "\\\\";
            }
            sb.append(str);
        }
        return sb.toString();
    }
}
