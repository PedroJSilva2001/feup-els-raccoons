package pt.up.fe.els2023.utils;

/**
 * This class wraps a value and compares it by identity.
 *
 * @param value The value to wrap.
 * @param <T> The type of the value.
 */
public record IdentityWrapper<T>(T value) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityWrapper<?> that = (IdentityWrapper<?>) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(value);
    }
}
