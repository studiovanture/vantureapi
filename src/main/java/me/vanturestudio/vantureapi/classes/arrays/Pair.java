package me.vanturestudio.vantureapi.classes.arrays;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Objects;

public class Pair<K, V> implements Entry<K, V>, Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = 8296563685697678334L;

    @Nullable
    protected K key;
    @Nullable
    protected V value;

    @Contract(pure = true)
    public Pair() {
        key = null;
        value = null;
    }

    @Contract(pure = true)
    public Pair(final @Nullable K k, final @Nullable V v) {
        this.key = k;
        this.value = v;
    }

    public Pair(final @NotNull Entry<K, V> e) {
        this.key = e.getKey();
        this.value = e.getValue();
    }

    public Pair<K, V> setKey(final @Nullable K k) {
        this.key = k;
        return this;
    }

    public Pair<K, V> set(final @Nullable K k, final @Nullable V v) {
        this.key = k;
        this.value = v;
        return this;
    }

    /**
     * @return "key,value"
     */
    @Override
    public String toString() {
        return key + "," + value;
    }

    /**
     * Checks for equality with Entries to match {@link #hashCode()}
     */
    @Contract(value = "null -> false", pure = true)
    @Override
    public final boolean equals(final @Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Entry))
            return false;
        final Entry<?, ?> other = (Entry<?, ?>) obj;
        final K first = this.key;
        final V second = this.value;
        return (first == null ? other.getKey() == null : first.equals(other.getKey())) &&
                (second == null ? other.getValue() == null : second.equals(other.getValue()));
    }

    /**
     * As defined by {@link Entry#hashCode()}
     */
    @Override
    public final int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    @Nullable
    public K getKey() {
        return key;
    }

    @Override
    @Nullable
    public V getValue() {
        return value;
    }

    @Override
    @Nullable
    public V setValue(final @Nullable V v) {
        final V old = this.value;
        this.value = v;
        return old;
    }

    /**
     * @return a shallow copy of this pair
     */
    @Override
    public Pair<K, V> clone() {
        return new Pair<>(this);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static Pair<String, Object> of(String first, Object second) {
        return new Pair<String, Object>().set(first, second);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static Pair<Object, Object> of(Object first, Object second) {
        return new Pair<>().set(first, second);
    }

}