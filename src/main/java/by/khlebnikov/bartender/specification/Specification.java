package by.khlebnikov.bartender.specification;

import java.util.List;

public interface Specification<T> {
    List<T> specified();
}
