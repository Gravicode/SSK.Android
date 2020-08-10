package org.apache.poi.p009ss.formula;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/* renamed from: org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment */
public final class CollaboratingWorkbooksEnvironment {
    public static final CollaboratingWorkbooksEnvironment EMPTY = new CollaboratingWorkbooksEnvironment();
    private final WorkbookEvaluator[] _evaluators;
    private final Map<String, WorkbookEvaluator> _evaluatorsByName;
    private boolean _unhooked;

    /* renamed from: org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment$WorkbookNotFoundException */
    public static final class WorkbookNotFoundException extends Exception {
        WorkbookNotFoundException(String msg) {
            super(msg);
        }
    }

    private CollaboratingWorkbooksEnvironment() {
        this._evaluatorsByName = Collections.emptyMap();
        this._evaluators = new WorkbookEvaluator[0];
    }

    public static void setup(String[] workbookNames, WorkbookEvaluator[] evaluators) {
        int nItems = workbookNames.length;
        if (evaluators.length != nItems) {
            StringBuilder sb = new StringBuilder();
            sb.append("Number of workbook names is ");
            sb.append(nItems);
            sb.append(" but number of evaluators is ");
            sb.append(evaluators.length);
            throw new IllegalArgumentException(sb.toString());
        } else if (nItems < 1) {
            throw new IllegalArgumentException("Must provide at least one collaborating worbook");
        } else {
            new CollaboratingWorkbooksEnvironment(workbookNames, evaluators, nItems);
        }
    }

    private CollaboratingWorkbooksEnvironment(String[] workbookNames, WorkbookEvaluator[] evaluators, int nItems) {
        Map<String, WorkbookEvaluator> m = new HashMap<>((nItems * 3) / 2);
        IdentityHashMap<WorkbookEvaluator, String> uniqueEvals = new IdentityHashMap<>((nItems * 3) / 2);
        int i = 0;
        while (i < nItems) {
            String wbName = workbookNames[i];
            WorkbookEvaluator wbEval = evaluators[i];
            if (m.containsKey(wbName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Duplicate workbook name '");
                sb.append(wbName);
                sb.append("'");
                throw new IllegalArgumentException(sb.toString());
            } else if (uniqueEvals.containsKey(wbEval)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Attempted to register same workbook under names '");
                sb2.append((String) uniqueEvals.get(wbEval));
                sb2.append("' and '");
                sb2.append(wbName);
                sb2.append("'");
                throw new IllegalArgumentException(sb2.toString());
            } else {
                uniqueEvals.put(wbEval, wbName);
                m.put(wbName, wbEval);
                i++;
            }
        }
        unhookOldEnvironments(evaluators);
        hookNewEnvironment(evaluators, this);
        this._unhooked = false;
        this._evaluators = evaluators;
        this._evaluatorsByName = m;
    }

    private static void hookNewEnvironment(WorkbookEvaluator[] evaluators, CollaboratingWorkbooksEnvironment env) {
        IEvaluationListener evalListener = evaluators[0].getEvaluationListener();
        for (WorkbookEvaluator evaluationListener : evaluators) {
            if (evalListener != evaluationListener.getEvaluationListener()) {
                throw new RuntimeException("Workbook evaluators must all have the same evaluation listener");
            }
        }
        EvaluationCache cache = new EvaluationCache(evalListener);
        for (int i = 0; i < nItems; i++) {
            evaluators[i].attachToEnvironment(env, cache, i);
        }
    }

    private void unhookOldEnvironments(WorkbookEvaluator[] evaluators) {
        Set<CollaboratingWorkbooksEnvironment> oldEnvs = new HashSet<>();
        for (WorkbookEvaluator environment : evaluators) {
            oldEnvs.add(environment.getEnvironment());
        }
        CollaboratingWorkbooksEnvironment[] oldCWEs = new CollaboratingWorkbooksEnvironment[oldEnvs.size()];
        oldEnvs.toArray(oldCWEs);
        for (CollaboratingWorkbooksEnvironment unhook : oldCWEs) {
            unhook.unhook();
        }
    }

    private void unhook() {
        if (this._evaluators.length >= 1) {
            for (WorkbookEvaluator detachFromEnvironment : this._evaluators) {
                detachFromEnvironment.detachFromEnvironment();
            }
            this._unhooked = true;
        }
    }

    public WorkbookEvaluator getWorkbookEvaluator(String workbookName) throws WorkbookNotFoundException {
        if (this._unhooked) {
            throw new IllegalStateException("This environment has been unhooked");
        }
        WorkbookEvaluator result = (WorkbookEvaluator) this._evaluatorsByName.get(workbookName);
        if (result != null) {
            return result;
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("Could not resolve external workbook name '");
        sb.append(workbookName);
        sb.append("'.");
        if (this._evaluators.length < 1) {
            sb.append(" Workbook environment has not been set up.");
        } else {
            sb.append(" The following workbook names are valid: (");
            int count = 0;
            for (String append : this._evaluatorsByName.keySet()) {
                int count2 = count + 1;
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append("'");
                sb.append(append);
                sb.append("'");
                count = count2;
            }
            sb.append(")");
        }
        throw new WorkbookNotFoundException(sb.toString());
    }
}
