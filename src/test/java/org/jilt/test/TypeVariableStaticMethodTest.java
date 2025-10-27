package org.jilt.test;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

abstract class Base {
    public final LocalDateTime dateTime;
    public final Long id;
    public final Set<Long> longs;

    protected Base(LocalDateTime dateTime, Long id, Set<Long> longs) {
        this.dateTime = dateTime;
        this.id = id;
        this.longs = longs;
    }

    public interface DateTimeStage<T extends Base> {
        IdStage<T> withDateTime(LocalDateTime dateTime);
    }

    public interface IdStage<T extends Base> {
        LongsStage<T> withId(Long id);
    }

    public interface LongsStage<T extends Base> {
        BuildStage<T> withLongs(Set<Long> longs);
    }

    public interface BuildStage<T extends Base> {
        T build();
    }

    protected abstract static class BaseBuilder<T extends Base> implements
            DateTimeStage<T>, IdStage<T>, LongsStage<T>, BuildStage<T> {
        protected LocalDateTime dateTime;
        protected Long id;
        protected Set<Long> longs;

        protected BaseBuilder() {}

        @Override
        public IdStage<T> withDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        @Override
        public LongsStage<T> withId(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public BuildStage<T> withLongs(Set<Long> longs) {
            this.longs = longs;
            return this;
        }
    }
}

class A extends Base {
    private A(Builder builder) {
        this(builder.dateTime, builder.id, builder.longs);
    }

    private A(LocalDateTime dateTime, Long id, Set<Long> longs) {
        super(dateTime, id, longs);
    }

    public static DateTimeStage<A> builder() {
        return new Builder();
    }

    private static final class Builder extends BaseBuilder<A> {
        @Override
        public A build() {
            return new A(this);
        }
    }

    public static JiltBaseBuilders.DateTime<A> jiltBuilder() {
        return new JiltBuilder();
    }

    private static final class JiltBuilder extends JiltBaseBuilder<A> {
        @Override
        public A build() {
            return new A(this.dateTime, this.id, this.longs);
        }
    }
}

class B extends Base {
    private B(Builder builder) {
        this(builder.dateTime, builder.id, builder.longs);
    }

    private B(LocalDateTime dateTime, Long id, Set<Long> longs) {
        super(dateTime, id, longs);
    }

    public static DateTimeStage<B> builder() {
        return new Builder();
    }

    private static final class Builder extends BaseBuilder<B> {
        @Override
        public B build() {
            return new B(this);
        }
    }

    public static JiltBaseBuilders.DateTime<B> jiltBuilder() {
        return new JiltBuilder();
    }

    private static final class JiltBuilder extends JiltBaseBuilder<B> {
        @Override
        public B build() {
            return new B(this.dateTime, this.id, this.longs);
        }
    }
}

class Utils {
    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }
}

class OriginalUsage {
    static Base generateBase(boolean isA) {
        return isA ? buildImpl(A.builder()) : buildImpl(B.builder());
    }

    private static <T extends Base> T buildImpl(Base.DateTimeStage<T> builder) {
        return builder
                .withDateTime(LocalDateTime.now())
                .withId(1L)
                .withLongs(Utils.setOf(1L, 2L))
                .build();
    }
}

class JiltContainer {
    @Builder(className = "JiltBaseBuilder", packageName = "org.jilt.test", style = BuilderStyle.STAGED, setterPrefix = "with")
    private static <T extends Base> T buildBase(
            LocalDateTime dateTime,
            Long id,
            Set<Long> longs) {
        throw new RuntimeException("Doesn't matter, won't be called anyway");
    }
}

class JiltUsage {
    static Base generateBase(boolean isA) {
        return isA ? buildImpl(A.jiltBuilder()) : buildImpl(B.jiltBuilder());
    }

    private static <T extends Base> T buildImpl(JiltBaseBuilders.DateTime<T> builder) {
        return builder
                .withDateTime(LocalDateTime.now())
                .withId(-1L)
                .withLongs(Utils.setOf(3L, 4L))
                .build();
    }
}

public class TypeVariableStaticMethodTest {
    @Test
    public void original_generateBase_true_returns_A_instance() {
        Base a = OriginalUsage.generateBase(true);

        assertThat(a).isInstanceOf(A.class);
        assertThat(a.dateTime).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(a.id).isEqualTo(1);
        assertThat(a.longs).containsOnly(1L, 2L);
    }

    @Test
    public void original_generateBase_false_returns_B_instance() {
        Base b = OriginalUsage.generateBase(false);

        assertThat(b).isInstanceOf(B.class);
        assertThat(b.dateTime).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(b.id).isEqualTo(1);
        assertThat(b.longs).containsOnly(1L, 2L);
    }

    @Test
    public void jilt_generateBase_true_returns_A_instance() {
        Base a = JiltUsage.generateBase(true);

        assertThat(a).isInstanceOf(A.class);
        assertThat(a.dateTime).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(a.id).isEqualTo(-1);
        assertThat(a.longs).containsOnly(3L, 4L);
    }

    @Test
    public void jilt_generateBase_false_returns_B_instance() {
        Base b = JiltUsage.generateBase(false);

        assertThat(b).isInstanceOf(B.class);
        assertThat(b.dateTime).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(b.id).isEqualTo(-1);
        assertThat(b.longs).containsOnly(3L, 4L);
    }
}
