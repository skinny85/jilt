package org.jilt.test.data.singular;

import org.jilt.Builder;
import org.jilt.Singular;
import java.util.List;

@Builder
public class SingularDemo {
    @Singular
    private final List<String> tags;

    @Singular
    private final List<? extends String> tagsWithGenerics;

    public SingularDemo(List<String> tags, List<? extends String> tagsWithGenerics) {
        this.tags = tags;
        this.tagsWithGenerics = tagsWithGenerics;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<? extends String> getTagsWithGenerics() {
        return tags;
    }
}
