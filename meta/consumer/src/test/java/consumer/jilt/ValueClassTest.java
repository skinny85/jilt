package consumer.jilt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueClassTest {
    @Test
    public void test_value_class() {
        ValueClass valueClass = new ValueClass(
                "name", 18, 'W'
        );
        ValueClassBuilder valueClassBuilder = new ValueClassBuilder();

        assertThat(valueClass).isNotNull();
    }
}
