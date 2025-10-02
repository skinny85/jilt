package org.jilt.test;

import org.jilt.Builder;
import org.jilt.Singular;
import java.util.List;

@Builder
public class SingularDemo {
    @Singular
    private List<String> tags;

    public SingularDemo(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
