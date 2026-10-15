package br.com.engine.engine;

public interface IStorageEngine<T> {
    void PUT(T key);
    String GET(T key);
    void DELETE(T key);
}
