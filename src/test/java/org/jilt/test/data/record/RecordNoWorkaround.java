package org.jilt.test.data.record;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

/**
 * Example JavaDoc for a record class.
 *
 * @param name Example JavaDoc of the {@code name} parameter.
 * @param age Example JavaDoc of the {@code age} parameter.
 */
@Builder(style = BuilderStyle.STAGED, toBuilder = "copy")
public record RecordNoWorkaround(@Opt String name, int age) {
}
