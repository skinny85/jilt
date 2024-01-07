package org.jilt.test.data.record;

import org.jilt.Builder;

public record RecordWithWorkaround(String name, int age) {
    @Builder
    public RecordWithWorkaround {
    }
}
