package com.nekiplay.hypixelcry.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class SimpleTimeMark implements Comparable<SimpleTimeMark> {
    private final long millis;

    private SimpleTimeMark(long millis) {
        this.millis = millis;
    }

    public Duration minus(SimpleTimeMark other) {
        return Duration.ofMillis(millis - other.millis);
    }

    public SimpleTimeMark plus(Duration other) {
        return new SimpleTimeMark(millis + other.toMillis());
    }

    public SimpleTimeMark minus(Duration other) {
        return plus(other.negated());
    }

    public Duration passedSince() {
        return now().minus(this);
    }

    public Duration timeUntil() {
        return passedSince().negated();
    }

    public boolean isInPast() {
        return timeUntil().isNegative();
    }

    public boolean isInFuture() {
        return timeUntil().isPositive();
    }

    public boolean isFarPast() {
        return millis == 0L;
    }

    public boolean isFarFuture() {
        return millis == Long.MAX_VALUE;
    }

    public SimpleTimeMark takeIfInitialized() {
        return (isFarPast() || isFarFuture()) ? null : this;
    }

    public Duration absoluteDifference(SimpleTimeMark other) {
        return Duration.ofMillis(Math.abs(millis - other.millis));
    }

    @Override
    public int compareTo(SimpleTimeMark other) {
        return Long.compare(millis, other.millis);
    }

    @Override
    public String toString() {
        if (isFarPast()) return "The Far Past";
        if (isFarFuture()) return "The Far Future";
        return Instant.ofEpochMilli(millis).toString();
    }


    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public long toMillis() {
        return millis;
    }

    public LocalDate toLocalDate() {
        return toLocalDateTime().toLocalDate();
    }

    public static SimpleTimeMark now() {
        return new SimpleTimeMark(System.currentTimeMillis());
    }

    public static SimpleTimeMark farPast() {
        return new SimpleTimeMark(0);
    }

    public static SimpleTimeMark farFuture() {
        return new SimpleTimeMark(Long.MAX_VALUE);
    }

    public static SimpleTimeMark fromNow(Duration duration) {
        return now().plus(duration);
    }

    public static SimpleTimeMark fromMillis(long millis) {
        return new SimpleTimeMark(millis);
    }

    // Note: You'll need to implement Duration class if you don't have it
    public static final class Duration {
        private final long millis;

        private Duration(long millis) {
            this.millis = millis;
        }

        public static Duration ofMillis(long millis) {
            return new Duration(millis);
        }

        public long toMillis() {
            return millis;
        }

        public boolean isNegative() {
            return millis < 0;
        }

        public boolean isPositive() {
            return millis > 0;
        }

        public Duration negated() {
            return new Duration(-millis);
        }
    }
}