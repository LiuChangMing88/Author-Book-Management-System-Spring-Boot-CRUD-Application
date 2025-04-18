package com.learning.PGdatabase.mappers;

public interface Mapper<B, T> {
    T mapTo (B b);
    B mapFrom (T t);
}
