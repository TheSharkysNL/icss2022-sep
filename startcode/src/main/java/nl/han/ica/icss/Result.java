package nl.han.ica.icss;

import java.util.Optional;

public sealed interface Result<T, E> {
    record Success<T, E>(T value) implements Result<T, E> {
        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public E error() {
            throw new RuntimeException("Cannot get error for a value result.");
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public Optional<E> ok() {
            return Optional.empty();
        }
    }
    record Error<T, E>(E exception) implements Result<T, E> {
        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public E error() {
            return exception;
        }

        @Override
        public T value() {
            throw new RuntimeException("Cannot get value for a error result.");
        }

        @Override
        public Optional<E> ok() {
            return Optional.of(exception);
        }
    }

    boolean isError();

    E error();

    T value();

    Optional<E> ok();

    static <T, E> Result<T, E> of(T value) {
        return new Success<>(value);
    }

    static <T, E> Result<T, E> err(E error) {
        return new Error<>(error);
    }
}
