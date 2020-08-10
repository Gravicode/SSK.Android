package bleshadow.dagger.internal;

import bleshadow.dagger.MembersInjector;

public final class MembersInjectors {

    private enum NoOpMembersInjector implements MembersInjector<Object> {
        INSTANCE;

        public void injectMembers(Object instance) {
            Preconditions.checkNotNull(instance, "Cannot inject members into a null reference");
        }
    }

    public static <T> MembersInjector<T> noOp() {
        return NoOpMembersInjector.INSTANCE;
    }

    private MembersInjectors() {
    }
}
