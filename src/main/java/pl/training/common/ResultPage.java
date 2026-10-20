package pl.training.common;

import java.util.List;
import java.util.function.Function;

public record ResultPage<T>(List<T> content, PageSpec pageSpec, int totalPages) {

    public <O> ResultPage<O> map(final Function<T, O> mapper) {
        var mappedContent = content.stream().map(mapper).toList();
        return new ResultPage<>(mappedContent, pageSpec, totalPages);
    }

}
