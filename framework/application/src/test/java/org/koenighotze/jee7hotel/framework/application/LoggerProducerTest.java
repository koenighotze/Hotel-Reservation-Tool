package org.koenighotze.jee7hotel.framework.application;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.koenighotze.jee7hotel.LoggerProducer;

import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.reflect.Member;
import java.util.logging.Logger;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class LoggerProducerTest {

    @Mocked
    private InjectionPoint point;

    @Mocked
    private Member member;

    @Test
    public void the_logger_uses_the_injection_points_declaring_class() {
        new Expectations() {
            {
                member.getDeclaringClass();
                result = LoggerProducerTest.class;
            }

            {
                point.getMember();
                result = member;
            }
        };

        Logger logger = new LoggerProducer().createLogger(point);

        assertThat(logger.getName()).isEqualTo(LoggerProducerTest.class.getName());
    }
}