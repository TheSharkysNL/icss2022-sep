package nl.han.ica.icss;

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
    }

    boolean isError();

    E error();

    T value();
}
