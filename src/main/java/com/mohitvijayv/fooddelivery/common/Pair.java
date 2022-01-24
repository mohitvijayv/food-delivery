package com.mohitvijayv.fooddelivery.common;

import java.util.Objects;

public class Pair<F, S> {
    public final F key;
    public final S value;

    public F getKey() {
        return key;
    }

    public S getValue() {
        return value;
    }

    public Pair(F key, S value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.key, key) && Objects.equals(p.value, value);
    }

    @Override
    public int hashCode() {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<A, B>(a, b);
    }
}
