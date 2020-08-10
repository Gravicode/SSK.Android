package org.apache.poi.util;

public class ArrayUtil {
    public static void arraycopy(byte[] src, int src_position, byte[] dst, int dst_position, int length) {
        if (src_position < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("src_position was less than 0.  Actual value ");
            sb.append(src_position);
            throw new IllegalArgumentException(sb.toString());
        } else if (src_position >= src.length) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("src_position was greater than src array size.  Tried to write starting at position ");
            sb2.append(src_position);
            sb2.append(" but the array length is ");
            sb2.append(src.length);
            throw new IllegalArgumentException(sb2.toString());
        } else if (src_position + length > src.length) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("src_position + length would overrun the src array.  Expected end at ");
            sb3.append(src_position + length);
            sb3.append(" actual end at ");
            sb3.append(src.length);
            throw new IllegalArgumentException(sb3.toString());
        } else if (dst_position < 0) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("dst_position was less than 0.  Actual value ");
            sb4.append(dst_position);
            throw new IllegalArgumentException(sb4.toString());
        } else if (dst_position >= dst.length) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("dst_position was greater than dst array size.  Tried to write starting at position ");
            sb5.append(dst_position);
            sb5.append(" but the array length is ");
            sb5.append(dst.length);
            throw new IllegalArgumentException(sb5.toString());
        } else if (dst_position + length > dst.length) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("dst_position + length would overrun the dst array.  Expected end at ");
            sb6.append(dst_position + length);
            sb6.append(" actual end at ");
            sb6.append(dst.length);
            throw new IllegalArgumentException(sb6.toString());
        } else {
            System.arraycopy(src, src_position, dst, dst_position, length);
        }
    }

    public static void arrayMoveWithin(Object[] array, int moveFrom, int moveTo, int numToMove) {
        int shiftTo;
        Object[] toShift;
        if (numToMove <= 0 || moveFrom == moveTo) {
            return;
        }
        if (moveFrom < 0 || moveFrom >= array.length) {
            throw new IllegalArgumentException("The moveFrom must be a valid array index");
        } else if (moveTo < 0 || moveTo >= array.length) {
            throw new IllegalArgumentException("The moveTo must be a valid array index");
        } else if (moveFrom + numToMove > array.length) {
            throw new IllegalArgumentException("Asked to move more entries than the array has");
        } else if (moveTo + numToMove > array.length) {
            throw new IllegalArgumentException("Asked to move to a position that doesn't have enough space");
        } else {
            Object[] toMove = new Object[numToMove];
            System.arraycopy(array, moveFrom, toMove, 0, numToMove);
            if (moveFrom > moveTo) {
                toShift = new Object[(moveFrom - moveTo)];
                System.arraycopy(array, moveTo, toShift, 0, toShift.length);
                shiftTo = moveTo + numToMove;
            } else {
                toShift = new Object[(moveTo - moveFrom)];
                System.arraycopy(array, moveFrom + numToMove, toShift, 0, toShift.length);
                shiftTo = moveFrom;
            }
            System.arraycopy(toMove, 0, array, moveTo, toMove.length);
            System.arraycopy(toShift, 0, array, shiftTo, toShift.length);
        }
    }
}
