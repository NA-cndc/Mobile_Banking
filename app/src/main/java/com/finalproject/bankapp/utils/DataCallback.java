package com.finalproject.bankapp.utils;

public interface DataCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}
