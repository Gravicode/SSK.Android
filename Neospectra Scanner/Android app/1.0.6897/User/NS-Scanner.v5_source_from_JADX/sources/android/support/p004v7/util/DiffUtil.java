package android.support.p004v7.util;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.p004v7.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* renamed from: android.support.v7.util.DiffUtil */
public class DiffUtil {
    private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>() {
        public int compare(Snake o1, Snake o2) {
            int cmpX = o1.f49x - o2.f49x;
            return cmpX == 0 ? o1.f50y - o2.f50y : cmpX;
        }
    };

    /* renamed from: android.support.v7.util.DiffUtil$Callback */
    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int i, int i2);

        public abstract boolean areItemsTheSame(int i, int i2);

        public abstract int getNewListSize();

        public abstract int getOldListSize();

        @Nullable
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return null;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$DiffResult */
    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;

        DiffResult(Callback callback, List<Snake> snakes, int[] oldItemStatuses, int[] newItemStatuses, boolean detectMoves) {
            this.mSnakes = snakes;
            this.mOldItemStatuses = oldItemStatuses;
            this.mNewItemStatuses = newItemStatuses;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = detectMoves;
            addRootSnake();
            findMatchingItems();
        }

        private void addRootSnake() {
            Snake firstSnake = this.mSnakes.isEmpty() ? null : (Snake) this.mSnakes.get(0);
            if (firstSnake == null || firstSnake.f49x != 0 || firstSnake.f50y != 0) {
                Snake root = new Snake();
                root.f49x = 0;
                root.f50y = 0;
                root.removal = false;
                root.size = 0;
                root.reverse = false;
                this.mSnakes.add(0, root);
            }
        }

        private void findMatchingItems() {
            int posOld = this.mOldListSize;
            int posNew = this.mNewListSize;
            for (int i = this.mSnakes.size() - 1; i >= 0; i--) {
                Snake snake = (Snake) this.mSnakes.get(i);
                int endX = snake.f49x + snake.size;
                int endY = snake.f50y + snake.size;
                if (this.mDetectMoves) {
                    while (posOld > endX) {
                        findAddition(posOld, posNew, i);
                        posOld--;
                    }
                    while (posNew > endY) {
                        findRemoval(posOld, posNew, i);
                        posNew--;
                    }
                }
                for (int j = 0; j < snake.size; j++) {
                    int oldItemPos = snake.f49x + j;
                    int newItemPos = snake.f50y + j;
                    int changeFlag = this.mCallback.areContentsTheSame(oldItemPos, newItemPos) ? 1 : 2;
                    this.mOldItemStatuses[oldItemPos] = (newItemPos << 5) | changeFlag;
                    this.mNewItemStatuses[newItemPos] = (oldItemPos << 5) | changeFlag;
                }
                posOld = snake.f49x;
                posNew = snake.f50y;
            }
        }

        private void findAddition(int x, int y, int snakeIndex) {
            if (this.mOldItemStatuses[x - 1] == 0) {
                findMatchingItem(x, y, snakeIndex, false);
            }
        }

        private void findRemoval(int x, int y, int snakeIndex) {
            if (this.mNewItemStatuses[y - 1] == 0) {
                findMatchingItem(x, y, snakeIndex, true);
            }
        }

        private boolean findMatchingItem(int x, int y, int snakeIndex, boolean removal) {
            int curY;
            int curX;
            int myItemPos;
            if (removal) {
                myItemPos = y - 1;
                curX = x;
                curY = y - 1;
            } else {
                myItemPos = x - 1;
                curX = x - 1;
                curY = y;
            }
            int curY2 = curY;
            int curX2 = curX;
            for (int i = snakeIndex; i >= 0; i--) {
                Snake snake = (Snake) this.mSnakes.get(i);
                int endX = snake.f49x + snake.size;
                int endY = snake.f50y + snake.size;
                int changeFlag = 4;
                if (removal) {
                    for (int pos = curX2 - 1; pos >= endX; pos--) {
                        if (this.mCallback.areItemsTheSame(pos, myItemPos)) {
                            if (this.mCallback.areContentsTheSame(pos, myItemPos)) {
                                changeFlag = 8;
                            }
                            this.mNewItemStatuses[myItemPos] = (pos << 5) | 16;
                            this.mOldItemStatuses[pos] = (myItemPos << 5) | changeFlag;
                            return true;
                        }
                    }
                    continue;
                } else {
                    for (int pos2 = curY2 - 1; pos2 >= endY; pos2--) {
                        if (this.mCallback.areItemsTheSame(myItemPos, pos2)) {
                            if (this.mCallback.areContentsTheSame(myItemPos, pos2)) {
                                changeFlag = 8;
                            }
                            this.mOldItemStatuses[x - 1] = (pos2 << 5) | 16;
                            this.mNewItemStatuses[pos2] = ((x - 1) << 5) | changeFlag;
                            return true;
                        }
                    }
                    continue;
                }
                curX2 = snake.f49x;
                curY2 = snake.f50y;
            }
            return false;
        }

        public void dispatchUpdatesTo(Adapter adapter) {
            dispatchUpdatesTo((ListUpdateCallback) new AdapterListUpdateCallback(adapter));
        }

        /* JADX WARNING: type inference failed for: r0v0, types: [android.support.v7.util.ListUpdateCallback] */
        /* JADX WARNING: type inference failed for: r1v1, types: [android.support.v7.util.BatchingListUpdateCallback] */
        /* JADX WARNING: type inference failed for: r0v1 */
        /* JADX WARNING: type inference failed for: r1v2 */
        /* JADX WARNING: type inference failed for: r0v2 */
        /* JADX WARNING: type inference failed for: r9v0 */
        /* JADX WARNING: type inference failed for: r1v4 */
        /* JADX WARNING: type inference failed for: r1v5, types: [android.support.v7.util.BatchingListUpdateCallback] */
        /* JADX WARNING: type inference failed for: r0v13 */
        /* JADX WARNING: type inference failed for: r1v6 */
        /* JADX WARNING: type inference failed for: r1v7 */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Unknown variable types count: 7 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatchUpdatesTo(android.support.p004v7.util.ListUpdateCallback r18) {
            /*
                r17 = this;
                r8 = r17
                r0 = r18
                boolean r1 = r0 instanceof android.support.p004v7.util.BatchingListUpdateCallback
                if (r1 == 0) goto L_0x000e
                r1 = r0
                android.support.v7.util.BatchingListUpdateCallback r1 = (android.support.p004v7.util.BatchingListUpdateCallback) r1
            L_0x000b:
                r9 = r0
                r10 = r1
                goto L_0x0015
            L_0x000e:
                android.support.v7.util.BatchingListUpdateCallback r1 = new android.support.v7.util.BatchingListUpdateCallback
                r1.<init>(r0)
                r0 = r1
                goto L_0x000b
            L_0x0015:
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                int r0 = r8.mOldListSize
                int r2 = r8.mNewListSize
                java.util.List<android.support.v7.util.DiffUtil$Snake> r3 = r8.mSnakes
                int r3 = r3.size()
                r11 = 1
                int r3 = r3 - r11
                r12 = r0
                r13 = r2
            L_0x0028:
                r14 = r3
                if (r14 < 0) goto L_0x0092
                java.util.List<android.support.v7.util.DiffUtil$Snake> r0 = r8.mSnakes
                java.lang.Object r0 = r0.get(r14)
                r15 = r0
                android.support.v7.util.DiffUtil$Snake r15 = (android.support.p004v7.util.DiffUtil.Snake) r15
                int r7 = r15.size
                int r0 = r15.f49x
                int r6 = r0 + r7
                int r0 = r15.f50y
                int r5 = r0 + r7
                if (r6 >= r12) goto L_0x004b
                int r4 = r12 - r6
                r0 = r8
                r2 = r10
                r3 = r6
                r11 = r5
                r5 = r6
                r0.dispatchRemovals(r1, r2, r3, r4, r5)
                goto L_0x004c
            L_0x004b:
                r11 = r5
            L_0x004c:
                if (r11 >= r13) goto L_0x005d
                int r0 = r13 - r11
                r2 = r8
                r3 = r1
                r4 = r10
                r5 = r6
                r16 = r6
                r6 = r0
                r0 = r7
                r7 = r11
                r2.dispatchAdditions(r3, r4, r5, r6, r7)
                goto L_0x0060
            L_0x005d:
                r16 = r6
                r0 = r7
            L_0x0060:
                int r7 = r0 + -1
            L_0x0062:
                r2 = r7
                if (r2 < 0) goto L_0x0089
                int[] r3 = r8.mOldItemStatuses
                int r4 = r15.f49x
                int r4 = r4 + r2
                r3 = r3[r4]
                r3 = r3 & 31
                r4 = 2
                if (r3 != r4) goto L_0x0085
                int r3 = r15.f49x
                int r3 = r3 + r2
                android.support.v7.util.DiffUtil$Callback r4 = r8.mCallback
                int r5 = r15.f49x
                int r5 = r5 + r2
                int r6 = r15.f50y
                int r6 = r6 + r2
                java.lang.Object r4 = r4.getChangePayload(r5, r6)
                r5 = 1
                r10.onChanged(r3, r5, r4)
                goto L_0x0086
            L_0x0085:
                r5 = 1
            L_0x0086:
                int r7 = r2 + -1
                goto L_0x0062
            L_0x0089:
                r5 = 1
                int r12 = r15.f49x
                int r13 = r15.f50y
                int r3 = r14 + -1
                r11 = 1
                goto L_0x0028
            L_0x0092:
                r10.dispatchLastEvent()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.util.DiffUtil.DiffResult.dispatchUpdatesTo(android.support.v7.util.ListUpdateCallback):void");
        }

        private static PostponedUpdate removePostponedUpdate(List<PostponedUpdate> updates, int pos, boolean removal) {
            for (int i = updates.size() - 1; i >= 0; i--) {
                PostponedUpdate update = (PostponedUpdate) updates.get(i);
                if (update.posInOwnerList == pos && update.removal == removal) {
                    updates.remove(i);
                    for (int j = i; j < updates.size(); j++) {
                        PostponedUpdate postponedUpdate = (PostponedUpdate) updates.get(j);
                        postponedUpdate.currentPos += removal ? 1 : -1;
                    }
                    return update;
                }
            }
            return null;
        }

        private void dispatchAdditions(List<PostponedUpdate> postponedUpdates, ListUpdateCallback updateCallback, int start, int count, int globalIndex) {
            if (!this.mDetectMoves) {
                updateCallback.onInserted(start, count);
                return;
            }
            for (int i = count - 1; i >= 0; i--) {
                int status = this.mNewItemStatuses[globalIndex + i] & 31;
                if (status == 0) {
                    updateCallback.onInserted(start, 1);
                    for (PostponedUpdate update : postponedUpdates) {
                        update.currentPos++;
                    }
                } else if (status == 4 || status == 8) {
                    int pos = this.mNewItemStatuses[globalIndex + i] >> 5;
                    updateCallback.onMoved(removePostponedUpdate(postponedUpdates, pos, true).currentPos, start);
                    if (status == 4) {
                        updateCallback.onChanged(start, 1, this.mCallback.getChangePayload(pos, globalIndex + i));
                    }
                } else if (status != 16) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown flag for pos ");
                    sb.append(globalIndex + i);
                    sb.append(" ");
                    sb.append(Long.toBinaryString((long) status));
                    throw new IllegalStateException(sb.toString());
                } else {
                    postponedUpdates.add(new PostponedUpdate(globalIndex + i, start, false));
                }
            }
        }

        private void dispatchRemovals(List<PostponedUpdate> postponedUpdates, ListUpdateCallback updateCallback, int start, int count, int globalIndex) {
            if (!this.mDetectMoves) {
                updateCallback.onRemoved(start, count);
                return;
            }
            for (int i = count - 1; i >= 0; i--) {
                int status = this.mOldItemStatuses[globalIndex + i] & 31;
                if (status == 0) {
                    updateCallback.onRemoved(start + i, 1);
                    for (PostponedUpdate update : postponedUpdates) {
                        update.currentPos--;
                    }
                } else if (status == 4 || status == 8) {
                    int pos = this.mOldItemStatuses[globalIndex + i] >> 5;
                    PostponedUpdate update2 = removePostponedUpdate(postponedUpdates, pos, false);
                    updateCallback.onMoved(start + i, update2.currentPos - 1);
                    if (status == 4) {
                        updateCallback.onChanged(update2.currentPos - 1, 1, this.mCallback.getChangePayload(globalIndex + i, pos));
                    }
                } else if (status != 16) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown flag for pos ");
                    sb.append(globalIndex + i);
                    sb.append(" ");
                    sb.append(Long.toBinaryString((long) status));
                    throw new IllegalStateException(sb.toString());
                } else {
                    postponedUpdates.add(new PostponedUpdate(globalIndex + i, start + i, true));
                }
            }
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public List<Snake> getSnakes() {
            return this.mSnakes;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$ItemCallback */
    public static abstract class ItemCallback<T> {
        public abstract boolean areContentsTheSame(T t, T t2);

        public abstract boolean areItemsTheSame(T t, T t2);

        public Object getChangePayload(T t, T t2) {
            return null;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$PostponedUpdate */
    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int posInOwnerList2, int currentPos2, boolean removal2) {
            this.posInOwnerList = posInOwnerList2;
            this.currentPos = currentPos2;
            this.removal = removal2;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$Range */
    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int oldListStart2, int oldListEnd2, int newListStart2, int newListEnd2) {
            this.oldListStart = oldListStart2;
            this.oldListEnd = oldListEnd2;
            this.newListStart = newListStart2;
            this.newListEnd = newListEnd2;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$Snake */
    static class Snake {
        boolean removal;
        boolean reverse;
        int size;

        /* renamed from: x */
        int f49x;

        /* renamed from: y */
        int f50y;

        Snake() {
        }
    }

    private DiffUtil() {
    }

    public static DiffResult calculateDiff(Callback cb) {
        return calculateDiff(cb, true);
    }

    public static DiffResult calculateDiff(Callback cb, boolean detectMoves) {
        int oldSize = cb.getOldListSize();
        int newSize = cb.getNewListSize();
        ArrayList arrayList = new ArrayList();
        List<Range> stack = new ArrayList<>();
        stack.add(new Range(0, oldSize, 0, newSize));
        int max = oldSize + newSize + Math.abs(oldSize - newSize);
        int[] forward = new int[(max * 2)];
        int[] backward = new int[(max * 2)];
        ArrayList arrayList2 = new ArrayList();
        while (true) {
            ArrayList arrayList3 = arrayList2;
            if (!stack.isEmpty()) {
                Range range = (Range) stack.remove(stack.size() - 1);
                Snake snake = diffPartial(cb, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, forward, backward, max);
                if (snake != null) {
                    if (snake.size > 0) {
                        arrayList.add(snake);
                    }
                    snake.f49x += range.oldListStart;
                    snake.f50y += range.newListStart;
                    Range left = arrayList3.isEmpty() ? new Range() : (Range) arrayList3.remove(arrayList3.size() - 1);
                    left.oldListStart = range.oldListStart;
                    left.newListStart = range.newListStart;
                    if (snake.reverse) {
                        left.oldListEnd = snake.f49x;
                        left.newListEnd = snake.f50y;
                    } else if (snake.removal) {
                        left.oldListEnd = snake.f49x - 1;
                        left.newListEnd = snake.f50y;
                    } else {
                        left.oldListEnd = snake.f49x;
                        left.newListEnd = snake.f50y - 1;
                    }
                    stack.add(left);
                    Range right = range;
                    if (!snake.reverse) {
                        right.oldListStart = snake.f49x + snake.size;
                        right.newListStart = snake.f50y + snake.size;
                    } else if (snake.removal) {
                        right.oldListStart = snake.f49x + snake.size + 1;
                        right.newListStart = snake.f50y + snake.size;
                    } else {
                        right.oldListStart = snake.f49x + snake.size;
                        right.newListStart = snake.f50y + snake.size + 1;
                    }
                    stack.add(right);
                } else {
                    arrayList3.add(range);
                }
                arrayList2 = arrayList3;
            } else {
                Collections.sort(arrayList, SNAKE_COMPARATOR);
                ArrayList arrayList4 = arrayList3;
                int[] iArr = backward;
                int[] iArr2 = forward;
                DiffResult diffResult = new DiffResult(cb, arrayList, forward, backward, detectMoves);
                return diffResult;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0053, code lost:
        if (r4[(r28 + r13) - 1] < r4[(r28 + r13) + 1]) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f6, code lost:
        if (r5[(r28 + r3) - 1] < r5[(r28 + r3) + 1]) goto L_0x0103;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x008d A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.support.p004v7.util.DiffUtil.Snake diffPartial(android.support.p004v7.util.DiffUtil.Callback r21, int r22, int r23, int r24, int r25, int[] r26, int[] r27, int r28) {
        /*
            r0 = r21
            r4 = r26
            r5 = r27
            int r6 = r23 - r22
            int r7 = r25 - r24
            int r8 = r23 - r22
            r9 = 1
            if (r8 < r9) goto L_0x017c
            int r8 = r25 - r24
            if (r8 >= r9) goto L_0x0019
            r18 = r6
            r19 = r7
            goto L_0x0180
        L_0x0019:
            int r8 = r6 - r7
            int r10 = r6 + r7
            int r10 = r10 + r9
            int r10 = r10 / 2
            int r11 = r28 - r10
            int r11 = r11 - r9
            int r12 = r28 + r10
            int r12 = r12 + r9
            r13 = 0
            java.util.Arrays.fill(r4, r11, r12, r13)
            int r11 = r28 - r10
            int r11 = r11 - r9
            int r11 = r11 + r8
            int r12 = r28 + r10
            int r12 = r12 + r9
            int r12 = r12 + r8
            java.util.Arrays.fill(r5, r11, r12, r6)
            int r11 = r8 % 2
            if (r11 == 0) goto L_0x003b
            r11 = 1
            goto L_0x003c
        L_0x003b:
            r11 = 0
        L_0x003c:
            r12 = 0
        L_0x003d:
            if (r12 > r10) goto L_0x0170
            int r13 = -r12
        L_0x0040:
            if (r13 > r12) goto L_0x00d9
            int r9 = -r12
            if (r13 == r9) goto L_0x005f
            if (r13 == r12) goto L_0x0056
            int r9 = r28 + r13
            r15 = 1
            int r9 = r9 - r15
            r9 = r4[r9]
            int r16 = r28 + r13
            int r16 = r16 + 1
            r2 = r4[r16]
            if (r9 >= r2) goto L_0x0057
            goto L_0x0060
        L_0x0056:
            r15 = 1
        L_0x0057:
            int r2 = r28 + r13
            int r2 = r2 - r15
            r2 = r4[r2]
            int r2 = r2 + r15
            r9 = 1
            goto L_0x0066
        L_0x005f:
            r15 = 1
        L_0x0060:
            int r2 = r28 + r13
            int r2 = r2 + r15
            r2 = r4[r2]
            r9 = 0
        L_0x0066:
            int r16 = r2 - r13
        L_0x0069:
            r17 = r16
            if (r2 >= r6) goto L_0x008d
            r3 = r17
            if (r3 >= r7) goto L_0x0088
            r18 = r6
            int r6 = r22 + r2
            r19 = r7
            int r7 = r24 + r3
            boolean r6 = r0.areItemsTheSame(r6, r7)
            if (r6 == 0) goto L_0x0093
            int r2 = r2 + 1
            int r16 = r3 + 1
            r6 = r18
            r7 = r19
            goto L_0x0069
        L_0x0088:
            r18 = r6
            r19 = r7
            goto L_0x0093
        L_0x008d:
            r18 = r6
            r19 = r7
            r3 = r17
        L_0x0093:
            int r6 = r28 + r13
            r4[r6] = r2
            if (r11 == 0) goto L_0x00cf
            int r6 = r8 - r12
            r7 = 1
            int r6 = r6 + r7
            if (r13 < r6) goto L_0x00cf
            int r6 = r8 + r12
            int r6 = r6 - r7
            if (r13 > r6) goto L_0x00cf
            int r6 = r28 + r13
            r6 = r4[r6]
            int r7 = r28 + r13
            r7 = r5[r7]
            if (r6 < r7) goto L_0x00cf
            android.support.v7.util.DiffUtil$Snake r6 = new android.support.v7.util.DiffUtil$Snake
            r6.<init>()
            int r7 = r28 + r13
            r7 = r5[r7]
            r6.f49x = r7
            int r7 = r6.f49x
            int r7 = r7 - r13
            r6.f50y = r7
            int r7 = r28 + r13
            r7 = r4[r7]
            int r15 = r28 + r13
            r15 = r5[r15]
            int r7 = r7 - r15
            r6.size = r7
            r6.removal = r9
            r7 = 0
            r6.reverse = r7
            return r6
        L_0x00cf:
            r7 = 0
            int r13 = r13 + 2
            r6 = r18
            r7 = r19
            r9 = 1
            goto L_0x0040
        L_0x00d9:
            r18 = r6
            r19 = r7
            r7 = 0
            int r2 = -r12
        L_0x00df:
            if (r2 > r12) goto L_0x0165
            int r3 = r2 + r8
            int r6 = r12 + r8
            if (r3 == r6) goto L_0x0102
            int r6 = -r12
            int r6 = r6 + r8
            if (r3 == r6) goto L_0x00f9
            int r6 = r28 + r3
            r15 = 1
            int r6 = r6 - r15
            r6 = r5[r6]
            int r9 = r28 + r3
            int r9 = r9 + r15
            r9 = r5[r9]
            if (r6 >= r9) goto L_0x00fa
            goto L_0x0103
        L_0x00f9:
            r15 = 1
        L_0x00fa:
            int r6 = r28 + r3
            int r6 = r6 + r15
            r6 = r5[r6]
            int r6 = r6 - r15
            r9 = 1
            goto L_0x0109
        L_0x0102:
            r15 = 1
        L_0x0103:
            int r6 = r28 + r3
            int r6 = r6 - r15
            r6 = r5[r6]
            r9 = 0
        L_0x0109:
            int r13 = r6 - r3
        L_0x010c:
            if (r6 <= 0) goto L_0x0125
            if (r13 <= 0) goto L_0x0125
            int r14 = r22 + r6
            r15 = 1
            int r7 = r14 + -1
            int r14 = r24 + r13
            int r1 = r14 + -1
            boolean r1 = r0.areItemsTheSame(r7, r1)
            if (r1 == 0) goto L_0x0125
            int r6 = r6 + -1
            int r13 = r13 + -1
            r7 = 0
            goto L_0x010c
        L_0x0125:
            int r1 = r28 + r3
            r5[r1] = r6
            if (r11 != 0) goto L_0x015f
            int r1 = r2 + r8
            int r7 = -r12
            if (r1 < r7) goto L_0x015f
            int r1 = r2 + r8
            if (r1 > r12) goto L_0x015f
            int r1 = r28 + r3
            r1 = r4[r1]
            int r7 = r28 + r3
            r7 = r5[r7]
            if (r1 < r7) goto L_0x015f
            android.support.v7.util.DiffUtil$Snake r1 = new android.support.v7.util.DiffUtil$Snake
            r1.<init>()
            int r7 = r28 + r3
            r7 = r5[r7]
            r1.f49x = r7
            int r7 = r1.f49x
            int r7 = r7 - r3
            r1.f50y = r7
            int r7 = r28 + r3
            r7 = r4[r7]
            int r14 = r28 + r3
            r14 = r5[r14]
            int r7 = r7 - r14
            r1.size = r7
            r1.removal = r9
            r7 = 1
            r1.reverse = r7
            return r1
        L_0x015f:
            r7 = 1
            int r2 = r2 + 2
            r7 = 0
            goto L_0x00df
        L_0x0165:
            r7 = 1
            int r12 = r12 + 1
            r6 = r18
            r7 = r19
            r9 = 1
            r13 = 0
            goto L_0x003d
        L_0x0170:
            r18 = r6
            r19 = r7
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.String r2 = "DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation."
            r1.<init>(r2)
            throw r1
        L_0x017c:
            r18 = r6
            r19 = r7
        L_0x0180:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.util.DiffUtil.diffPartial(android.support.v7.util.DiffUtil$Callback, int, int, int, int, int[], int[], int):android.support.v7.util.DiffUtil$Snake");
    }
}
