package ru.chernyshev.myeighthtestappspringboot.model;

public enum Role {
    READ_ONLY, // только чтение (видит все записи, менять нельзя)
    USER,      // видит и меняет только свои записи
    ADMIN      // полный доступ
}
